package v0;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        Selection defaultSelection = new DefaultSelection();
        Crossover defaultCrossover = new DefaultCrossover();
        Mutation defaultMutation = new DefaultMutation();

        PopulationFactory scheduleFactory = new SchedulePopulation();

        GeneticAlgorithm scheduleGeneticAlgorithm = new GeneticAlgorithm(defaultSelection, defaultCrossover, defaultMutation, scheduleFactory);
        scheduleGeneticAlgorithm.start();

        PopulationFactory suggestionFactory = new SuggestionPopulation();
        GeneticAlgorithm suggestionGeneticAlgorithm = new GeneticAlgorithm(defaultSelection, defaultCrossover, defaultMutation, suggestionFactory);
        suggestionGeneticAlgorithm.start();
    }
}
