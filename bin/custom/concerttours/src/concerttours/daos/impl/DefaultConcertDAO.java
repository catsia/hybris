package concerttours.daos.impl;

import concerttours.daos.ConcertDAO;
import concerttours.model.ConcertModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "concertDAO")
public class DefaultConcertDAO implements ConcertDAO
{
    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Override
    public List<ConcertModel> findConcerts() {
        final String queryString = //
                "SELECT {p:" + ConcertModel.PK + "} "//
                        + "FROM {" + ConcertModel._TYPECODE + " AS p} ";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        return flexibleSearchService.<ConcertModel> search(query).getResult();
    }

}
