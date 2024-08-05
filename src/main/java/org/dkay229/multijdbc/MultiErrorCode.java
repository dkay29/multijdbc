package org.dkay229.multijdbc;

public enum MultiErrorCode {
    COULD_NOT_CONNECT(1001, "Could not connect to specified server"),
    MALFORMED_JDBC_URL(1001, "Bad JDBC URL"),

    LAST_ERROR_CODE(9999,"LAST CODE");

    private final int code;
    private final String message;

    MultiErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("ErrorCode{code=%d, message='%s'}", code, message);
    }
}
