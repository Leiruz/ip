package ella;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores and manages a list of tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Returns the number of tasks.
     *
     * @return Task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a task by zero-based index.
     *
     * @param index Zero-based index.
     * @return Task at index.
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "index must be within [0, size)";
        return tasks.get(index);
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        assert task != null : "task must not be null";
        tasks.add(task);
    }

    /**
     * Removes and returns a task at the given zero-based index.
     *
     * @param index Zero-based index.
     * @return Removed task.
     */
    public Task remove(int index) {
        assert index >= 0 && index < tasks.size() : "index must be within [0, size)";
        return tasks.remove(index);
    }

    /**
     * Finds tasks that contain the keyword (case-insensitive) in their display string.
     *
     * @param keyword Keyword to match.
     * @return List of matching tasks (may be empty).
     */
    public List<Task> findMatching(String keyword) {
        assert keyword != null : "keyword must not be null";

        String keyLower = keyword.toLowerCase();

        // A-Streams: stream-based filtering
        return tasks.stream()
                .filter(t -> t.toString().toLowerCase().contains(keyLower))
                .collect(Collectors.toList());
    }

    /**
     * Converts all tasks into storage lines for persistence.
     *
     * @return List of storage lines.
     */
    public List<String> toStorageLines() {
        // A-Streams: stream-based mapping
        return tasks.stream()
                .peek(t -> {
                    assert t != null : "TaskList should not contain null tasks";
                })
                .map(Task::toStorageString)
                .collect(Collectors.toList());
    }
}
