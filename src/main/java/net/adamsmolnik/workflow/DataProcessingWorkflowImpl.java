package net.adamsmolnik.workflow;

import java.util.ArrayList;
import java.util.List;
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
import net.adamsmolnik.model.notification.NotificationRequest;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.annotations.Wait;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

/**
 * @author ASmolnik *
 */
public class DataProcessingWorkflowImpl implements DataProcessingWorkflow {

    private static final String WF_INFO_TEMPLATE = "DataProcessingWorkflow has been %s for source objectKey %s";

    private final ImportActivityClient imAC = new ImportActivityClientImpl();

    private final NotificationActivityClient nAC = new NotificationActivityClientImpl();

    private final DetectionActivityClient detectionAC = new DetectionActivityClientImpl();

    private final DigestActivityClient digestAC = new DigestActivityClientImpl();

    private final ExtractionActivityClient extractionAC = new ExtractionActivityClientImpl();

    @Override
    public void launch(String srcObjectKey, Set<ActionType> actions) {
        nAC.publish(new NotificationRequest(String.format(WF_INFO_TEMPLATE, "launched", srcObjectKey)));
        Promise<ImportResponse> importResponse = imAC.doImport(new ImportRequest(srcObjectKey));
        Promise<OutcomeReport> outcomeReport = takeActions(actions, importResponse);
        publishCompleted(srcObjectKey, outcomeReport);
    }

    @Asynchronous
    private void publishCompleted(String srcObjectKey, Promise<OutcomeReport> outcomeReport) {
        nAC.publish(new NotificationRequest(String.format(WF_INFO_TEMPLATE, "completed", srcObjectKey + " with the received outcome report: \n"
                + outcomeReport.get())));
    }

    @Asynchronous
    private Promise<OutcomeReport> takeActions(Set<ActionType> actionTypes, Promise<ImportResponse> importResponse) {
        OutcomeReport outcomeReport = new OutcomeReport();
        String objectKey = importResponse.get().importedObjectKey;
        outcomeReport.add("Data has been imported into internal folder " + objectKey);
        List<Promise<?>> actionsDone = new ArrayList<>();
        boolean doExtraction = actionTypes.contains(ActionType.EXTRACT);
        boolean doDetection = actionTypes.contains(ActionType.DETECT);
        if (doDetection || doExtraction) {
            final Promise<DetectionResponse> detectionResponse = detectionAC.detect(new DetectionRequest(objectKey));
            ActivityOutcome<String> ao = new ActivityOutcome<String>() {

                @Override
                public String getOutcome() {
                    return detectionResponse.get().subType;
                }
            };
            actionsDone.add(detectionResponse);
            addToReport(outcomeReport, "Data has been recognized as ", ao, detectionResponse);
            if (doExtraction) {
                Promise<?> extractionResponse = extract(outcomeReport, objectKey, doExtraction, detectionResponse);
                actionsDone.add(extractionResponse);
            }
        }
        if (actionTypes.contains(ActionType.DIGEST)) {
            final Promise<DigestResponse> digestResponse = digestAC.digest(new DigestRequest("SHA-256", objectKey));
            ActivityOutcome<String> ao = new ActivityOutcome<String>() {

                @Override
                public String getOutcome() {
                    return digestResponse.get().digest;
                }
            };
            actionsDone.add(digestResponse);
            addToReport(outcomeReport, "Digest calculated is ", ao, digestResponse);
        }

        return waitFor(actionsDone, outcomeReport);
    }

    @Asynchronous
    private Promise<OutcomeReport> waitFor(@Wait List<Promise<?>> actionsDone, OutcomeReport outcomeReport) {
        return Promise.asPromise(outcomeReport);
    }

    @Asynchronous
    private void addToReport(OutcomeReport outcomeReport, String message, ActivityOutcome<?> ao, Promise<?> waitFor) {
        outcomeReport.add(message + ao.getOutcome());
    }

    @Asynchronous
    private Promise<ExtractionResponse> extract(OutcomeReport outcomeReport, String objectKey, boolean doExtraction,
            Promise<DetectionResponse> detectionResponse) {
        String subType = detectionResponse.get().subType;
        final Promise<ExtractionResponse> extractionResponse = extractionAC.extract(new ExtractionRequest(objectKey, subType));
        ActivityOutcome<String> ao = new ActivityOutcome<String>() {

            @Override
            public String getOutcome() {
                return extractionResponse.get().objectKeys.toString();
            }
        };
        addToReport(outcomeReport, "Data has been extracted into ", ao, extractionResponse);
        return extractionResponse;
    }

}
