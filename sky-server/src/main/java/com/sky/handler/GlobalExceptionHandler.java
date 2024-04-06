package com.sky.handler;

import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /*
    * Spring MVC 异常处理机制： Spring MVC 提供了一套异常处理机制，
    * 通过 @ExceptionHandler 注解可以在控制器类中定义处理特定异常的方法，
    * 这些方法会在发生异常时被调用，从而实现局部异常处理。
    * 全局异常处理则是通过 @ControllerAdvice 注解来实现，
    * 该注解标注的类会被注册为全局异常处理器，
    * 其中的 @ExceptionHandler 方法可以处理所有控制器类抛出的异常。
    * */

    //当 Spring Boot 应用中发生异常时，Spring MVC 会首先寻找匹配的局部异常处理器，如果找不到，则会将异常交给全局异常处理器来处理。
    /*全局异常处理器会根据异常的类型调用对应的 @ExceptionHandler 方法进行处理，最终返回合适的错误响应给客户端。这样就实现了全局异常处理的功能。*/


    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //Duplicate entry 'xiaoyan' for key 'employee.idx_username'
        log.info("ex:{}",ex.getMessage());
        String[] message = ex.getMessage().split(" ");
        return Result.error("用户名"+message[2]+"已存在");
    }

}
