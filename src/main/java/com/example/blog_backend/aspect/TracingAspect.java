package com.example.blog_backend.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.logging.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@Order(1) // tracing FIRST
public class TracingAspect {

    @Around("execution(* com.example.blog_backend.controller..*(..))")
    public Object trace(ProceedingJoinPoint jp) throws Throwable {

        String traceId = UUID.randomUUID().toString();

        MDC.put("traceId", traceId);

        try {
            return jp.proceed();
        } finally {
            MDC.clear();
        }
    }
}
