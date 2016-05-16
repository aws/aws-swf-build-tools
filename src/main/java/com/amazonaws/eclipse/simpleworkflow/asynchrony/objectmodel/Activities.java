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


public class Activities extends TypeDefinition {
    private List<ActivityMethod> activities;
    
    public Activities(String prefix, String version, String dataConverter, String interfaceName, String qualifiedName) {
        super(prefix, version, dataConverter, interfaceName, qualifiedName);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<? extends Activities> getSuperTypes() {
        return (List<? extends Activities>)super.getSuperTypes();
    }
    
    public List<ActivityMethod> getActivities() {
        if (activities == null) {
            activities = new ArrayList<ActivityMethod>();
        }
        
        return activities;
    }
    
    public void setActivities(List<ActivityMethod> activities) {
        List<ActivityMethod> activitiesCopy = new ArrayList<ActivityMethod>();
        if (activities != null) {
            activitiesCopy.addAll(activities);
        }
        
        this.activities = activitiesCopy;
    }
    
    public Activities withActivities(ActivityMethod... activities) {
        for (ActivityMethod activity : activities) {
            getActivities().add(activity);
        }
        return this;
    }
    
    public Activities withActivities(Collection<ActivityMethod> activities) {
        List<ActivityMethod> activitiesCopy = new ArrayList<ActivityMethod>();
        if (activities != null) {
            activitiesCopy.addAll(activities);
        }
        
        this.activities = activitiesCopy;
        return this;
    }
}
