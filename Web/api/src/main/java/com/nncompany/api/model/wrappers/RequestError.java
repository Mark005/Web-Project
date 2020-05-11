package com.nncompany.api.model.wrappers;

public class RequestError {

    private Integer code;
    private String message;
    private String description;

    public RequestError(Integer code, String message, String description){
        this.code = code;
        this.message = message;
        this. description = description;
    }




    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
