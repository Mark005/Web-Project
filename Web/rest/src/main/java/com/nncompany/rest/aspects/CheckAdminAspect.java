package com.nncompany.rest.aspects;

import com.nncompany.api.model.wrappers.RequestError;
import com.nncompany.impl.util.UserKeeper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckAdminAspect {

    @Pointcut("execution(public * com.nncompany.rest.servlets.BriefingServlet.addBriefing(..)) || " +
              "execution(public * com.nncompany.rest.servlets.BriefingServlet.updateBriefing(..)) || " +
              "execution(public * com.nncompany.rest.servlets.BriefingServlet.deleteBriefing(..))")
    public void briefings() { }

    @Pointcut("execution(public * com.nncompany.rest.servlets.TaskServlet.addTask(..)) || " +
              "execution(public * com.nncompany.rest.servlets.TaskServlet.deleteTask(..)) || " +
              "execution(public * com.nncompany.rest.servlets.TaskServlet.deleteTask(..))")
    public void tasks() { }

    @Pointcut("execution(public * com.nncompany.rest.servlets.UserBriefingServlet.addConductedBriefing(..)) || " +
              "execution(public * com.nncompany.rest.servlets.UserBriefingServlet.updateConductionDate(..)) || " +
              "execution(public * com.nncompany.rest.servlets.UserBriefingServlet.deleteConductedBriefing(..))")
    public void userBriefing() { }

    @Around("briefings() || tasks() || userBriefing()")
    public Object beforeCallAtMethod1(ProceedingJoinPoint joinPoint) throws Throwable {
        if(!UserKeeper.getLoggedUser().isAdmin()){
            return new ResponseEntity<>(new RequestError(403,
                                                        "access denied",
                                                        "you haven't access for this operation, relogin as administrator"),
                                                        HttpStatus.FORBIDDEN);
        } else {
            return joinPoint.proceed();
        }
    }
}

