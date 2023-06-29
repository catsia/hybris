package concerttours.service.impl;

import concerttours.daos.ConcertDAO;
import concerttours.model.ConcertModel;
import concerttours.service.ConcertService;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DefaultConcertService implements ConcertService
{
    private ConcertDAO concertDAO;

    @Required
    public void setConcertDAO(final ConcertDAO concertDAO)
    {
        this.concertDAO = concertDAO;
    }


    @Override
    public List<ConcertModel> getConcerts() {
        return concertDAO.findConcerts();
    }

}
