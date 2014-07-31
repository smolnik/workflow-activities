package net.adamsmolnik.workflow;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;

public class ClientDataProcessingWorkflowImpl {

    public static void main(String[] args) throws Exception {
        ClientConfiguration config = new ClientConfiguration().withSocketTimeout(70 * 1000);
        AmazonS3Client s3Client = new AmazonS3Client();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        s3Client.putObject("net.adamsmolnik.warsjawa", "myfolder/awsugpl.zip", new File("C:/temp/awsugpl.zip"));
        AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(config);
        service.setEndpoint("https://swf.us-east-1.amazonaws.com");
        String domain = "net.adamsmolnik";
        DataProcessingWorkflowClientExternal client = new DataProcessingWorkflowClientExternalFactoryImpl(service, domain).getClient();
        Set<ActionType> actionTypes = new HashSet<>();
        actionTypes.add(ActionType.IMPORT);
        actionTypes.add(ActionType.DIGEST);
        actionTypes.add(ActionType.EXTRACT);
        client.launch("myfolder/awsugpl.zip", actionTypes);
        TimeUnit.SECONDS.sleep(180);
    }

}
