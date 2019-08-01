package com.haze.system.service;

import com.haze.TestApplication;
import com.haze.system.entity.User;
import com.haze.system.utils.Sex;
import com.haze.system.utils.Status;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    //@Test
    public void saveOrUpdate() {
        User user = new User();
        user.setName("admin");
        user.setLoginName("admin2");
        user.setPassword("123456");
        user.setEmail("admin@hotmail.com");
        user.setSex(Sex.M);
        user.setStatus(Status.ENABLE);
        try {
            userService.saveOrUpdate(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}