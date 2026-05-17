package v0;

import java.util.List;

public interface Mutation {
    List<Individual> mutate(Individual individual);
}
