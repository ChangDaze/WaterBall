public class TelecomDisconnectCommand implements Command{
    private final Telecom telecom;

    public TelecomDisconnectCommand(Telecom telecom) {
        this.telecom = telecom;
    }

    @Override
    public void execute() {
        telecom.disconnect();
    }

    @Override
    public void undo() {
        telecom.connect();
    }

    @Override
    public String getCommandName() {
        return "DisconnectTelecom";
    }
}
