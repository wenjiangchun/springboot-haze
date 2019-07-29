package com.haze.system.service.ws;

import javax.jws.WebService;

import com.haze.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

@Component
@WebService(endpointInterface="com.haze.system.service.ws.UserServiceWS")
public class UserServiceWSImpl implements UserServiceWS {

	@Autowired
	private UserService userService;
	
	@Override
	public String getUser(String id, int count) {
        return id + count;
	}

}
