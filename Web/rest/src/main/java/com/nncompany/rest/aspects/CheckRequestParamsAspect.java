package com.nncompany.rest.aspects;

import com.nncompany.api.model.wrappers.RequestError;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckRequestParamsAspect {

    @Pointcut("execution(public * com.nncompany.rest.servlets.BriefingServlet.*(..)) && args(page, pageSize, ..)")
    public void briefings(Integer page, Integer pageSize) { }

    @Around("briefings(page ,pageSize)")
    public Object checkRequestParams(ProceedingJoinPoint joinPoint, Integer page, Integer pageSize) throws Throwable {
        if(page < 0 || pageSize < 1) {
            return new ResponseEntity<>(new RequestError(400,
                                                        "query params error",
                                                        "query params must be: page >= 0 and pageSize > 0"),
                                                        HttpStatus.BAD_REQUEST);
        } else {
            return joinPoint.proceed();
        }
    }
}
