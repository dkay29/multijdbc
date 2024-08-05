package org.dkay229.multijdbc;

public class MultiException extends RuntimeException {
    private final MultiErrorCode errorCode;
    private String message ;
    public MultiException(MultiErrorCode errorCode) {
        this(errorCode,null);
    }
    public MultiException(MultiErrorCode errorCode, String message) {
        super(message==null?errorCode.getMessage():errorCode.getMessage()+" : "+message);
        this.errorCode = errorCode;
        this.message =message==null?errorCode.getMessage():errorCode.getMessage()+" : "+message;
    }

    public MultiErrorCode getErrorCode() {
        return errorCode;
    }

    public String getReason() {
        return String.format("MULS-%07d %s ",errorCode,message);
    }

    @Override
    public String toString() {
        return "MultiException{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                '}';
    }
}
