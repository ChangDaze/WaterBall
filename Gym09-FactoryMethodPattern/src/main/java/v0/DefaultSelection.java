package v0;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultSelection implements Selection{
    @Override
    public List<Individual> select(List<Individual> population) {
        return population.stream()
                .sorted((individual1, individual2) -> individual2.getFitness().compareTo(individual1.getFitness()))
                .limit(2)
                .collect(Collectors.toList());
    }
}
