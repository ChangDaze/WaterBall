package v0;

import java.util.List;

public class DefaultCrossover implements Crossover{
    @Override
    public Individual crossover(List<Individual> population) {
        return population.get(0).crossover(population.get(1));
    }
}
