package ella;

/**
 * The entry point of the Ella chatbot application.
 * Loads tasks from storage and runs the command loop that processes user commands.
 */
public class Ella {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    // If loading fails (corrupt file, invalid format, etc.), we store a warning message.
    private String startupWarning;

    /**
     * Creates an Ella instance using the given file path for persistent storage.
     *
     * @param filePath Relative path to the save file (e.g., "data/ella.txt").
     */
    public Ella(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loaded = new TaskList();
        try {
            for (String line : storage.loadLines()) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                loaded.add(Task.fromStorageString(line));
            }
            startupWarning = null;
        } catch (Exception e) {
            // Console warning (for CLI users)
            ui.showLoadingError();
            // GUI-friendly warning (for GUI users)
            startupWarning = "Warning: I couldn't load your saved tasks. Starting fresh.";
        }
        tasks = loaded;
    }

    /**
     * Runs the main command loop: reads input, parses/executes it, and prints responses.
     */
    public void run() {
        ui.showWelcome();

        if (startupWarning != null) {
            ui.showBox(startupWarning);
        }

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
            } catch (EllaException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Returns the list of tasks.
     *
     * @return TaskList object containing the tasks.
     */
    public TaskList getTasks() {
        return tasks;
    }

    /**
     * Returns the storage object.
     *
     * @return Storage object used for loading and saving tasks.
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * Returns a startup warning if loading saved data failed, otherwise null.
     *
     * @return warning message or null.
     */
    public String getStartupWarning() {
        return startupWarning;
    }

    /**
     * Launches the application (CLI).
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        new Ella(Storage.DEFAULT_SAVE_PATH).run();
    }
}
