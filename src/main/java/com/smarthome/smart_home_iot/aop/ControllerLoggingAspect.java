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

    @Pointcut("execution(public * com.smarthome.smart_home_iot.controller..*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();

        apiLogger.info("API CALL {}", method);
        appLogger.info("START {}", method);
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();

        apiLogger.info("API RESPONSE {}", method);
        appLogger.info("END {}", method);
    }


    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String method = joinPoint.getSignature().toShortString();

        errorLogger.error("EXCEPTION in {}", method, ex);
    }
}
