package com.online.exam.exception;

import java.util.List;

public class ApiErrorResponseBuilder {
    private static ApiErrorResponseBuilder apiErrorResponseBuilder;
    private ApiErrorResponseBuilder(){

    }
    private String errorId;
    private Integer status;
    private String message;
    private List<String> errors;
    private String path;

    public synchronized static ApiErrorResponseBuilder getInstance(){
        if(apiErrorResponseBuilder==null){
            apiErrorResponseBuilder=new ApiErrorResponseBuilder();
        }
    return apiErrorResponseBuilder;
    }
    public ApiErrorResponseBuilder withErrorId(String errorId){
        apiErrorResponseBuilder.errorId=errorId;
        return apiErrorResponseBuilder;
    }
    public ApiErrorResponseBuilder withStatus(Integer status){
        apiErrorResponseBuilder.status=status;
        return apiErrorResponseBuilder;
    }
    public ApiErrorResponseBuilder withMessage(String message){
        apiErrorResponseBuilder.message=message;
        return apiErrorResponseBuilder;
    }
    public ApiErrorResponseBuilder withErrors(List<String> errors){
        apiErrorResponseBuilder.errors=errors;
        return apiErrorResponseBuilder;
    }
    public ApiErrorResponseBuilder forPath(String path){
        apiErrorResponseBuilder.path=path;
        return apiErrorResponseBuilder;
    }

    public ApiErrorResponse build(){
        ApiErrorResponse apiErrorResponse=new ApiErrorResponse();
        apiErrorResponse.setErrorId(apiErrorResponseBuilder.errorId);
        apiErrorResponse.setErrors(apiErrorResponseBuilder.errors);
        apiErrorResponse.setMessage(apiErrorResponseBuilder.message);
        apiErrorResponse.setPath(apiErrorResponseBuilder.path);
        apiErrorResponse.setStatus(apiErrorResponseBuilder.status);
        return apiErrorResponse;
    }

}
