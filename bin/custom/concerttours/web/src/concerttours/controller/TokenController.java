package concerttours.controller;


import concerttours.model.TokenModel;
import concerttours.service.TokenModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class TokenController
{
	@Autowired
	private TokenModelService tokenModelService;

	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public String handleRequest(final ModelMap model) throws Exception
	{
		TokenModel tokenModel = tokenModelService.getToken();
		model.put("token", tokenModel);
		return "token";
	}

}
