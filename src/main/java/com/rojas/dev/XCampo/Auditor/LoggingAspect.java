package com.rojas.dev.XCampo.Auditor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger aspectLogger = LoggerFactory.getLogger("AspectLogger");

    @Pointcut("within(com.rojas.dev.XCampo.controller..*)")
    public void controllerPointcut(){}

    @Before("controllerPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        aspectLogger.info("Llamada al método: {} con argumentos: {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs()
                );
    }

    @AfterReturning(pointcut = "controllerPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result){
        aspectLogger.info("Método ejecutado: {}, Retorno: {}",
                joinPoint.getSignature().toShortString(),
                result
                );
    }

    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "error")
    public void logError(JoinPoint joinPoint, Throwable error){
        aspectLogger.error("Excepción en método: {}, Mensaje: {}",
                joinPoint.getSignature().toShortString(),
                error.getMessage(), error
                );
    }
}
