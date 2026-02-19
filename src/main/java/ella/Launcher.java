package ella;

import javafx.application.Application;

/**
 * Launches the JavaFX application.
 * This indirection avoids JavaFX launcher issues when packaging/running jars.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
