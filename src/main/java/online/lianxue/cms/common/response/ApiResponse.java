package online.lianxue.cms.common.response;

import lombok.Builder;
import lombok.Data;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

/**
 * HTTP结果封装
 *
 * @author zhuyl
 */
@Data
@Builder
public class ApiResponse<T> {

    private boolean success;
    private int code;
    private String msg;
    private T data;

    public static ApiResponse ok() {
        return ApiResponse.builder()
                .success(true)
                .code(OK.value())
                .msg(OK.getReasonPhrase())
                .build();
    }

    public static ApiResponse ok(String msg) {
        return ApiResponse.builder()
                .success(true)
                .code(OK.value())
                .msg(msg)
                .build();
    }

    public static <T> ApiResponse ok(T data) {
        return ApiResponse.builder()
                .success(true)
                .code(OK.value())
                .msg(OK.getReasonPhrase())
                .data(data)
                .build();
    }

    public static ApiResponse error() {
        return error(INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    public static ApiResponse error(String msg) {
        return error(INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static ApiResponse error(int code, String msg) {
        return ApiResponse.builder()
                .success(false)
                .code(code)
                .msg(msg)
                .build();

    }


}
