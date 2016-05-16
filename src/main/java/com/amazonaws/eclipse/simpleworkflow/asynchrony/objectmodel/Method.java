/*
 * Copyright 2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class Method {
    private String methodName;
    private String methodReturnType;
    private String methodReturnTypeNoGenerics;
    private String methodReturnTypeUnboxed;
    private List<MethodParameter> methodParameters;
    private String annotationsToCopy;
    private List<String> thrownExceptions;
    private boolean hasGenericReturnType;
    private boolean primitiveReturnType;
    
    public Method() {
    }

    public String getMethodName() {
        return methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public Method withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }
    
    public String getMethodReturnType() {
        return methodReturnType;
    }
    
    public void setMethodReturnType(String methodReturnType) {
        this.methodReturnType = methodReturnType;
    }

    public Method withMethodReturnType(String methodReturnType) {
        this.methodReturnType = methodReturnType;
        return this;
    }
    
    public String getMethodReturnTypeNoGenerics() {
        return methodReturnTypeNoGenerics;
    }

    public void setMethodReturnTypeNoGenerics(String methodReturnTypeNoGenerics) {
        this.methodReturnTypeNoGenerics = methodReturnTypeNoGenerics;
    }
    
    public String getMethodReturnTypeUnboxed() {
        return methodReturnTypeUnboxed;
    }
    
    public void setMethodReturnTypeUnboxed(String methodReturnTypeUnboxed) {
        this.methodReturnTypeUnboxed = methodReturnTypeUnboxed;
    }

    public Method withMethodReturnTypeUnboxed(String methodReturnTypeUnboxed) {
        this.methodReturnTypeUnboxed = methodReturnTypeUnboxed;
        return this;
    }

    public List<MethodParameter> getMethodParameters() {
        if (methodParameters == null) {
            methodParameters = new ArrayList<MethodParameter>();
        }
        
        return methodParameters;
    }
    
    public void setMethodParameters(List<MethodParameter> methodParameters) {
        List<MethodParameter> methodParametersCopy = new ArrayList<MethodParameter>();
        if (methodParameters != null) {
            methodParametersCopy.addAll(methodParameters);
        }
        
        this.methodParameters = methodParametersCopy;
    }
    
    public Method withMethodParameters(MethodParameter... methodParameters) {
        for (MethodParameter methodParameter : methodParameters) {
            getMethodParameters().add(methodParameter);
        }
        return this;
    }
    
    public Method withMethodParameters(Collection<MethodParameter> methodParameters) {
        List<MethodParameter> methodParametersCopy = new ArrayList<MethodParameter>();
        if (methodParameters != null) {
            methodParametersCopy.addAll(methodParameters);
        }
        
        this.methodParameters = methodParametersCopy;
        return this;
    }
    
    public String getAnnotationsToCopy() {
        return annotationsToCopy;
    }

    public void setAnnotationsToCopy(String annotationsToCopy) {
        this.annotationsToCopy = annotationsToCopy;
    }

    
    public List<String> getThrownExceptions() {
        if (thrownExceptions == null) {
            thrownExceptions = new ArrayList<String>();
        }
        
        return thrownExceptions;
    }

    
    public void setThrownExceptions(List<String> thrownExceptions) {
        List<String> thrownExceptionsCopy = new ArrayList<String>();
        if (thrownExceptions != null) {
            thrownExceptionsCopy.addAll(thrownExceptions);
        }
        
        this.thrownExceptions = thrownExceptionsCopy;
    }
    
    public Method withThrownExceptions(String... thrownExceptions) {
        for (String thrownException : thrownExceptions) {
            getThrownExceptions().add(thrownException);
        }
        return this;
    }
    
    public Method withThrownExceptions(Collection<String> thrownExceptions) {
        List<String> thrownExceptionsCopy = new ArrayList<String>();
        if (thrownExceptions != null) {
            thrownExceptionsCopy.addAll(thrownExceptions);
        }
        
        this.thrownExceptions = thrownExceptionsCopy;
        return this;
    }
    
    public boolean isHasGenericReturnType() {
        return hasGenericReturnType;
    }

    public void setHasGenericReturnType(boolean hasGenericReturnType) {
        this.hasGenericReturnType = hasGenericReturnType;
    }
    
    public Method withHasGenericReturnType(boolean hasGenericReturnType) {
        this.hasGenericReturnType = hasGenericReturnType;
        return this;
    }
    
    public boolean isPrimitiveReturnType() {
        return primitiveReturnType;
    }

    public void setPrimitiveReturnType(boolean primitiveReturnType) {
        this.primitiveReturnType = primitiveReturnType;
    }
    
    public Method withPrimitiveReturnType(boolean primitiveReturnType) {
        this.primitiveReturnType = primitiveReturnType;
        return this;
    }

}
