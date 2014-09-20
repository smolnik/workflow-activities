package net.adamsmolnik.workflow.local;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import net.adamsmolnik.workflow.ActionType;
import net.adamsmolnik.workflow.DataProcessingWorkflowClientExternal;
import net.adamsmolnik.workflow.DataProcessingWorkflowClientExternalFactoryImpl;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;

/**
 * @author ASmolnik
 *
 */
public class WorkflowClient {

    public static void main(String[] args) throws Exception {
        AmazonS3Client s3Client = new AmazonS3Client();
        String bucketName = "student100";
        String srcObjectKey = "external/awsugpl.zip";
        s3Client.putObject(bucketName, srcObjectKey, new File("C:/temp/awsugpl.zip"));

        ClientConfiguration config = new ClientConfiguration().withSocketTimeout(70 * 1000);
        AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(config);
        service.setEndpoint("https://swf.us-east-1.amazonaws.com");
        String domain = "student100";
        DataProcessingWorkflowClientExternal client = new DataProcessingWorkflowClientExternalFactoryImpl(service, domain).getClient();
        Set<ActionType> actionTypes = new HashSet<>();
        actionTypes.add(ActionType.IMPORT);
        actionTypes.add(ActionType.DIGEST);
        actionTypes.add(ActionType.EXTRACT);
        client.launch(bucketName, srcObjectKey, actionTypes);
        TimeUnit.SECONDS.sleep(20);
        System.out.println("state = " + client.getState());
        TimeUnit.SECONDS.sleep(300);
    }

}