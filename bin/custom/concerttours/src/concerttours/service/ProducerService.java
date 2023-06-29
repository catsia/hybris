package concerttours.service;

import concerttours.model.ProducerModel;

import java.util.List;


public interface ProducerService
{
   List<ProducerModel> getProducers();
   ProducerModel getProducerByCode(String code);
}
