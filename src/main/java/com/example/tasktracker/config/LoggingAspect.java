package com.example.tasktracker.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Logs method entry before execution.
     *
     * @param joinPoint provides access to method signature
     *                  and method arguments
     */
    @Before("execution(* com.example.tasktracker.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering: {} with args={}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    /**
     * Logs method exit after successful execution.
     *
     * @param joinPoint provides access to method signature
     * @param result the value returned by the method
     */
    @AfterReturning(
            pointcut = "execution(* com.example.tasktracker.service.*.*(..))",
            returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("Exiting: {} with result={}",
                joinPoint.getSignature().getName(),
                result);
    }

    /**
     * Logs exceptions thrown during method execution.
     *
     * @param joinPoint provides access to method signature
     * @param ex the exception thrown by the method
     */
    @AfterThrowing(
            pointcut = "execution(* com.example.tasktracker.service.*.*(..))",
            throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        log.error("Exception in {}: {}",
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }
}