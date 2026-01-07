package DecoratorPattern.commons;

import static java.lang.String.format;

public class Email {
    private final String username;
    private final String domain;

    public Email(String username, String domain) {
        this.username = username;
        this.domain = domain;
    }

    public String getUsername(){return username;}

    public String getDomain(){return domain;}

    @Override
    public String toString(){return format("%s@%s", username, domain);}
}
