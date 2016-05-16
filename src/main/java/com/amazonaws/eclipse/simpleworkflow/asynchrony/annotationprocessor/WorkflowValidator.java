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

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.GetState;
import com.amazonaws.services.simpleworkflow.flow.annotations.Signal;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;

public class WorkflowValidator extends ElementScanner6<Boolean, ProcessingEnvironment> {
    private boolean hasErrors = false;
    private boolean hasExecute = false;
    private boolean hasGetState = false;
    
    public boolean isHasErrors() {
        return hasErrors;
    }
    
    public boolean isHasExecute() {
        return hasExecute;
    }
    
    public boolean isHasGetState() {
        return hasGetState;
    }
    
    @Override
    public Boolean visitType(TypeElement e, ProcessingEnvironment p) {
        if (e.getAnnotation(Workflow.class) != null) {
            if (e.getKind().isClass()) {
                reportError(p, "@Workflow can only be used on an interface.", e);
            }
            
            if (e.getNestingKind().isNested()) {
                reportError(p, "@Workflow not allowed on inner or nested types.", e);
            }
        }

        return super.visitType(e, p);
    }
    
    @Override
    public Boolean visitExecutable(ExecutableElement e, ProcessingEnvironment p) {
        int annotationCount = 0;
        
        if (e.getAnnotation(Execute.class) != null) {
            if (hasExecute) {
                reportError(p, "Only one method allowed with @Execute annotation.", e);
            } else {
                hasExecute = true;
            }
            annotationCount++;
            TypeMirror returnType = e.getReturnType();
            if (!(ProcessorUtils.isVoidType(returnType) || ProcessorUtils.isPromiseType(returnType))) {
                reportError(p, "Method with @Execute annotations is only allowed to have void or Promise as return types.", e);
            }
        }
        
        if (e.getAnnotation(Signal.class) != null) {
            if (!ProcessorUtils.isVoidType(e.getReturnType())) {
                reportError(p, "Signal method cannot have a return type.", e);
            }
            
            annotationCount++;
        }
        
        if (e.getAnnotation(GetState.class) != null) {
            if (hasGetState) {
                reportError(p, "Only one method allowed with @GetState annotation.", e);
            }
            else {
                hasGetState = true;
            }
            
            TypeMirror returnType = e.getReturnType();
            if (ProcessorUtils.isVoidType(returnType)) {
                reportError(p, "GetState method cannot have void as return type.", e);
            } else if (ProcessorUtils.isPromiseType(returnType)) {
                reportError(p, "GetState method cannot have Promise as return type.", e);
            }
            annotationCount++;
        }
        if (annotationCount > 1) {
            reportError(p, "Annotations @Execute, @Signal and @GetState are exclusive.", e);
        }

        for (VariableElement parameter: e.getParameters()) {
            TypeMirror parameterType = parameter.asType();
            if (ProcessorUtils.isPromiseType(parameterType)) {
                reportError(p, "@Workflow methods are not allowed to have Promise parameter types.", parameter);
            }
        }
        
        return super.visitExecutable(e, p);
    }
    
    private void reportError(ProcessingEnvironment environment, String message, Element element) {
        hasErrors = true;
        environment.getMessager().printMessage(Kind.ERROR, message, element);
    }

}
