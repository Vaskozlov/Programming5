import application.Application;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String filename = System.getenv("DATABASE_PATH");
        Application application = new Application();

        application.start(filename);
    }
}