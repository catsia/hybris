package concerttours.controller;


import concerttours.model.ConcertModel;
import concerttours.model.ProducerModel;
import concerttours.service.ConcertService;
import concerttours.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ProducerController
{

	@Autowired
	private ProducerService producerService;

	@RequestMapping(value = "/producers", method = RequestMethod.GET)
	public String handleRequest(final ModelMap model) throws Exception
	{
		List<ProducerModel> producers = producerService.getProducers();
		model.put("producers", producers);
		return "producers";
	}

}
