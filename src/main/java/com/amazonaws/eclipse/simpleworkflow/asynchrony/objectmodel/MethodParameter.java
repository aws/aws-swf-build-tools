/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel;


public class MethodParameter {
    private String parameterName;
    private String parameterType;
    private String parameterTypeUnboxed;
    
    public MethodParameter() {
    }
    
    public MethodParameter(String parameterName, String parameterType) {
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }
    
    public String getParameterName() {
        return parameterName;
    }
    
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
    
    public MethodParameter withParameterName(String parameterName) {
        this.parameterName = parameterName;
        return this;
    }
    
    public String getParameterType() {
        return parameterType;
    }
    
    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
    
    public MethodParameter withParameterType(String parameterType) {
        this.parameterType = parameterType;
        return this;
    }
    
    public String getParameterTypeUnboxed() {
        return parameterTypeUnboxed;
    }

    public void setParameterTypeUnboxed(String parameterTypeUnboxed) {
        this.parameterTypeUnboxed = parameterTypeUnboxed;
    }
    
    public MethodParameter withParameterTypeUnboxed(String parameterTypeUnboxed) {
        this.parameterTypeUnboxed = parameterTypeUnboxed;
        return this;
    }
}
