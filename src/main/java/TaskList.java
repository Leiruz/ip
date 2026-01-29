import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public int size() {
        return tasks.size();
    }

    public Task get(int index) { // 0-based
        return tasks.get(index);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task remove(int index) { // 0-based
        return tasks.remove(index);
    }

    public List<String> toStorageLines() {
        ArrayList<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(t.toStorageString());
        }
        return lines;
    }
}
