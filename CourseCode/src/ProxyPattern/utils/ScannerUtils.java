package ProxyPattern.utils;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Supplier;

public class ScannerUtils {
    private static final Scanner in = new Scanner(System.in);

    public static int inputIntegerBetween(int min, int max, String errorMessage){
        return inputNumberBetween(in::nextInt, min, max, errorMessage);
    }

    public static double inputDoubleBetween(double min, double max, String errorMessage){
        return inputNumberBetween(in::nextDouble, min, max, errorMessage);
    }

    public static <T extends Number> T inputNumberBetween(Supplier<T> nextNumberSupplier, T min, T max, String errorMessage){
        try{
            //class Supplier
            T choice = nextNumberSupplier.get();
            if(choice.doubleValue()<min.doubleValue() || choice.doubleValue() > max.doubleValue()) {
                System.err.println(errorMessage);
                return inputNumberBetween(nextNumberSupplier, min, max, errorMessage); //失敗會recursive重要input
            }
            return choice;
        }catch (InputMismatchException err){
            System.err.println(errorMessage);
            return inputNumberBetween(nextNumberSupplier, min, max, errorMessage); //失敗會recursive重要input
        }
    }

    public static String inputLine() {
        String line = in.nextLine();
        if(line.isBlank()) { //會recursive跳過空行
            return inputLine();
        }
        return line;
    }

    public static String input() {
        String line = in.next();
        if(line.isBlank()) { //會recursive跳過空行
            return inputLine();
        }
        return line;
    }
}
