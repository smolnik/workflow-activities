package net.adamsmolnik.workflow;

import java.util.Set;
import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.GetState;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;

/**
 * @author ASmolnik
 *
 */
@Workflow
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds = 3600)
public interface DataProcessingWorkflow {

    @Execute(version = "1.0.5")
    public void launch(String srcObjectKey, Set<ActionType> operations);

    @GetState
    public String getState();
}
