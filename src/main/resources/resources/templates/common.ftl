<#macro printParameters method>
<#list method.methodParameters as param>
${param.parameterTypeUnboxed} ${param.parameterName}<#if param_has_next>, </#if></#list></#macro>

<#macro printParametersAsPromise method>
<#list method.methodParameters as param>
Promise<${param.parameterType}> ${param.parameterName}<#if param_has_next>, </#if></#list></#macro>

<#macro printParametersAsPromiseFinal method>
<#list method.methodParameters as param>
final Promise<${param.parameterType}> ${param.parameterName}<#if param_has_next>, </#if></#list></#macro>

<#macro printInput method>
<#list method.methodParameters as param>
${param.parameterName}, </#list></#macro>

<#macro printInputAsPromise method>
<#list method.methodParameters as param>
Promise.asPromise(${param.parameterName}), </#list></#macro>

<#macro printThrows method>
<#if (method.thrownExceptions?size > 0)>throws </#if><#list method.thrownExceptions as exception>${exception}<#if exception_has_next>, </#if></#list></#macro>

<#macro printThrowsImpl method returnType>
        try {
            _state_ = dynamicWorkflowClient.getWorkflowExecutionState(${returnType}.class);
        } catch (Throwable _failure_) {
<#if (method.thrownExceptions?size > 0)>
<#list method.thrownExceptions as exception>
            if (_failure_ instanceof ${exception}) {
                throw (${exception}) _failure_;
            }
<#if exception_has_next>
            else 
<#else>
            else {
</#if>
</#list>
                if (_failure_ instanceof RuntimeException) {
                    throw (RuntimeException) _failure_;
                } else if (_failure_ instanceof Error) {
                    throw (Error) _failure_;
                } else {
                    throw new RuntimeException("Unknown exception.", _failure_);
                }
            }
<#else>
            if (_failure_ instanceof RuntimeException) {
                throw (RuntimeException) _failure_;
            } else if (_failure_ instanceof Error) {
                throw (Error) _failure_;
            } else {
                throw new RuntimeException("Unknown exception.", _failure_);
            }
</#if>
        }
</#macro>