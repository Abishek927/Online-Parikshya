package com.online.exam.exception;

import lombok.Data;

@Data

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String resourceName;
    private String resourceField;
    private String fieldValue;
    public ResourceNotFoundException(String resourceName, String resourceField, String fieldValue) {
        super(String.format("%s resource not found with %s:%s",resourceName,resourceField,fieldValue));
        this.resourceName = resourceName;
        this.resourceField = resourceField;
        this.fieldValue = fieldValue;
    }



}
