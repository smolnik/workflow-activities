package net.adamsmolnik.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.adamsmolnik.boundary.dataimport.ImportActivityClient;
import net.adamsmolnik.boundary.dataimport.ImportActivityClientImpl;
import net.adamsmolnik.boundary.detection.DetectionActivityClient;
import net.adamsmolnik.boundary.detection.DetectionActivityClientImpl;
import net.adamsmolnik.boundary.digest.DigestActivityClient;
import net.adamsmolnik.boundary.digest.DigestActivityClientImpl;
import net.adamsmolnik.boundary.extraction.ExtractionActivityClient;
import net.adamsmolnik.boundary.extraction.ExtractionActivityClientImpl;
import net.adamsmolnik.boundary.notification.NotificationActivityClient;
import net.adamsmolnik.boundary.notification.NotificationActivityClientImpl;
import net.adamsmolnik.model.dataimport.ImportRequest;
import net.adamsmolnik.model.dataimport.ImportResponse;
import net.adamsmolnik.model.detection.DetectionRequest;
import net.adamsmolnik.model.detection.DetectionResponse;
import net.adamsmolnik.model.digest.DigestRequest;
import net.adamsmolnik.model.digest.DigestResponse;
import net.adamsmolnik.model.extraction.ExtractionRequest;
import net.adamsmolnik.model.extraction.ExtractionResponse;
import net.adamsmolnik.model.extraction.ExtractionStatus;
import net.adamsmolnik.model.notification.NotificationRequest;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.annotations.Wait;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

/**
 * @author ASmolnik *
 */
public class DataProcessingWorkflowImpl implements DataProcessingWorkflow {

    private static final String WF_INFO_TEMPLATE = "DataProcessingWorkflow has been %s for source objectKey %s";

    private volatile String state = "Just launched";

    private final ImportActivityClient imAC = new ImportActivityClientImpl();

    private final NotificationActivityClient nAC = new NotificationActivityClientImpl();

    private final DetectionActivityClient detectionAC = new DetectionActivityClientImpl();

    private final DigestActivityClient digestAC = new DigestActivityClientImpl();

    private final ExtractionActivityClient extractionAC = new ExtractionActivityClientImpl();

    private final Map<String, String> outcomeMetadata = new HashMap<>();

    private final AmazonS3Client s3Client = new AmazonS3Client();

    private String destObjectKey;

    private String bucketName;

    @Override
    public void launch(String bucketName, String srcObjectKey, Set<ActionType> actionTypes) {
        if (actionTypes.isEmpty()) {
            String noActionDefinedInfo = String.format("No action defined for ?", srcObjectKey);
            setState(noActionDefinedInfo);
            nAC.publish(new NotificationRequest(noActionDefinedInfo));
            return;
        }
        this.bucketName = bucketName;
        notifyConditionallyOnStart(srcObjectKey, actionTypes);
        Promise<ImportResponse> importResponse = imAC.doImport(new ImportRequest(srcObjectKey));
        processImportResponse(importResponse);
        Promise<Void> waitFor = takeActions(actionTypes, importResponse);
        finish(srcObjectKey, actionTypes, waitFor);
    }

    @Asynchronous
    private Promise<Void> takeActions(Set<ActionType> actionTypes, Promise<ImportResponse> importResponse) {
        String objectKey = importResponse.get().importedObjectKey;
        List<Promise<?>> actionsToBeWaitedFor = new ArrayList<>();
        boolean doExtraction = actionTypes.contains(ActionType.EXTRACT);
        boolean doDetection = actionTypes.contains(ActionType.DETECT);
        if (doDetection || doExtraction) {
            Promise<DetectionResponse> detectionResponse = detectionAC.detect(new DetectionRequest(objectKey));
            processDetectionResponse(detectionResponse);
            actionsToBeWaitedFor.add(detectionResponse);
            if (doExtraction) {
                Promise<ExtractionResponse> extractionResponse = extract(objectKey, doExtraction, detectionResponse);
                processExtractionResponse(extractionResponse);
                actionsToBeWaitedFor.add(extractionResponse);
            }
        }
        if (actionTypes.contains(ActionType.DIGEST)) {
            String algorithm = "SHA-256";
            Promise<DigestResponse> digestResponse = digestAC.digest(new DigestRequest(algorithm, objectKey));
            processDigestResponse(algorithm, digestResponse);
            actionsToBeWaitedFor.add(digestResponse);
        }
        return waitFor(actionsToBeWaitedFor);
    }

    private void notifyConditionallyOnStart(String srcObjectKey, Set<ActionType> actions) {
        if (actions.contains(ActionType.NOTIFY_ON_START)) {
            nAC.publish(new NotificationRequest(String.format(WF_INFO_TEMPLATE, "launched", srcObjectKey)));
        }
    }

    @Asynchronous
    private void processImportResponse(Promise<ImportResponse> importResponse) {
        this.destObjectKey = importResponse.get().importedObjectKey;
        setState("Just imported");
    }

    @Asynchronous
    private void processDetectionResponse(Promise<DetectionResponse> detectionResponse) {
        DetectionResponse dr = detectionResponse.get();
        setState("Just detected");
        setOutcomeMetadata("mediaType", dr.type + "/" + dr.subType);
    }

    @Asynchronous
    private void processDigestResponse(String algorithm, Promise<DigestResponse> digestResponse) {
        setState("The digest performed");
        setOutcomeMetadata("digest-" + algorithm, digestResponse.get().digest);
    }

    @Asynchronous
    private void processExtractionResponse(Promise<ExtractionResponse> extractionResponse) {
        ExtractionResponse er = extractionResponse.get();
        if (er.status == ExtractionStatus.NOT_ELIGIBLE) {
            return;
        }
        setState("Just extracted", extractionResponse);
        setOutcomeMetadata("extractionInfoObjectKey", er.extractionInfoObjectKey);
    }

    @Asynchronous
    private void finish(String srcObjectKey, Set<ActionType> actions, Promise<?> waitFor) {
        persistOutcomeMetadata();
        String completedState = "Completed";
        setState(completedState);
        if (actions.contains(ActionType.NOTIFY_ON_FINISH)) {
            nAC.publish(new NotificationRequest(String.format(WF_INFO_TEMPLATE, completedState, srcObjectKey
                    + " with the received outcome metadata: \n" + outcomeMetadata.toString())));
        }
    }

    @Asynchronous
    private Promise<Void> waitFor(@Wait List<Promise<?>> actionsDone) {
        return Promise.Void();
    }

    @Asynchronous
    private Promise<ExtractionResponse> extract(String objectKey, boolean doExtraction, Promise<DetectionResponse> detectionResponse) {
        final Promise<ExtractionResponse> extractionResponse = extractionAC
                .extract(new ExtractionRequest(objectKey, detectionResponse.get().subType));
        return extractionResponse;
    }

    @Asynchronous
    private void setState(String state, Promise<?>... waitFor) {
        this.state = state;
    }

    @Override
    public String getState() {
        return state;
    }

    private void setOutcomeMetadata(String key, String value) {
        outcomeMetadata.put(key, value);
    }

    private void persistOutcomeMetadata() {
        CopyObjectRequest cor = new CopyObjectRequest(bucketName, destObjectKey, bucketName, destObjectKey);
        ObjectMetadata omd = s3Client.getObjectMetadata(bucketName, destObjectKey);
        omd.setUserMetadata(outcomeMetadata);
        cor.setNewObjectMetadata(omd);
        s3Client.copyObject(cor);
    }

}
