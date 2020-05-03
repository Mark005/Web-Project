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

    @Pointcut("execution(public * com.nncompany.rest.servlets.ChatServlet.getChatsMessages(..)) && args(page, pageSize, ..)")
    public void chat(Integer page, Integer pageSize) { }

    @Pointcut("execution(public * com.nncompany.rest.servlets.DialogServlet.getDialogWithUser(..)) && args(userId, page, pageSize, ..)")
    public void dialogs(Integer userId, Integer page, Integer pageSize) { }

    @Pointcut("execution(public * com.nncompany.rest.servlets.TaskServlet.getAllTasks(..)) && args(page, pageSize, ..)")
    public void tasks(Integer page, Integer pageSize) { }

    @Pointcut("execution(public * com.nncompany.rest.servlets.UserBriefingServlet.getAll(..)) && args(page, pageSize, ..)")
    public void userBriefing(Integer page, Integer pageSize) { }

    @Pointcut("execution(public * com.nncompany.rest.servlets.UserServlet.getUsers(..)) && args(page, pageSize, ..)")
    public void users(Integer page, Integer pageSize) { }


    @Around("briefings(page, pageSize) || " +
            "chat(page, pageSize) || " +
            "tasks(page, pageSize) ||" +
            "userBriefing(page, pageSize) ||" +
            "users(page, pageSize)")
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

    @Around("dialogs(userId, page, pageSize)")
    public Object checkRequestParams(ProceedingJoinPoint joinPoint, Integer userId, Integer page, Integer pageSize) throws Throwable {
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
