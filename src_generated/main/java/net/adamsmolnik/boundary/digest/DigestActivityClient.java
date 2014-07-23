/**
 * This code was generated from {@link net.adamsmolnik.boundary.digest.DigestActivity}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package net.adamsmolnik.boundary.digest;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.ActivitiesClient;
import com.amazonaws.services.simpleworkflow.flow.ActivitySchedulingOptions;

public interface DigestActivityClient extends ActivitiesClient
{
    Promise<net.adamsmolnik.model.digest.DigestResponse> digest(net.adamsmolnik.model.digest.DigestRequest digestRequest);
    Promise<net.adamsmolnik.model.digest.DigestResponse> digest(net.adamsmolnik.model.digest.DigestRequest digestRequest, Promise<?>... waitFor);
    Promise<net.adamsmolnik.model.digest.DigestResponse> digest(net.adamsmolnik.model.digest.DigestRequest digestRequest, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor);
    Promise<net.adamsmolnik.model.digest.DigestResponse> digest(Promise<net.adamsmolnik.model.digest.DigestRequest> digestRequest);
    Promise<net.adamsmolnik.model.digest.DigestResponse> digest(Promise<net.adamsmolnik.model.digest.DigestRequest> digestRequest, Promise<?>... waitFor);
    Promise<net.adamsmolnik.model.digest.DigestResponse> digest(Promise<net.adamsmolnik.model.digest.DigestRequest> digestRequest, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor);
}