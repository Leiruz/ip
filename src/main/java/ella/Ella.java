package ella;
<<<<<<< HEAD

/**
 * The entry point of the Ella chatbot application.
 * Loads tasks from storage and runs the command loop that processes user commands.
 */
=======
>>>>>>> branch-Level-9
public class Ella {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

<<<<<<< HEAD
    /**
     * Creates an Ella instance using the given file path for persistent storage.
     *
     * @param filePath Relative path to the save file (e.g., "data/duke.txt").
     */
=======
>>>>>>> branch-Level-9
    public Ella(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loaded = new TaskList();
        try {
            for (String line : storage.loadLines()) {
<<<<<<< HEAD
                if (line.trim().isEmpty()) {
                    continue;
                }
=======
                if (line.trim().isEmpty()) continue;
>>>>>>> branch-Level-9
                loaded.add(Task.fromStorageString(line));
            }
        } catch (Exception e) {
            ui.showLoadingError();
        }
        tasks = loaded;
    }

<<<<<<< HEAD
    /**
     * Runs the main command loop: reads input, parses/executes it, and prints responses.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            if (input == null) {
                break;
            }

            try {
                String response = Parser.handle(input, tasks, storage);
                if (response != null && !response.isEmpty()) {
                    ui.showBox(response);
                }
                if (input.equalsIgnoreCase("bye")) {
                    break;
                }
=======
    public void run() {
        ui.showWelcome();
        while (true) {
            String input = ui.readCommand();
            if (input == null) break;

            try {
                String response = Parser.handle(input, tasks, storage);
                if (response != null && !response.isEmpty()) ui.showBox(response);
                if (input.equalsIgnoreCase("bye")) break;
>>>>>>> branch-Level-9
            } catch (EllaException e) {
                ui.showError(e.getMessage());
            }
        }
    }

<<<<<<< HEAD
    /**
     * Launches the application.
     *
     * @param args Command line arguments (unused).
     */
=======
>>>>>>> branch-Level-9
    public static void main(String[] args) {
        new Ella("data/duke.txt").run();
    }
}
