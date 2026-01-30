package ella;
<<<<<<< HEAD

import java.util.Scanner;

/**
 * Handles all interactions with the user via the command line.
 */
=======
import java.util.Scanner;

>>>>>>> branch-Level-9
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner = new Scanner(System.in);

<<<<<<< HEAD
    /**
     * Shows the startup greeting and logo.
     */
=======
>>>>>>> branch-Level-9
    public void showWelcome() {
        String logo =
                "EEEEEE  L      L        AAA   \n"
              + "E       L      L       A   A  \n"
              + "EEEEE   L      L       AAAAA  \n"
              + "E       L      L       A   A  \n"
              + "EEEEEE  LLLLL  LLLLL   A   A  \n";
        System.out.println(logo);
        showBox("Hello! I'm Ella\nWhat can I do for you?");
    }

<<<<<<< HEAD
    /**
     * Reads a command line from the user.
     *
     * @return The trimmed command string, or null if input is closed.
     */
=======
>>>>>>> branch-Level-9
    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

<<<<<<< HEAD
    /**
     * Shows a message wrapped by divider lines.
     *
     * @param message The message to display.
     */
=======
>>>>>>> branch-Level-9
    public void showBox(String message) {
        System.out.println(LINE);
        for (String line : message.split("\n")) {
            System.out.println(" " + line);
        }
        System.out.println(LINE);
    }

<<<<<<< HEAD
    /**
     * Shows a warning message when loading saved data fails.
     */
=======
>>>>>>> branch-Level-9
    public void showLoadingError() {
        showBox("Warning: Could not load saved tasks. Starting fresh.");
    }

<<<<<<< HEAD
    /**
     * Shows an error message to the user.
     *
     * @param message The error message.
     */
=======
>>>>>>> branch-Level-9
    public void showError(String message) {
        showBox("Oops! " + message);
    }
}
