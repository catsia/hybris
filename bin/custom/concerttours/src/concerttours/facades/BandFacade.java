package concerttours.facades;

import java.util.List;
import concerttours.data.BandData;
public interface BandFacade {
    BandData getBand(String name);
    List<BandData> getBands();
}
