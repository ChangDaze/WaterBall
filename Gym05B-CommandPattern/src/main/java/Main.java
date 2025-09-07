import java.util.*;

public class Main {
    public static void main(String[] args){
        Tank tank = new Tank();
        Telecom telecom = new Telecom();
        Keyboard keyboard = new Keyboard();

        Map<String, Command> settingMap = new TreeMap<>();
        settingMap.put("0", new TankMoveForwardCommand(tank));
        settingMap.put("1", new TankMoveBackwardCommand(tank));
        settingMap.put("2", new TelecomConnectCommand(telecom));
        settingMap.put("3", new TelecomDisconnectCommand(telecom));
        settingMap.put("4", new ResetCommand(keyboard));

        Scanner in = new Scanner(System.in);
        boolean keepDo = true;
        while (keepDo){
            keyboard.printCommands();
            System.out.print("(1) 快捷鍵設置 (2) Undo (3) Redo (字母) 按下按鍵: ");
            String command = in.nextLine();

            switch (command) {
                case "1":
                    System.out.print("設置巨集指令 (y/n)：");
                    String macroFlag = in.nextLine();
                    System.out.print("Key: ");
                    String key = in.nextLine();


                    if (macroFlag.equals("y")) {
                        System.out.printf("要將哪些指令設置成快捷鍵 %s 的巨集（輸入多個數字，以空白隔開）: \n", key);

                        settingMap.forEach((k, v) ->
                                System.out.printf("(%s) %s\n", k, v.getCommandName()));
                        String refCommands = in.nextLine();

                        List<Command> commands = new ArrayList<>();
                        Arrays.stream(refCommands.split(" "))
                            .forEach(cmd -> commands.add(settingMap.get(cmd)));

                        MacroCommand macroCommand = new MacroCommand(commands);

                        keyboard.setButton(key, macroCommand);
                    }else {
                        System.out.printf("要將哪一道指令設置到快捷鍵 %s 上: \n", key);
                        settingMap.forEach((k, v) ->
                                System.out.printf("(%s) %s\n", k, v.getCommandName()));
                        String refCommand = in.nextLine();
                        keyboard.setButton(key, settingMap.get(refCommand));
                    }

                    break;
                case "2":
                    keyboard.undo();
                    break;
                case "3":
                    keyboard.redo();
                    break;
                case "exit":
                    keepDo = false;
                    break;
                default:
                    keyboard.press(command);
            }
        }
    }
}
