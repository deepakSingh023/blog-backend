package com.example.blog_backend.aspect;


import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class MetricAspect {

    private final MeterRegistry registry;

    public MetricAspect(MeterRegistry registry) {
        this.registry = registry;
    }

    @Around("execution(* com.example.blog_backend.controller..*(..))")
    public Object recordMetrics(ProceedingJoinPoint jp) throws Throwable {

        String api = jp.getSignature().getName();
        String controller =
                jp.getSignature().getDeclaringType().getSimpleName();

        Timer.Sample sample = Timer.start(registry);

        try {
            Object result = jp.proceed();

            sample.stop(
                    Timer.builder("http.api.latency")
                            .tag("controller", controller)
                            .tag("api", api)
                            .tag("status", "success")
                            .publishPercentiles(0.5,0.95,0.99)
                            .publishPercentileHistogram()
                            .register(registry)
            );

            registry.counter(
                    "http.api.count",
                    "controller", controller,
                    "api", api,
                    "status", "success"
            ).increment();

            return result;

        } catch (Exception ex) {

            registry.counter(
                    "http.api.count",
                    "controller", controller,
                    "api", api,
                    "status", "error"
            ).increment();

            throw ex;
        }
    }
}
