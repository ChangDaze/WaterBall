public class TelecomConnectCommand implements Command{
    private final Telecom telecom;

    public TelecomConnectCommand(Telecom telecom) {
        this.telecom = telecom;
    }

    @Override
    public void execute() {
        telecom.connect();
    }

    @Override
    public void undo() {
        telecom.disconnect();
    }

    @Override
    public String getCommandName() {
        return "ConnectTelecom";
    }
}
