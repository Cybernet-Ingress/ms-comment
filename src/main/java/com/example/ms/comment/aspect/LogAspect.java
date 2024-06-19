package com.example.ms.comment.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Around(value = "within(@com.example.ms.comment.annotation.LogAnnotation *)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().getName();
        log.info("ActionLog.start {}", method);
        Object action = joinPoint.proceed();
        log.info("ActionLog.end {}", method);
        return action;
    }
}