package com.haze.system.event;

import com.haze.system.entity.User;
import org.springframework.context.ApplicationEvent;


public class UserChangeGroupEvent extends ApplicationEvent {

    private User user;
    public UserChangeGroupEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
