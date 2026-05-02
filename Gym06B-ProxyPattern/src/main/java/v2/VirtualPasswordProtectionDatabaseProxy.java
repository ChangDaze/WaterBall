package v2;

public class VirtualPasswordProtectionDatabaseProxy extends VirtualDatabaseProxy{
    private static final String REQUIRED_PASSWORD = "1qaz2wsx";

    @Override
    public Employee getEmployeeById(int id) {


        // 1. Check the Environment Variable first
        String envPassword = System.getenv("PASSWORD");

        if (envPassword == null || !envPassword.equals(REQUIRED_PASSWORD)) {
            // 2. Interrupt access if password doesn't match
            throw new SecurityException("Access Denied: Invalid or missing PASSWORD environment variable.");
        }

        return new VirtualEmployeeProxy(id, this);
    }
}
