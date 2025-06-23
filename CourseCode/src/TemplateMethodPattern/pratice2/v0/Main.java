package TemplateMethodPattern.pratice2.v0;

import TemplateMethodPattern.pratice1.v0.Class1;
import TemplateMethodPattern.pratice1.v0.Class2;

public class Main {
    public static void main(String[] args){
        String[] array = new String[]{ "0123456789", "", "Waterball", "Waterball"};
        SearchLongestMessage test1 = new SearchLongestMessage();
        SearchEmptyMessageIndex test2 = new SearchEmptyMessageIndex();
        CountNumberOfWaterballs test3 = new CountNumberOfWaterballs();

        System.out.println("===test1===");
        var result1 = test1.search(array);
        System.out.printf("【result1】 %s \n", result1);

        System.out.println("===test2===");
        var result2 = test2.search(array);
        System.out.printf("【result2】 %s \n", result2);

        System.out.println("===test3===");
        var result3 = test3.count(array);
        System.out.printf("【result3】 %d \n", result3);
    }
}
