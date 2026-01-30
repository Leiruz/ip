package ella;
public class Ella {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Ella(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loaded = new TaskList();
        try {
            for (String line : storage.loadLines()) {
                if (line.trim().isEmpty()) continue;
                loaded.add(Task.fromStorageString(line));
            }
        } catch (Exception e) {
            ui.showLoadingError();
        }
        tasks = loaded;
    }

    public void run() {
        ui.showWelcome();
        while (true) {
            String input = ui.readCommand();
            if (input == null) break;

            try {
                String response = Parser.handle(input, tasks, storage);
                if (response != null && !response.isEmpty()) ui.showBox(response);
                if (input.equalsIgnoreCase("bye")) break;
            } catch (EllaException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Ella("data/duke.txt").run();
    }
}
