package com.sky.Asbect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author Gt_boy
 * @description: TODO
 * @date 2024/4/9 23:03
 */
@Component
@Aspect
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void PointCut(){};

    @Around("PointCut()")
    public void autoFill(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("进入切面增强");
//        获得连接点上的注解，连接点就是要增强的方法
        //signature是方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = annotation.value();

        //获得连接点中的参数
        Object[] args = joinPoint.getArgs();
        //我们规定mapper方法第一个参数放的的实体类
        Object entity = args[0];

        //获得实体类的大Class对象
        Class<?> entityClass = entity.getClass();

        //准备要加的参数
        LocalDateTime now = LocalDateTime.now();
        Long user = BaseContext.getCurrentId();

        //通过大Class对象获得方法
        if (operationType == OperationType.INSERT){
            try {
                Method setCreateTime = entityClass.getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entityClass.getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entityClass.getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entityClass.getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setCreateUser.invoke(entity,user);
                setCreateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,user);
                setUpdateTime.invoke(entity,now);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if (operationType == OperationType.UPDATE){
            try {
                Method setUpdateTime = entityClass.getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entityClass.getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateUser.invoke(entity,user);
                setUpdateTime.invoke(entity,now);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.info("离开切面");
        joinPoint.proceed();




//        log.info("切面增强OK了");
//        Signature signature = joinPoint.getSignature();
    }
}
