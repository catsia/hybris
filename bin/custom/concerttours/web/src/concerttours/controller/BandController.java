package concerttours.controller;


import concerttours.model.BandModel;
import java.util.List;
import java.util.ArrayList;
import concerttours.service.BandService;

import org.springframework.web.servlet.ModelAndView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BandController
{

	@Autowired
	private BandService bandService;

	@RequestMapping(value = "/bands", method = RequestMethod.GET)
	public String handleRequest(final ModelMap model) throws Exception
	{
		List<BandModel> bands = bandService.getBands();
		model.put("bands", bands);
		return "bands";
	}

}
