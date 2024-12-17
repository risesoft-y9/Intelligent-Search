package net.risesoft.sso.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.risesoft.sso.exception.AuthException;

/**
 * 全局的异常处理器
 */
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({Throwable.class})
    public String processException(Throwable e) {
        e.printStackTrace();
        return e.getMessage();
    }

    @ExceptionHandler({AuthException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String processAuthException(AuthException e) {
        e.printStackTrace();
        return e.getMessage();
    }

}
