package br.edu.fiec.MapeamentoDeSaude.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut que corresponde a todos os métodos públicos em qualquer classe
     * dentro dos pacotes de controladores.
     */
    @Pointcut("within(br.edu.fiec.MapeamentoDeSaude.features..*.*Controller)")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("Entrando no método: {} com argumentos: {}", methodName, Arrays.toString(args));
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterMethodExecution(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("Saindo do método: {} com resultado: {}", methodName, result);
    }
}