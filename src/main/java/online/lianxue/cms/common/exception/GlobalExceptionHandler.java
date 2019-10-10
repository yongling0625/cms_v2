package online.lianxue.cms.common.exception;

import online.lianxue.cms.common.response.ApiResponse;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕捉UnauthorizedException
    @ExceptionHandler(UnauthorizedException.class)
    public ApiResponse handle401(UnauthorizedException e) {
        return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    // 捕捉AuthenticationException
    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse handle401(AuthenticationException e) {
        return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    // 捕捉shiro的异常
    @ExceptionHandler(ShiroException.class)
    public ApiResponse handle401(ShiroException e) {
        return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    /**
     * 处理所有接口数据验证异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        StringBuffer errorMsg = new StringBuffer();
        errors.forEach(x -> errorMsg.append(x.getDefaultMessage()).append(";"));
        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), errorMsg.toString());
    }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    public ApiResponse globalException(Exception e) {
        e.printStackTrace();
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
