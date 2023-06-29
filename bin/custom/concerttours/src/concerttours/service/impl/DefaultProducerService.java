package concerttours.service.impl;

import concerttours.daos.ProducerDAO;
import concerttours.model.ProducerModel;
import concerttours.service.ProducerService;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DefaultProducerService implements ProducerService
{
    private ProducerDAO ProducerDAO;

    @Required
    public void setProducerDAO(final ProducerDAO ProducerDAO)
    {
        this.ProducerDAO = ProducerDAO;
    }


    @Override
    public List<ProducerModel> getProducers() {
        return ProducerDAO.findProducers();
    }

    @Override
    public ProducerModel getProducerByCode(String code) {
        return ProducerDAO.findProducerByCode(code);
    }
}
