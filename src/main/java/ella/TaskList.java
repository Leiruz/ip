package ella;

import java.util.ArrayList;
import java.util.List;

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
     * Converts all tasks into storage lines for persistence.
     *
     * @return List of storage lines.
     */
    public List<String> toStorageLines() {
        ArrayList<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            assert t != null : "TaskList should not contain null tasks";
            lines.add(t.toStorageString());
        }
        return lines;
    }
}
