package concerttours.controller;


import concerttours.data.BandData;
import concerttours.facades.BandFacade;
import concerttours.model.BandModel;
import java.util.List;
import java.util.ArrayList;
import concerttours.service.BandService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
	private BandFacade bandFacade;

	@RequestMapping(value = "/bands", method = RequestMethod.GET)
	public String handleRequest(final ModelMap model) throws Exception
	{
		List<BandData> bands = bandFacade.getBands();
		model.put("bands", bands);
		return "bands";
	}

}
