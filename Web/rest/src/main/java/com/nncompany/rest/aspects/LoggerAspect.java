package com.nncompany.rest.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {

    @AfterThrowing( pointcut = "execution(public * com.nncompany.rest.servlets.*.*(..))",
                    throwing= "ex")
    public void loggingExceptions(JoinPoint joinPoint, Exception ex){
        ObjectMapper objectMapper = new ObjectMapper();
        Logger log = Logger.getLogger(joinPoint.getTarget().getClass());
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();

        String message = ex.getMessage() + "\nCall method '" + methodName + "' with " + methodArgs.length + " args:\n" ;
        try {
            message += objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(methodArgs);
        } catch (JsonProcessingException e){
            Logger.getLogger(this.getClass()).error(e.getMessage());
        }
        log.error(message);
    }
}
