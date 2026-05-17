package v0;

import java.util.List;

public interface PopulationFactory {
    List<Individual> getDefaultPopulation();
    Individual findBestIndividual(List<Individual> population);
}
