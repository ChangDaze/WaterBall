package TemplateMethodPattern.pratice1.v0;

public class Main {
    public static void main(String[] args){
        //應該都是泡沫排序
        int[] array1 = new int[]{ 1, 2, 3, 7, 8, 9, 4, 5, 6};
        printData(array1);
        Class1 class1 = new Class1();
        class1.p1(array1);
        printData(array1);

        int[] array2 = new int[]{ 1, 2, 3, 7, 8, 9, 4, 5, 6};
        printData(array2);
        Class2 class2 = new Class2();
        class2.p2(array2);
        printData(array2);
    }

    private static void printData(int[] array){
        for (int i : array) {
            System.out.print(i);
            System.out.print(", ");
        }
        System.out.println();
    }
}
