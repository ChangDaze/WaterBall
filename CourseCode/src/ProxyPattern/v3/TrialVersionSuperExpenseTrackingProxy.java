package ProxyPattern.v3;

public class TrialVersionSuperExpenseTrackingProxy extends VirtualSuperExpenseTrackingSystemProxy{
    @Override
    public void exportCSV(String filename) {
        throw new UnsupportedOperationException("CSV exporting is not supported in the trial version.");
    }
}
