package claygminx.entity;

import claygminx.consts.ResultCode;

/**
 * RESTful返回值
 */
public class Result {

    private final static Result DEFAULT_SUCCESS = new Result(ResultCode.SUCCESS, null, null);

    private ResultCode code;
    private String message;
    private Object data;

    public Result(ResultCode code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code == null? null : code.ordinal();
    }

    public void setCode(ResultCode code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Result success() {
        return DEFAULT_SUCCESS;
    }

    public static Result failure(String message) {
        return new Result(ResultCode.FAILURE, message, null);
    }
}
