import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

public class Keyboard {
    private Map<String, Command> buttonMap = new TreeMap<>();
    private Map<String, Command> buttonMapBackup;
    private final Stack<Command> doCommands = new Stack<>();
    private final Stack<Command> undoCommands = new Stack<>();

    public void setButton(String key, Command command){
        buttonMap.put(key, command);
    }

    public void resetButton() {
        buttonMapBackup = buttonMap;
        buttonMap = new Hashtable<>();
    }

    public void recoverResetButton() {
        buttonMap = buttonMapBackup;
    }

    /*
在Undo時，把doCommands最上層的指令pop出來，
並執行此指令的Undo操作。
Undo完將此指令Push到undoCommands中
 */
    public void undo() {
        if(!doCommands.isEmpty()){
            Command previousCommand = doCommands.pop();
            previousCommand.undo();
            undoCommands.push(previousCommand);
        }
    }

    /*
   而在Redo時，
   把undoCommands最上層的指令pop出來，
   呼叫此指令的execute操作，
   呼叫完之後將此指令Push到doCommands中
     */
    public void redo() {
        if(!undoCommands.isEmpty()){
            Command nextCommand = undoCommands.pop();
            nextCommand.execute();
            doCommands.push(nextCommand);
        }
    }

    public void press(String key) {
        Command command = buttonMap.get(key);
        command.execute();
        //每當執行完指令後，就把此指令push到doCommands中並清空undoCommands
        doCommands.push(command);
        undoCommands.clear();
    }

    public void printCommands() {
        buttonMap.forEach((key, command) -> {
            System.out.printf("%s: %s\n", key, command.getCommandName());
        });
    }
}
