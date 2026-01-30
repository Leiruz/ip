package ella;
<<<<<<< HEAD

=======
>>>>>>> branch-Level-9
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
/**
 * Handles reading and writing task data to a file on disk.
 */
public class Storage {
    private final Path path;

    /**
     * Creates a storage component that saves to "data/duke.txt".
     */
=======
public class Storage {
    private final Path path;

>>>>>>> branch-Level-9
    public Storage() {
        this.path = Paths.get("data", "duke.txt");
    }

<<<<<<< HEAD
    /**
     * Creates a storage component that saves to a specific file path.
     *
     * @param filePath Relative file path (recommended).
     */
=======
>>>>>>> branch-Level-9
    public Storage(String filePath) {
        this.path = Paths.get(filePath);
    }

<<<<<<< HEAD
    /**
     * Loads all lines from the save file.
     * Returns an empty list if the file does not exist.
     *
     * @return List of lines from the save file.
     * @throws IOException If reading fails.
     */
=======
>>>>>>> branch-Level-9
    public List<String> loadLines() throws IOException {
        ensureParentFolderExists();
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        return Files.readAllLines(path);
    }

<<<<<<< HEAD
    /**
     * Saves the provided lines to the save file, overwriting existing content.
     *
     * @param lines The lines to write.
     * @throws IOException If writing fails.
     */
=======
>>>>>>> branch-Level-9
    public void saveLines(List<String> lines) throws IOException {
        ensureParentFolderExists();
        Files.write(path, lines);
    }

<<<<<<< HEAD
    /**
     * Ensures the parent folder (e.g., "data/") exists.
     *
     * @throws IOException If directory creation fails.
     */
=======
>>>>>>> branch-Level-9
    private void ensureParentFolderExists() throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
