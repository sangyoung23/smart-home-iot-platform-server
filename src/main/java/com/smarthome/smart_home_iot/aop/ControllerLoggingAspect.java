package com.smarthome.smart_home_iot.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect {

    private static final Logger appLogger   = LoggerFactory.getLogger("APP");
    private static final Logger apiLogger   = LoggerFactory.getLogger("API");
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR");
    // auditlog는 실제로 중요 이벤트가 발생할 때만 직 호출하는 방식으로 변경
    // private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");

    @Pointcut("execution(public * com.smarthome.smart_home_iot.controller..*(..))")
    public void controllerMethods() {}

    // Before: 메서드 호출 전
    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        appLogger.info("START {} - args: {}", method, args);
        apiLogger.info("API CALL {} - args: {}", method, args);
//        auditLogger.info("AUDIT START {} - args: {}", method, args);
    }

    // AfterReturning: 정상 종료
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().toShortString();

        appLogger.info("END {} - result: {}", method, result);
        apiLogger.info("API RESPONSE {} - result: {}", method, result);
//        auditLogger.info("AUDIT END {} - result: {}", method, result);
    }

    // AfterThrowing: 예외 발생
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String method = joinPoint.getSignature().toShortString();

        appLogger.error("EXCEPTION in {} - message: {}", method, ex.getMessage(), ex);
        errorLogger.error("ERROR in {} - message: {}", method, ex.getMessage(), ex);
//        auditLogger.error("AUDIT ERROR in {} - message: {}", method, ex.getMessage(), ex);
    }
}
