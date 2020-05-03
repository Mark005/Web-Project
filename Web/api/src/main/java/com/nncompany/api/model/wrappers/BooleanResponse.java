package com.nncompany.api.model.wrappers;

public class BooleanResponse {

    private Boolean response;

    public BooleanResponse(){}

    public BooleanResponse(Boolean response){
        this.response = response;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }
}
