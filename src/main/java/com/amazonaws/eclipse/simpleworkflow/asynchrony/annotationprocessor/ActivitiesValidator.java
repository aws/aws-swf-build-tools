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
package com.amazonaws.eclipse.simpleworkflow.asynchrony.annotationprocessor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementScanner6;
import javax.tools.Diagnostic.Kind;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activity;


public class ActivitiesValidator extends ElementScanner6<Boolean, ProcessingEnvironment> {
    private boolean hasErrors = false;
    private String version;
    private String parentVersion;
    
    public boolean isHasErrors() {
        return hasErrors;
    }
    
    @Override
    public Boolean visitType(TypeElement e, ProcessingEnvironment p) {
        if (e.getAnnotation(Activities.class) != null) {
            if (e.getKind().isClass()) {
                reportError(p, "@Activities can only be used on an interface.", e);
            }
            
            if (e.getNestingKind().isNested()) {
                reportError(p, "@Activities not allowed on inner or nested types.", e);
            }
            
            version = ProcessorUtils.getActivitiesVersion(e);
            parentVersion = ProcessorUtils.getParentActivitiesVersion(p, e);
        }

        return super.visitType(e, p);
    }
    
    
    @Override
    public Boolean visitExecutable(ExecutableElement e, ProcessingEnvironment p) {
        TypeMirror returnType = e.getReturnType();
        if (!ProcessorUtils.isVoidType(returnType) && ProcessorUtils.isPromiseType(returnType)) {
            reportError(p, "Activity methods are not allowed to have Promise return type.", e);
        }
        
        for (VariableElement parameter: e.getParameters()) {
            TypeMirror parameterType = parameter.asType();
            if (ProcessorUtils.isPromiseType(parameterType)) {
                reportError(p, "Activity methods are not allowed to have Promise parameter type.", parameter);
            }
        }
        
        if ((version == null || version.isEmpty()) && (parentVersion == null || parentVersion.isEmpty())) {
            Activity activityAnnotation = e.getAnnotation(Activity.class);
            if (activityAnnotation == null || activityAnnotation.name().isEmpty()) {
                reportError(p, "Activity version not specified.", e);
            }
        }
        
        return super.visitExecutable(e, p);
    }
    
    private void reportError(ProcessingEnvironment environment, String message, Element element) {
        hasErrors = true;
        environment.getMessager().printMessage(Kind.ERROR, message, element);
    }

}
