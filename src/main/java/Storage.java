import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path path;

    public Storage() {
        this.path = Paths.get("data", "duke.txt");
    }

    public Storage(String filePath) {
        this.path = Paths.get(filePath);
    }

    /** Loads tasks from disk. If file/folder doesn't exist, returns empty list. */
    public List<String> loadLines() throws IOException {
        ensureParentFolderExists();
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        return Files.readAllLines(path);
    }

    /** Saves lines to disk, overwriting existing file content. */
    public void saveLines(List<String> lines) throws IOException {
        ensureParentFolderExists();
        Files.write(path, lines);
    }

    private void ensureParentFolderExists() throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
