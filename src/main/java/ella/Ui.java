package ella;
import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner = new Scanner(System.in);

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

    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    public void showBox(String message) {
        System.out.println(LINE);
        for (String line : message.split("\n")) {
            System.out.println(" " + line);
        }
        System.out.println(LINE);
    }

    public void showLoadingError() {
        showBox("Warning: Could not load saved tasks. Starting fresh.");
    }

    public void showError(String message) {
        showBox("Oops! " + message);
    }
}
