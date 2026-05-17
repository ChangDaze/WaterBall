package v0;

import java.util.List;

public interface Individual {
    Integer getFitness();
    Individual crossover(Individual individual);
    List<Individual> mutate();
    String getGenes();
}
