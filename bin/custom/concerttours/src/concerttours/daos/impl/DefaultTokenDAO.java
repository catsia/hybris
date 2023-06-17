package concerttours.daos.impl;

import concerttours.daos.TokenDAO;
import concerttours.model.TokenModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "tokenDAO")
public class DefaultTokenDAO implements TokenDAO
{
    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Override
    public TokenModel findToken() {
        final String queryString = //
                "SELECT {p:" + TokenModel.PK + "} "//
                        + "FROM {" + TokenModel._TYPECODE + " AS p} ";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        return flexibleSearchService.<TokenModel> search(query).getResult().get(0);
    }
}
