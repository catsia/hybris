package concerttours.service.impl;

import concerttours.model.BandModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import concerttours.service.TrainingBandService;

import java.util.List;

public class TrainingBandServiceImpl extends DefaultBandService implements TrainingBandService {
    @Override
    public BandModel getBandForCode(String code) {
        final List<BandModel> result = bandDAO.findBandsByCode(code);
        if (result.isEmpty())
        {
            throw new UnknownIdentifierException("Band with code '" + code + "' not found!");
        }
        else if (result.size() > 1)
        {
            throw new AmbiguousIdentifierException("Band code '" + code + "' is not unique, " + result.size() + " bands found!");
        }
        return result.get(0);
    }

}
