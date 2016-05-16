<#include "header.ftl">
<#import "common.ftl" as lib>
<#macro generateActivitiesMethodImpl activities>
<#list activities.activities as activityMethod>
<#assign activityName = activityMethod.activityName>
<#assign activityVersion = activityMethod.activityVersion>
<#assign parameterCount = activityMethod.methodParameters?size>
<#assign hasParameters = (parameterCount > 0)>
<#assign activityImplMethodName = "${activityMethod.methodName}Impl">
<#if hasParameters>
    @Override
    public final Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParameters activityMethod/>) {
        return ${activityImplMethodName}(<@lib.printInputAsPromise activityMethod/>(ActivitySchedulingOptions)null);
    }

    @Override
    public final Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParameters activityMethod/>, Promise<?>... waitFor) {
        return ${activityImplMethodName}(<@lib.printInputAsPromise activityMethod/>(ActivitySchedulingOptions)null, waitFor);
    }

    @Override
    public final Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParameters activityMethod/>, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
        return ${activityImplMethodName}(<@lib.printInputAsPromise activityMethod/>optionsOverride, waitFor);
    }

    @Override
    public final Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParametersAsPromise activityMethod/>) {
        return ${activityImplMethodName}(<@lib.printInput activityMethod/>(ActivitySchedulingOptions)null);
    }

    @Override
    public final Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParametersAsPromise activityMethod/>, Promise<?>... waitFor) {
        return ${activityImplMethodName}(<@lib.printInput activityMethod/>(ActivitySchedulingOptions)null, waitFor);
    }

    @Override
    public final Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParametersAsPromise activityMethod/>, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
        return ${activityImplMethodName}(<@lib.printInput activityMethod/>optionsOverride, waitFor);
    }
    
    ${activityMethod.annotationsToCopy}
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Promise<${activityMethod.methodReturnType}> ${activityImplMethodName}(<@lib.printParametersAsPromiseFinal activityMethod/>, final ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
<#else>
    @Override
    public final Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}() {
        return ${activityImplMethodName}((ActivitySchedulingOptions)null);
    }

    @Override
    public final Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(Promise<?>... waitFor) {
        return ${activityImplMethodName}((ActivitySchedulingOptions)null, waitFor);
    }

    @Override
    public final Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
        return ${activityImplMethodName}(optionsOverride, waitFor);
    }
    
    ${activityMethod.annotationsToCopy}
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Promise<${activityMethod.methodReturnType}> ${activityImplMethodName}(final ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
</#if>

        ActivityType _activityType = new ActivityType();
		_activityType.setName("${activityName}");
		_activityType.setVersion("${activityVersion}");

        Promise[] _input_ = new Promise[${parameterCount}];
<#list activityMethod.methodParameters as param>
        _input_[${param_index}] = ${param.parameterName};
</#list>

        return (Promise)scheduleActivity(_activityType, _input_, optionsOverride, ${activityMethod.methodReturnTypeNoGenerics}.class, waitFor);
    }

</#list>
</#macro>
package ${packageName};

import com.amazonaws.services.simpleworkflow.flow.ActivitiesClientBase;
import com.amazonaws.services.simpleworkflow.flow.ActivitySchedulingOptions;
import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericActivityClient;
import com.amazonaws.services.simpleworkflow.model.ActivityType;

public class ${clientImplName} extends ActivitiesClientBase implements ${clientInterfaceName} {

	public ${clientImplName}() {
        this(null, new ${activities.dataConverter}(), null);
    }

    public ${clientImplName}(GenericActivityClient genericClient) {
        this(genericClient, new ${activities.dataConverter}(), null);
    }
    
    public ${clientImplName}(GenericActivityClient genericClient, 
            DataConverter dataConverter, ActivitySchedulingOptions schedulingOptions) {
            
        super(genericClient, dataConverter, schedulingOptions);
    }
    
<@generateActivitiesMethodImpl activities/>
<#list activities.superTypes as superActivities>
    <@generateActivitiesMethodImpl superActivities/>
</#list>
}