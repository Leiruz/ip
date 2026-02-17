package ella;

import java.util.Scanner;

/**
 * Handles all interactions with the user via the command line.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String BOT_NAME = "Ella";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Shows the startup greeting and logo.
     */
    public void showWelcome() {
        String logo =
                "EEEEEE  L      L        AAA   \n"
              + "E       L      L       A   A  \n"
              + "EEEEE   L      L       AAAAA  \n"
              + "E       L      L       A   A  \n"
              + "EEEEEE  LLLLL  LLLLL   A   A  \n";

        System.out.println(logo);
        showBox("✨ Hey! I'm " + BOT_NAME + ".\n"
                + "I can help you keep your tasks under control.\n"
                + "Tell me what you need — I'm listening 👂");
    }

    /**
     * Reads a command line from the user.
     *
     * @return The trimmed command string, or null if input is closed.
     */
    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    /**
     * Shows a message wrapped by divider lines.
     *
     * @param message The message to display.
     */
    public void showBox(String message) {
        System.out.println(LINE);
        for (String line : message.split("\\n")) {
            System.out.println(" " + line);
        }
        System.out.println(LINE);
    }

    /**
     * Shows a warning message when loading saved data fails.
     */
    public void showLoadingError() {
        showBox("⚠️ I tried to load your saved tasks, but something went wonky.\n"
                + "No worries — we'll start fresh.");
    }

    /**
     * Shows an error message to the user.
     *
     * @param message The error message.
     */
    public void showError(String message) {
        showBox("😅 Oops! " + message + "\n"
                + "Tip: If you're stuck, try `list` to see what I know so far.");
    }
}
