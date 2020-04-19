package com.nncompany.rest.aspects;

import com.nncompany.api.model.wrappers.RequestError;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckPathVariablesAspect {

    @Pointcut("execution(public * com.nncompany.rest.servlets.BriefingServlet.getBriefingById(..)) && args(id, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.BriefingServlet.updateBriefing(..)) && args(id, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.BriefingServlet.deleteBriefing(..)) && args(id, ..) " )
    public void briefings(Integer id) { }

    @Pointcut("execution(public * com.nncompany.rest.servlets.ChatServlet.getChatsMessage(..)) && args(msgId, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.ChatServlet.changeChatsMessage(..)) && args(msgId, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.ChatServlet.deleteChatsMessage(..)) && args(msgId, ..)")
    public void chat(Integer msgId) { }

    @Pointcut("execution(public * com.nncompany.rest.servlets.DialogServlet.getDialogWithUser(..)) && args(userId, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.DialogServlet.getMessageFromDialogWithUser(..)) && args(userId, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.DialogServlet.sendDialogMessage(..)) && args(userId, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.DialogServlet.changeDialogsMessage(..)) && args(userId, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.DialogServlet.deleteDialogsMessage(..)) && args(userId, ..)")
    public void dialog(Integer userId) { }

    @Pointcut("execution(public * com.nncompany.rest.servlets.TaskServlet.changeTask(..)) && args(id, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.TaskServlet.deleteTask(..)) && args(id, ..)")
    public void tasks(Integer id) { }

    @Pointcut("execution(public * com.nncompany.rest.servlets.UserBriefingServlet.getBriefingsByCurrentUser(..)) && args(id, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.UserBriefingServlet.getUsersByCurrentBriefing(..)) && args(id, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.UserBriefingServlet.updateConductionDate(..)) && args(id, ..) ||" +
              "execution(public * com.nncompany.rest.servlets.UserBriefingServlet.deleteConductedBriefing(..)) && args(id, ..)")
    public void userBriefing(Integer id) { }

    @Around("briefings(id)  || " +
            "chat(id) ||" +
            "dialog(id) ||" +
            "tasks(id) ||" +
            "userBriefing(id)")
    public Object checkPathVariable(ProceedingJoinPoint joinPoint, Integer id) throws Throwable {
        if(id < 1) {
            return new ResponseEntity<>(new RequestError(400,
                                                        "path variable error",
                                                        "path variable must be > 0"),
                                                        HttpStatus.BAD_REQUEST);
        } else {
            return joinPoint.proceed();
        }
    }
}
