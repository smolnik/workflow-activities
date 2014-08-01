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
        SystemCredentials sc = new SystemCredentials();
        AmazonS3Client s3Client = new AmazonS3Client(sc);
        s3Client.putObject("net.adamsmolnik.warsjawa", "myfolder/awsugpl.zip", new File("C:/temp/awsugpl.zip"));

        ClientConfiguration config = new ClientConfiguration().withSocketTimeout(70 * 1000);
        AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(sc, config);
        service.setEndpoint("https://swf.us-east-1.amazonaws.com");
        String domain = "net.adamsmolnik";
        DataProcessingWorkflowClientExternal client = new DataProcessingWorkflowClientExternalFactoryImpl(service, domain).getClient();
        Set<ActionType> actionTypes = new HashSet<>();
        actionTypes.add(ActionType.IMPORT);
        actionTypes.add(ActionType.DIGEST);
        actionTypes.add(ActionType.EXTRACT);
        client.launch("myfolder/awsugpl.zip", actionTypes);
        TimeUnit.SECONDS.sleep(300);
    }

}