/**
 * This code was generated from {@link net.adamsmolnik.boundary.digest.DigestActivity}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package net.adamsmolnik.boundary.digest;

import com.amazonaws.services.simpleworkflow.flow.ActivitiesClientBase;
import com.amazonaws.services.simpleworkflow.flow.ActivitySchedulingOptions;
import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericActivityClient;
import com.amazonaws.services.simpleworkflow.model.ActivityType;

public class DigestActivityClientImpl extends ActivitiesClientBase implements DigestActivityClient {

	public DigestActivityClientImpl() {
        this(null, new com.amazonaws.services.simpleworkflow.flow.JsonDataConverter(), null);
    }

    public DigestActivityClientImpl(GenericActivityClient genericClient) {
        this(genericClient, new com.amazonaws.services.simpleworkflow.flow.JsonDataConverter(), null);
    }
    
    public DigestActivityClientImpl(GenericActivityClient genericClient, 
            DataConverter dataConverter, ActivitySchedulingOptions schedulingOptions) {
            
        super(genericClient, dataConverter, schedulingOptions);
    }
    
    @Override
    public final Promise<net.adamsmolnik.model.digest.DigestResponse> digest(net.adamsmolnik.model.digest.DigestRequest digestRequest) {
        return digestImpl(Promise.asPromise(digestRequest), (ActivitySchedulingOptions)null);
    }

    @Override
    public final Promise<net.adamsmolnik.model.digest.DigestResponse> digest(net.adamsmolnik.model.digest.DigestRequest digestRequest, Promise<?>... waitFor) {
        return digestImpl(Promise.asPromise(digestRequest), (ActivitySchedulingOptions)null, waitFor);
    }

    @Override
    public final Promise<net.adamsmolnik.model.digest.DigestResponse> digest(net.adamsmolnik.model.digest.DigestRequest digestRequest, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
        return digestImpl(Promise.asPromise(digestRequest), optionsOverride, waitFor);
    }

    @Override
    public final Promise<net.adamsmolnik.model.digest.DigestResponse> digest(Promise<net.adamsmolnik.model.digest.DigestRequest> digestRequest) {
        return digestImpl(digestRequest, (ActivitySchedulingOptions)null);
    }

    @Override
    public final Promise<net.adamsmolnik.model.digest.DigestResponse> digest(Promise<net.adamsmolnik.model.digest.DigestRequest> digestRequest, Promise<?>... waitFor) {
        return digestImpl(digestRequest, (ActivitySchedulingOptions)null, waitFor);
    }

    @Override
    public final Promise<net.adamsmolnik.model.digest.DigestResponse> digest(Promise<net.adamsmolnik.model.digest.DigestRequest> digestRequest, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
        return digestImpl(digestRequest, optionsOverride, waitFor);
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Promise<net.adamsmolnik.model.digest.DigestResponse> digestImpl(final Promise<net.adamsmolnik.model.digest.DigestRequest> digestRequest, final ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {

        ActivityType activityType = new ActivityType();
		activityType.setName("DigestActivity.digest");
		activityType.setVersion("1.0");

        Promise[] _input_ = new Promise[1];
        _input_[0] = digestRequest;

        return (Promise)scheduleActivity(activityType, _input_, optionsOverride, net.adamsmolnik.model.digest.DigestResponse.class, waitFor);
    }

}