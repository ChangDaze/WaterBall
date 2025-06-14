package TemplateMethodPattern.common;

import java.util.List;

public class Utiles {
    public static void printGroups(List<Group> groups) {
        for (Group group : groups) {
            System.out.print("[");
            //一行寫兩個
            for (int i = 0; i < group.size(); i++) {
                if ( i % 2 == 0) {
                    System.out.print(group.get(i)); //會觸發student override toString
                } else {
                    System.out.printf(", %s", group.get(i));
                    if ( i != group.size() - 1) {
                        System.out.println();
                    }
                }
            }
            System.out.println();
            System.out.println();
        }
        System.out.print("]\n\n");
    }
}
