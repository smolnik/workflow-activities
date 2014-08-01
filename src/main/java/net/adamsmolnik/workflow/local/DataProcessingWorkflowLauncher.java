package net.adamsmolnik.workflow.local;

import java.util.concurrent.TimeUnit;
import net.adamsmolnik.workflow.DataProcessingWorkflowImpl;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;

/**
 * @author ASmolnik
 *
 */
public class DataProcessingWorkflowLauncher {

    public static void main(String[] args) throws Exception {
        ClientConfiguration config = new ClientConfiguration().withSocketTimeout(70 * 1000);
        AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(new SystemCredentials(), config);
        service.setEndpoint("https://swf.us-east-1.amazonaws.com");
        String domain = "net.adamsmolnik";
        String taskListToPoll = "dataProcessingWorkflow";
        WorkflowWorker wfw = new WorkflowWorker(service, domain, taskListToPoll);
        wfw.addWorkflowImplementationType(DataProcessingWorkflowImpl.class);
        wfw.start();
        TimeUnit.SECONDS.sleep(300);
    }

}
