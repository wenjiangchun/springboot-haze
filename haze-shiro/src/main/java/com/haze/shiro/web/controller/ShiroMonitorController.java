package com.haze.shiro.web.controller;

import com.haze.shiro.ShiroUser;
import com.haze.web.BaseController;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(value = "/shiro")
public class ShiroMonitorController extends BaseController {

	@Autowired
	private SessionDAO sessionDAO;
		
	@GetMapping(value = "/online/view")
	public String list(Model model) {
		List<ShiroUser> onlineList = new ArrayList<>();
		Collection<Session> activeSessions = sessionDAO.getActiveSessions();
		for (Session activeSession : activeSessions) {
			SimplePrincipalCollection principalCollection;
			if (activeSession.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
				principalCollection = (SimplePrincipalCollection) activeSession
						.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				ShiroUser user = (ShiroUser) principalCollection.getPrimaryPrincipal();
				user.setIp(activeSession.getHost());
				user.setStartLoginTime(activeSession.getStartTimestamp());
				onlineList.add(user);
			}
		}
		model.addAttribute("onlineList", onlineList);
		return "shiro/onlineList";
	}
	

}
