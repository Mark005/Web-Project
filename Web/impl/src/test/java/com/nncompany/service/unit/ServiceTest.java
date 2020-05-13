package com.nncompany.service.unit;

import com.nncompany.api.interfaces.services.*;
import com.nncompany.api.model.entities.*;
import com.nncompany.api.model.enums.Gender;
import com.nncompany.api.model.enums.TaskStatus;
import com.nncompany.api.model.enums.TaskType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application-context-test.xml"})
@Transactional
public class ServiceTest {

    @Autowired
    IBriefingService briefingService;

    @Test
    public void briefingServiceTest(){
        Briefing briefing = new Briefing();
        briefing.setName("something smart name");
        briefing.setIntervalInMonths(32);

        briefingService.save(briefing);
        Assert.assertNotNull(briefingService.get(briefing.getId()));
        Assert.assertEquals(briefing.getName(), briefingService.get(briefing.getId()).getName());

        briefing.setName("not very smart name :( ");
        briefingService.update(briefing);
        Assert.assertEquals(briefing.getName(), briefingService.get(briefing.getId()).getName());

        Assert.assertNotNull(briefingService.getTotalCount());

        Assert.assertTrue(briefingService.getAll().size() == briefingService.getTotalCount());

        Assert.assertNotNull(briefingService.getWithPagination(3, 3));

        briefingService.delete(briefing);
        Assert.assertNull(briefingService.get(briefing.getId()));
    }



    @Autowired
    IMessageService messageService;

    @Test
    public void messageServiceTest(){
        Message message = new Message();
        User userOne = userService.getAll().get(0);
        User userTwo = userService.getAll().get(1);
        User userEmpty = null;
        message.setUserFrom(userOne);
        message.setUserTo(userTwo);
        message.setDate(new Date());
        message.setText("MOST POWERFUL LETTERS!!!11");

        messageService.save(message);
        Assert.assertNotNull(messageService.get(message.getId()));
        Assert.assertEquals(message.getText(), messageService.get(message.getId()).getText());

        message.setText("weak letters");
        messageService.update(message);
        Assert.assertEquals(message.getText(), messageService.get(message.getId()).getText());

        List messagesList = messageService.getDialogWithPagination(userOne,userTwo,0,999999);
        Integer countMessages = messageService.getTotalCountMessages(userOne, userTwo);
        Assert.assertTrue(messagesList.size() == countMessages);

        List chatsList = messageService.getChatWithPagination(0,999999);
        Integer chatCountMessages = messageService.getTotalCountMessages(userOne, userEmpty);
        Assert.assertTrue(chatsList.size() == chatCountMessages);

        messageService.delete(message);
        Assert.assertNull(messageService.get(message.getId()));
    }



    @Autowired
    ITaskService taskService;

    @Test
    public void taskServiceTest(){
        User userOne = userService.getAll().get(0);
        User userTwo = userService.getAll().get(1);

        Task task = new Task();
        task.setName("task only for gods");
        task.setType(TaskType.PERSONAL);
        task.setCreator(userOne);
        task.setExecutor(userTwo);
        task.setStatus(TaskStatus.OPEN);
        task.setDeadline(new Date());

        taskService.save(task);
        Assert.assertNotNull(taskService.get(task.getId()));
        Assert.assertEquals(task.getName(), taskService.get(task.getId()).getName());

        task.setName("task changed to another");
        taskService.update(task);
        Assert.assertEquals(task.getName(), taskService.get(task.getId()).getName());

        taskService.delete(task);
        Assert.assertNull(taskService.get(task.getId()));

        Integer getAllSize = taskService.getAll(0,999999,TaskStatus.OPEN, TaskType.PERSONAL).size();
        Integer getAllCount = taskService.getTotalCountForGetAll(TaskStatus.OPEN, TaskType.PERSONAL);
        Assert.assertTrue(getAllSize  == getAllCount);

        Integer getUsersTasksSize = taskService.getUsersTasks(userOne, TaskStatus.OPEN, TaskType.PERSONAL).size();
        Integer getUsersTasksCount = taskService.getTotalCountForGetUsersTasks(userOne, TaskStatus.OPEN, TaskType.PERSONAL);
        Assert.assertTrue(getUsersTasksSize  == getUsersTasksCount);

        taskService.delete(task);
        Assert.assertNull(taskService.get(task.getId()));
    }



    @Autowired
    IUserBriefingService userBriefingService;

    @Test
    public void userBriefingServiceTest(){
        User userOne = userService.getAll().get(0);
        User userTwo = userService.getAll().get(1);
        Briefing briefing = briefingService.getAll().get(0);

        UserBriefing userBriefing = new UserBriefing();
        userBriefing.setUser(userOne);
        userBriefing.setBriefing(briefing);
        userBriefing.setLastDate(new Date());

        userBriefingService.save(userBriefing);
        Assert.assertNotNull(userBriefingService.get(userBriefing.getId()));
        Assert.assertEquals(userBriefing.getLastDate(), userBriefingService.get(userBriefing.getId()).getLastDate());

        userBriefing.setUser(userTwo);
        userBriefingService.update(userBriefing);
        Assert.assertEquals(userBriefing.getUser(), userBriefingService.get(userBriefing.getId()).getUser());

        Assert.assertNotNull(briefingService.getTotalCount());

        Assert.assertTrue(userBriefingService.getAll().size() == userBriefingService.getTotalCount());

        userBriefingService.delete(userBriefing);
        Assert.assertNull(userBriefingService.get(userBriefing.getId()));
    }


    @Autowired
    IUserCredsService userCredsService;

    @Test
    public void userCredsServiceTest(){

        User user = new User();
            user.setCertificateNumber(1);
            user.setName("aaabbbccc");
            user.setSurname("rfdg");
            user.setGender(Gender.MALE);
            user.setProfession("chisto lox");
            user.setDateEmployment(new Date());
            user.setAdmin(false);
        UserCreds userCreds = new UserCreds();
        userCreds.setLogin("s_dfgLKqlboKFDC`kbmk'mnt");
        userCreds.setPass("123654");
        userCreds.setUser(user);

        Assert.assertFalse(userCredsService.checkLogin(userCreds.getLogin()));

        userService.save(user);
        userCredsService.save(userCreds);
        Assert.assertTrue(userCredsService.checkLogin(userCreds.getLogin()));

        Assert.assertNotNull(userCredsService.get(userCreds.getId()));

        Assert.assertTrue(userCredsService.getAll().size() != 0);

        Assert.assertNotNull(userCredsService.getUserCredsByLoginAndPass(userCreds.getLogin(), userCreds.getPass()));

        userCreds.setPass("qaz");
        userCredsService.update(userCreds);
        Assert.assertTrue(userCredsService.get(userCreds.getId()).getPass() == userCreds.getPass());
    }



    @Autowired
    private IUserService userService;

    @Test
    public void userServiceTest()
    {
        User user = new User();
            user.setCertificateNumber(1);
            user.setName("aaabbbccc");
            user.setSurname("rfdg");
            user.setGender(Gender.MALE);
            user.setProfession("chisto lox");
            user.setDateEmployment(new Date());
            user.setAdmin(false);
        userService.save(user);
        Assert.assertEquals(user.getName(), userService.get(user.getId()).getName());

        user.setName("new");
        userService.update(user);
        Assert.assertEquals(user.getName(), userService.get(user.getId()).getName());

        Assert.assertTrue(userService.getTotalCount() == userService.getAll().size());

        userService.delete(user);
        Assert.assertNull(userService.get(user.getId()));
    }
}
