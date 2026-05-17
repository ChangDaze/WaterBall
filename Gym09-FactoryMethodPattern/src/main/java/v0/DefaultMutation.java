package v0;

import java.util.List;

public class DefaultMutation implements Mutation {
    @Override
    public List<Individual> mutate(Individual individual) {
        return individual.mutate();
    }
}
