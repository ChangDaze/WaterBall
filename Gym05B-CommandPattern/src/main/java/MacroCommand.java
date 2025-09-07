import java.util.List;
import java.util.stream.Collectors;

public class MacroCommand implements Command{
    private final List<Command> commands;
    private final String name;

    public MacroCommand(List<Command> commands) {
        this.commands = commands;
        this.name = commands.stream()
                .map(Command::getCommandName)
                .collect(Collectors.joining(" & "));
    }

    @Override
    public void execute() {
        for (var command : commands) {
            command.execute();
        }
    }

    @Override
    public void undo() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }

    @Override
    public String getCommandName() {
        return name;
    }
}
