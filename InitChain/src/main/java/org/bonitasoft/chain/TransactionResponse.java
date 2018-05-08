package org.bonitasoft.chain;

public class TransactionResponse {

    private String id;
    private Boolean error;
    private String errorCode;
    private String errorMessage;
    
    
    public TransactionResponse(String id) {
        this.id = id;
        this.error = false;
    }
    
    public TransactionResponse(String errorCode, String errorMessage) {
        this.error = true;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getError() {
        return error;
    }

    protected void setError(Boolean error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
}
