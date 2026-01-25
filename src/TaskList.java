public class TaskList {
    private static final int MAX_TASKS = 100;
    private final Task[] tasks = new Task[MAX_TASKS];
    private int size = 0;

    public int size() {
        return size;
    }

    public Task get(int index) { // 0-based
        return tasks[index];
    }

    public void add(Task task) throws EllaException {
        if (size >= MAX_TASKS) {
            throw new EllaException("I can only store up to " + MAX_TASKS + " tasks.");
        }
        tasks[size] = task;
        size++;
    }
}
