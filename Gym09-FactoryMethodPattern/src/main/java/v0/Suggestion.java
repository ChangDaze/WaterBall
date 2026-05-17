package v0;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Suggestion implements Individual{
    private final List<Integer> combination;

    public Suggestion(List<Integer> combination) {
        this.combination = combination;
    }

    @Override
    public Integer getFitness() {
/*
你所經營的購物網站有以下產品：
產品 1：價格 100 元，重量 2 公斤，類別 A。
產品 2：價格 200 元，重量 3 公斤，類別 A。
產品 3：價格 150 元，重量 5 公斤，類別 B。
產品 4：價格 300 元，重量 4 公斤，類別 B。
產品 5：價格 180 元，重量 6 公斤，類別 C。
產品 6：價格 250 元，重量 7 公斤，類別 C。

系統會記載每個客戶對不同類別的產品的喜好度，好比你有一位客戶他的喜好度 (Preference) 如下：
類別 A：80%
類別 B：60%
類別 C：20%

假設客戶預算為 700 元，購物袋最大承載重量為 15 公斤
*/
        // Product definitions: [cost, weight, category (0=A, 1=B, 2=C)]
        int[][] products = {
            {100, 2, 0},  // Product 1: cost, weight, category A
            {200, 3, 0},  // Product 2: cost, weight, category A
            {150, 5, 1},  // Product 3: cost, weight, category B
            {300, 4, 1},  // Product 4: cost, weight, category B
            {180, 6, 2},  // Product 5: cost, weight, category C
            {250, 7, 2}   // Product 6: cost, weight, category C
        };
        
        // Category preferences: A=80, B=60, C=20
        int[] categoryPrefs = {80, 60, 20};
        
        int totalCost = 0;
        int totalWeight = 0;
        int totalPreference = 0;
        
        // Calculate total cost, weight, and preference
        for (int i = 0; i < combination.size(); i++) {
            int count = combination.get(i);
            int[] product = products[i];
            int cost = product[0];
            int weight = product[1];
            int category = product[2];
            
            totalCost += cost * count;
            totalWeight += weight * count;
            
            // Add preference for this product (count * category preference)
            totalPreference += count * categoryPrefs[category];
        }
        
        // Check constraints: if exceeds cost or weight, return -1
        if (totalCost > 700 || totalWeight > 15) {
            return -1;
        }
        
        return totalPreference;
    }

    @Override
    public Individual crossover(Individual individual) {
        Suggestion other = (Suggestion) individual;
        List<Integer> newCombination = new ArrayList<>();

        // 前後分界線
        int midpoint = combination.size() / 2;
        
        // Add front half of this object
        newCombination.addAll(combination.subList(0, midpoint));
        
        // Add last half of the input object
        newCombination.addAll(other.combination.subList(midpoint, other.combination.size()));
        
        return new Suggestion(newCombination);
    }

    @Override
    public List<Individual> mutate() {
        List<Individual> mutatedSuggestions = new ArrayList<>();
        Random random = new Random();
        
        // Add this object itself
        mutatedSuggestions.add(this);
        
        // Create 9 mutated versions
        for (int i = 0; i < 9; i++) {
            List<Integer> newCombination = new ArrayList<>(combination);
            
            // Randomly select an index
            int randomIndex = random.nextInt(newCombination.size());
            
            // Randomly add 1 or subtract 1
            int change = random.nextBoolean() ? 1 : -1;
            int currentValue = newCombination.get(randomIndex);
            
            // Ensure the value doesn't go below 0
            if (currentValue + change >= 0) {
                newCombination.set(randomIndex, currentValue + change);
            }
            
            mutatedSuggestions.add(new Suggestion(newCombination));
        }
        
        return mutatedSuggestions;
    }

    @Override
    public String getGenes() {
        return combination.toString();
    }
}
