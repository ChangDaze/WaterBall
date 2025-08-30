package CommandPattern.v2;

import java.util.Stack;

public class Controller {
    private final Command[] commands = new Command[6];
    private final Stack<Command> doCommands = new Stack<>();
    private final Stack<Command> undoCommands = new Stack<>();

    public void setCommands(int button, Command command) {
        commands[button] = command;
    }

    public void press(int button) {
        if (button >= 0 && button < commands.length) {
            Command command = commands[button];
            command.execute();
            //每當執行完指令後，就把此指令push到doCommands中並清空undoCommands
            doCommands.push(command);
            undoCommands.clear();
        }else {
            throw new IllegalArgumentException("Button" + button + "unsupported.");
        }
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
}
