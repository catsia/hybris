package concerttours.daos.impl;

import concerttours.daos.ProducerDAO;
import concerttours.model.ProducerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "producerDAO")
public class DefaultProducerDAO implements ProducerDAO
{
    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Override
    public List<ProducerModel> findProducers() {
        final String queryString = //
                "SELECT {p:" + ProducerModel.PK + "} "//
                        + "FROM {" + ProducerModel._TYPECODE + " AS p} ";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        return flexibleSearchService.<ProducerModel> search(query).getResult();
    }

    @Override
    public ProducerModel findProducerByCode(String code) {
        final String queryString = //
                "SELECT {p:" + ProducerModel.PK + "} "//
                        + "FROM {" + ProducerModel._TYPECODE + " AS p} "
                        + "WHERE " + "{p:" + ProducerModel.CODE + "}=?code ";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        return flexibleSearchService.<ProducerModel> search(query).getResult().get(0);
    }
}
