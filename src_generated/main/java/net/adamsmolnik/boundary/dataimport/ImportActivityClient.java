/**
 * This code was generated from {@link net.adamsmolnik.boundary.dataimport.ImportActivity}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package net.adamsmolnik.boundary.dataimport;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.ActivitiesClient;
import com.amazonaws.services.simpleworkflow.flow.ActivitySchedulingOptions;

public interface ImportActivityClient extends ActivitiesClient
{
    Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(net.adamsmolnik.model.dataimport.ImportRequest importRequest);
    Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(net.adamsmolnik.model.dataimport.ImportRequest importRequest, Promise<?>... waitFor);
    Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(net.adamsmolnik.model.dataimport.ImportRequest importRequest, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor);
    Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(Promise<net.adamsmolnik.model.dataimport.ImportRequest> importRequest);
    Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(Promise<net.adamsmolnik.model.dataimport.ImportRequest> importRequest, Promise<?>... waitFor);
    Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(Promise<net.adamsmolnik.model.dataimport.ImportRequest> importRequest, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor);
}