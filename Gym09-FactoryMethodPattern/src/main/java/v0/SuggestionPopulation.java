package v0;

import java.util.ArrayList;
import java.util.List;

public class SuggestionPopulation implements PopulationFactory{
    @Override
    public List<Individual> getDefaultPopulation() {
        List<Integer> combination = List.of(2, 0, 0, 1, 1, 0);
        Suggestion suggestion1 = new Suggestion(combination);
        Suggestion suggestion2 = new Suggestion(combination);
        return List.of(suggestion1,suggestion2);
    }

    @Override
    public Individual findBestIndividual(List<Individual> population) {
        return population.stream()
                .max((individual1, individual2) -> individual1.getFitness().compareTo(individual2.getFitness()))
                .orElse(null);
    }
}
