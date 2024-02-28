import client.Application;
import exceptions.OrganizationAlreadyPresentedException;
import lib.Localization;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String filename = System.getenv("DATABASE_PATH");
        Application application = new Application();

        try {
            application.start(filename);
        } catch (OrganizationAlreadyPresentedException exception) {
            System.out.println(Localization.get("message.start.bad_file.organization_repeated_twice"));
        }
    }
}