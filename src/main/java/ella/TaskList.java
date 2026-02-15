package ella;

import java.util.ArrayList;
import java.util.Comparator;
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

        return tasks.stream()
                .filter(t -> t.toString().toLowerCase().contains(keyLower))
                .collect(Collectors.toList());
    }

    /**
     * Sorts tasks in a default sensible order:
     * 1) Not done first
     * 2) By type: Todo, Deadline, Event
     * 3) By description (case-insensitive)
     */
    public void sortDefault() {
        Comparator<Task> comparator =
                Comparator.comparing(Task::isDone) // false first (not done), true later (done)
                        .thenComparing(t -> typeRank(t.getType()))
                        .thenComparing(t -> t.getDescription().toLowerCase());

        tasks.sort(comparator);
    }

    private int typeRank(TaskType type) {
        assert type != null : "type must not be null";
        // Adjust if your TaskType names differ
        if (type == TaskType.TODO) {
            return 0;
        }
        if (type == TaskType.DEADLINE) {
            return 1;
        }
        if (type == TaskType.EVENT) {
            return 2;
        }
        return 99;
    }

    /**
     * Converts all tasks into storage lines for persistence.
     *
     * @return List of storage lines.
     */
    public List<String> toStorageLines() {
        return tasks.stream()
                .peek(t -> {
                    assert t != null : "TaskList should not contain null tasks";
                })
                .map(Task::toStorageString)
                .collect(Collectors.toList());
    }
}
