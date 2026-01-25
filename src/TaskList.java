import java.util.ArrayList;

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
}
