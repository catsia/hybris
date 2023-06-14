package concerttours.service;

import concerttours.model.BandModel;

public interface TrainingBandService extends BandService {
    BandModel getBandForCode(String code);
}
