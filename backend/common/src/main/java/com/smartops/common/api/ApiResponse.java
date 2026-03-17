package com.smartops.common.api;

public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    public ApiResponse() {
    }

    public ApiResponse(Integer code, String message, T data, Long timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMessage(),
                data,
                System.currentTimeMillis()
        );
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                ResultCode.SUCCESS.getCode(),
                message,
                data,
                System.currentTimeMillis()
        );
    }

    public static <T> ApiResponse<T> failed(ResultCode code) {
        return new ApiResponse<>(
                code.getCode(),
                code.getMessage(),
                null,
                System.currentTimeMillis()
        );
    }

    public static <T> ApiResponse<T> failed(Integer code, String message) {
        return new ApiResponse<>(
                code,
                message,
                null,
                System.currentTimeMillis()
        );
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
