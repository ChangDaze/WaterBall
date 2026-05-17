package v0;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Schedule implements Individual{
    private final List<String> order;

    public Schedule(List<String> order) {
        this.order = order;
    }

    @Override
    public Integer getFitness() {
        /*
該公司需要生產 3 種產品：產品 A、產品 B 和產品 C，它們需要的生產時間分別為 2 小時、4 小時和 6 小時。公司有 2 台機器和 4 名工人可供使用，每個產品都需要使用一台機器和一名工人才能生產，並且每台機器和每名工人一次只能處理一個產品。
        */
        int machine1Time = 0;
        int machine2Time = 0;
        
        for (String character : order) {
            int productTime = 0;
            if ("A".equals(character)) {
                productTime = 2;
            } else if ("B".equals(character)) {
                productTime = 4;
            } else if ("C".equals(character)) {
                productTime = 6;
            }
            
            // Assign to the machine with less time
            if (machine1Time <= machine2Time) {
                machine1Time += productTime;
            } else {
                machine2Time += productTime;
            }
        }
        
        // Return negative of the maximum time used by either machine
        int maxTime = Math.max(machine1Time, machine2Time);
        return -maxTime;
    }

    @Override
    public Individual crossover(Individual individual) {
        Schedule other = (Schedule) individual;
        List<String> newOrder = new ArrayList<>();
        
        int midpoint = order.size() / 2;
        
        // Add front half of this object
        newOrder.addAll(order.subList(0, midpoint));
        
        // Add last half of the input object
        newOrder.addAll(other.order.subList(midpoint, other.order.size()));
        
        return new Schedule(newOrder);
    }

    @Override
    public List<Individual> mutate() {
        List<Individual> mutatedSchedules = new ArrayList<>();
        Random random = new Random();
        
        // Add this object itself
        mutatedSchedules.add(this);
        
        // Create 9 mutated versions
        for (int i = 0; i < 9; i++) {
            List<String> newOrder = new ArrayList<>(order);
            
            // Swap two random indices
            int index1 = random.nextInt(newOrder.size());
            int index2 = random.nextInt(newOrder.size());
            
            String temp = newOrder.get(index1);
            newOrder.set(index1, newOrder.get(index2));
            newOrder.set(index2, temp);
            
            mutatedSchedules.add(new Schedule(newOrder));
        }
        
        return mutatedSchedules;
    }

    @Override
    public String getGenes() {
        return order.toString();
    }
}
