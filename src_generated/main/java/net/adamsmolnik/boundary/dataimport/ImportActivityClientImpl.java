/**
 * This code was generated from {@link net.adamsmolnik.boundary.dataimport.ImportActivity}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package net.adamsmolnik.boundary.dataimport;

import com.amazonaws.services.simpleworkflow.flow.ActivitiesClientBase;
import com.amazonaws.services.simpleworkflow.flow.ActivitySchedulingOptions;
import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericActivityClient;
import com.amazonaws.services.simpleworkflow.model.ActivityType;

public class ImportActivityClientImpl extends ActivitiesClientBase implements ImportActivityClient {

	public ImportActivityClientImpl() {
        this(null, new com.amazonaws.services.simpleworkflow.flow.JsonDataConverter(), null);
    }

    public ImportActivityClientImpl(GenericActivityClient genericClient) {
        this(genericClient, new com.amazonaws.services.simpleworkflow.flow.JsonDataConverter(), null);
    }
    
    public ImportActivityClientImpl(GenericActivityClient genericClient, 
            DataConverter dataConverter, ActivitySchedulingOptions schedulingOptions) {
            
        super(genericClient, dataConverter, schedulingOptions);
    }
    
    @Override
    public final Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(net.adamsmolnik.model.dataimport.ImportRequest importRequest) {
        return doImportImpl(Promise.asPromise(importRequest), (ActivitySchedulingOptions)null);
    }

    @Override
    public final Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(net.adamsmolnik.model.dataimport.ImportRequest importRequest, Promise<?>... waitFor) {
        return doImportImpl(Promise.asPromise(importRequest), (ActivitySchedulingOptions)null, waitFor);
    }

    @Override
    public final Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(net.adamsmolnik.model.dataimport.ImportRequest importRequest, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
        return doImportImpl(Promise.asPromise(importRequest), optionsOverride, waitFor);
    }

    @Override
    public final Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(Promise<net.adamsmolnik.model.dataimport.ImportRequest> importRequest) {
        return doImportImpl(importRequest, (ActivitySchedulingOptions)null);
    }

    @Override
    public final Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(Promise<net.adamsmolnik.model.dataimport.ImportRequest> importRequest, Promise<?>... waitFor) {
        return doImportImpl(importRequest, (ActivitySchedulingOptions)null, waitFor);
    }

    @Override
    public final Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImport(Promise<net.adamsmolnik.model.dataimport.ImportRequest> importRequest, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
        return doImportImpl(importRequest, optionsOverride, waitFor);
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Promise<net.adamsmolnik.model.dataimport.ImportResponse> doImportImpl(final Promise<net.adamsmolnik.model.dataimport.ImportRequest> importRequest, final ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {

        ActivityType activityType = new ActivityType();
		activityType.setName("ImportActivity.doImport");
		activityType.setVersion("1.0");

        Promise[] _input_ = new Promise[1];
        _input_[0] = importRequest;

        return (Promise)scheduleActivity(activityType, _input_, optionsOverride, net.adamsmolnik.model.dataimport.ImportResponse.class, waitFor);
    }

}