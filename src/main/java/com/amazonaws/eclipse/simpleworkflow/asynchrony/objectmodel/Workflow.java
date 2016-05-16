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

public class Workflow extends TypeDefinition {
    private ExecuteMethod executeMethod;
    private GetStateMethod getStateMethod;
    private List<SignalMethod> signals;
    
    public Workflow(String prefix, String version, String dataConverter, String interfaceName, String qualifiedName) {
        super(prefix, version, dataConverter, interfaceName, qualifiedName);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<? extends Workflow> getSuperTypes() {
        return (List<? extends Workflow>)super.getSuperTypes();
    }
    
    public ExecuteMethod getExecuteMethod() {
        return executeMethod;
    }

    public void setExecuteMethod(ExecuteMethod executeMethod) {
        this.executeMethod = executeMethod;
    }
    
    public Workflow withExecuteMethod(ExecuteMethod executeMethod) {
        this.executeMethod = executeMethod;
        return this;
    }
    
    public GetStateMethod getGetStateMethod() {
        return getStateMethod;
    }

    public void setGetStateMethod(GetStateMethod getStateMethod) {
        this.getStateMethod = getStateMethod;
    }
    
    public Workflow withGetStateMethod(GetStateMethod getStateMethod) {
        this.getStateMethod = getStateMethod;
        return this;
    }

    public List<SignalMethod> getSignals() {
        if (signals == null) {
            signals = new ArrayList<SignalMethod>();
        }
        
        return signals;
    }
    
    public void setSignals(List<SignalMethod> signals) {
        List<SignalMethod> signalsCopy = new ArrayList<SignalMethod>();
        if (signals != null) {
            signalsCopy.addAll(signals);
        }
        
        this.signals = signalsCopy;
    }
    
    public Workflow withSignals(SignalMethod... signals) {
        for (SignalMethod signal : signals) {
            getSignals().add(signal);
        }
        return this;
    }
    
    public Workflow withSignals(Collection<SignalMethod> signals) {
        List<SignalMethod> signalsCopy = new ArrayList<SignalMethod>();
        if (signals != null) {
            signalsCopy.addAll(signals);
        }
        
        this.signals = signalsCopy;
        return this;
    }
    
}
