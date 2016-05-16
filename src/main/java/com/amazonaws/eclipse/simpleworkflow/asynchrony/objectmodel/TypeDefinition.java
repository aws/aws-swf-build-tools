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
import java.util.HashMap;
import java.util.List;


public class TypeDefinition {
    private final String prefix;
    private final String version;
    private final String dataConverter;
    private final String interfaceName;
    private final String qualifiedName;
    private final String packageName;
    
    private List<? extends TypeDefinition> superTypes;
    
    public TypeDefinition(String prefix, String version, String dataConverter, String interfaceName, 
            String qualifiedName) {
        this.prefix = prefix;
        this.version = version;
        this.dataConverter = dataConverter;
        this.interfaceName = interfaceName;
        this.qualifiedName = qualifiedName;
        this.packageName = qualifiedName.substring(0, qualifiedName.length() - interfaceName.length() - 1);
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getDataConverter() {
        return dataConverter;
    }

    public String getInterfaceName() {
        return interfaceName;
    }
    
    public String getQualifiedName() {
        return qualifiedName;
    }
    
    public String getPackageName() {
        return packageName;
    }

    public List<? extends TypeDefinition> getSuperTypes() {
        if (superTypes == null) {
            superTypes = new ArrayList<TypeDefinition>();
        }
        
        return superTypes;
    }
    
    public void setSuperTypes(List<? extends TypeDefinition> superTypes) {
        List<TypeDefinition> superTypesCopy = new ArrayList<TypeDefinition>();
        if (superTypes != null) {
            superTypesCopy.addAll(superTypes);
        }
        
        this.superTypes = superTypesCopy;
    }
    
    public Collection<? extends TypeDefinition> getAllSuperTypes() {
        HashMap<String, TypeDefinition> allSuperTypesMap = new HashMap<String, TypeDefinition>();
        
        for (TypeDefinition type: getSuperTypes()) {
            for (TypeDefinition superType: type.getAllSuperTypes()) {
                if (!allSuperTypesMap.containsKey(superType.getQualifiedName())) {
                    allSuperTypesMap.put(superType.getQualifiedName(), superType);
                }
            }
            
            allSuperTypesMap.put(type.getQualifiedName(), type);
        }
        
        return allSuperTypesMap.values();
    }
}
