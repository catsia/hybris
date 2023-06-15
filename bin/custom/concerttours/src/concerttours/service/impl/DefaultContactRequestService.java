package concerttours.service.impl;

import concerttours.model.ContactRequestsModel;
import concerttours.service.ContactRequestService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

public class DefaultContactRequestService implements ContactRequestService {
    private FlexibleSearchService flexibleSearchService;

    public ContactRequestsModel getContactRequest(final String sender) {
        final String queryString = "SELECT {PK} FROM {ContactRequests} WHERE {sender} = " + sender;
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        final SearchResult<ContactRequestsModel> result = this.flexibleSearchService.search(query);
        final int resultCount = result.getTotalCount();
        if (resultCount == 0) {
            throw new UnknownIdentifierException(
                    "ContactRequest with sender '" + sender + "' not found!"
            );
        }
        return result.getResult().get(0);
    }

    public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}
