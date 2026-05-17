package v0;

import java.util.ArrayList;
import java.util.List;

public class SchedulePopulation implements PopulationFactory{
    @Override
    public List<Individual> getDefaultPopulation() {
        List<String> order = new ArrayList<>();
        
        // Add 100 A's
        for (int i = 0; i < 100; i++) {
            order.add("A");
        }
        
        // Add 200 B's
        for (int i = 0; i < 200; i++) {
            order.add("B");
        }
        
        // Add 300 C's
        for (int i = 0; i < 300; i++) {
            order.add("C");
        }
        
        Schedule schedule1 = new Schedule(order);
        Schedule schedule2 = new Schedule(order);
        return List.of(schedule1, schedule2);
    }

    @Override
    public Individual findBestIndividual(List<Individual> population) {
        return population.stream()
                .max((individual1, individual2) -> individual1.getFitness().compareTo(individual2.getFitness()))
                .orElse(null);
    }
}
