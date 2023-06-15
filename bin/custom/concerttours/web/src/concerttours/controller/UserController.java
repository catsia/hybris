package concerttours.controller;


import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.core.model.user.UserModel;

import org.springframework.web.servlet.ModelAndView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController
{

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String handleRequest(final ModelMap model, @RequestParam String uid) throws Exception
	{
		UserModel user = null;
		if (uid == null || uid.equals(""))
		{
			user = userService.getCurrentUser();
		}
		else
		{
		        user = new UserModel();
		}
		model.put("user", user);
		return "user";
	}

}
