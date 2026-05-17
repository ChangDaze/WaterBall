package v0;

import java.util.List;

public class GeneticAlgorithm {
    private final Selection selection;
    private final Crossover crossover;
    private final Mutation mutation;
    private final PopulationFactory populationFactory;
    private final Integer TIME_OF_ITERATIONS = 10;

    public GeneticAlgorithm(Selection selection, Crossover crossover, Mutation mutation, PopulationFactory populationFactory) {
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
        this.populationFactory = populationFactory;
    }

    public void start() {
        List<Individual> population = populationFactory.getDefaultPopulation();

        for (int i = 0; i < TIME_OF_ITERATIONS; i++) {
            // 選擇
            List<Individual> selectedIndividuals = selection.select(population);

            // 交配
            Individual offspring = crossover.crossover(selectedIndividuals);

            // 突變
            population = mutation.mutate(offspring);
        }

        Individual bestIndividual = populationFactory.findBestIndividual(population);
        System.out.println("Best Individual: " + bestIndividual.getGenes());
    }

}
