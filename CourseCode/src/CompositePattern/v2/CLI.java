package CompositePattern.v2;

import java.util.Scanner;

public class CLI {
    private final Scanner in = new Scanner(System.in);
    private Directory current;//當前目錄

    public CLI(Directory current){
        this.current = current;
    }

    public void start(){
        while (true){
            System.out.printf("%s> ", current.getName());
            String command = in.nextLine();
            executeCommand(command);
        }
    }

    private void executeCommand(String command) {
        String[] parts = command.split(" ");
        if("cd".equals(parts[0])) {
            changeDirectory(parts[1]);
        } else if("size".equals(parts[0])) {
            size(parts[1]);
        }else {
            System.err.println("Unrecognizable Command.");
        }
    }

    private void size(String name) {
        Item item = current.getItem(name);
        if(item == null) {
            System.err.printf("Can't find the item '%s'.%n",name);
        }else {
            System.out.printf("Size: %dB%n", item.bytes());
        }
    }

    private void changeDirectory(String name) {
        if("..".equals(name)) {
            current = current.getParent() == null ? current : current.getParent();
        }else {

            //型別不符：如果 getItem(name) 實際回傳的是一個 File 物件，但你強行把它轉成 Directory，Java 會噴出 java.lang.ClassCastException。
            Directory target = (Directory)current.getItem(name);
            if (target == null) {
                System.err.printf("Can't find the item '%s'.%n",name);
            } else {
                current = target;
            }
        }
    }
}
