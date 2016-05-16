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
package com.amazonaws.eclipse.simpleworkflow.asynchrony.common;

//import com.amazonaws.services.simpleworkflow.client.asynchrony.CurrentContext;
import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.JsonDataConverter;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activity;
import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.ExponentialRetry;
import com.amazonaws.services.simpleworkflow.flow.annotations.GetState;
import com.amazonaws.services.simpleworkflow.flow.annotations.Signal;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;


public class ProcessorConstants {
    public static final String CLIENT_INTERFACE_SUFFIX = "Client";
    public static final String CLIENT_IMPL_SUFFIX = "ClientImpl";
    public static final String CLIENT_FACTORY_SUFFIX = "ClientFactory";
    public static final String CLIENT_FACTORY_IMPL_SUFFIX = "ClientFactoryImpl";
    
    public static final String CLIENT_EXTERNAL_INTERFACE_SUFFIX = "ClientExternal";
    public static final String CLIENT_EXTERNAL_IMPL_SUFFIX = "ClientExternalImpl";
    public static final String CLIENT_EXTERNAL_FACTORY_SUFFIX = "ClientExternalFactory";
    public static final String CLIENT_EXTERNAL_FACTORY_IMPL_SUFFIX = "ClientExternalFactoryImpl";
    
    public static final String SELF_CLIENT_INTERFACE_SUFFIX = "SelfClient";
    public static final String SELF_CLIENT_IMPL_SUFFIX = "SelfClientImpl";
    
    public static final String WORKFLOW_ANNOTATION_TYPE_CLASS_NAME = Workflow.class.getName();
    public static final String ACTIVITIES_ANNOTATION_TYPE_CLASS_NAME = Activities.class.getName();
    
    public static final String EXPONENTIALRETRY_ANNOTATION = ExponentialRetry.class.getName();
    public static final String EXECUTE_ANNOTATION = Execute.class.getName();
    public static final String ACTIVITY_ANNOTATION = Activity.class.getName();
    public static final String SIGNAL_ANNOTATION = Signal.class.getName();
    public static final String GETSTATE_ANNOTATION = GetState.class.getName();
    
    public static final String JAVA_LANG_OVERRIDE = Override.class.getName();

    public static final Class<? extends DataConverter> DEFAULT_DATACONVERTER = JsonDataConverter.class;
    
}
