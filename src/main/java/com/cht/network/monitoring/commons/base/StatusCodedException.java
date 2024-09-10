package com.cht.network.monitoring.commons.base;

/**
 * 包含狀態碼的例外物件。
 */
public class StatusCodedException extends RuntimeException {

    private static final long serialVersionUID = 5375942239036657021L;

    private StatusCode code;

    /**
     * 指定狀態碼，產生例外物件。
     *
     * @param code
     *            狀態碼。
     */
    public StatusCodedException(StatusCode code) {
        super(code.toString());
        this.code = code;
    }

    /**
     * 指定狀態碼及例外發生原因，產生例外物件。
     *
     * @param code
     *            狀態碼。
     * @param cause
     *            例外發生原因。
     */
    public StatusCodedException(StatusCode code, Throwable cause) {
        super(code.toString(), cause);
        this.code = code;
    }


    public StatusCode getCode() {
        return code;
    }
}
