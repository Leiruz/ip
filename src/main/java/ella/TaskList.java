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
        return tasks.get(index);
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns a task at the given zero-based index.
     *
     * @param index Zero-based index.
     * @return Removed task.
     */
    public Task remove(int index) {
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
            lines.add(t.toStorageString());
        }
        return lines;
    }
}
