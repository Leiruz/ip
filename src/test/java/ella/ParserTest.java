package ella;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @TempDir
    Path tempDir;

    private Storage storageWithTempFile() {
        Path file = tempDir.resolve("ella-test.txt");
        return new Storage(file.toString());
    }

    @Test
    public void handle_emptyInput_throwsException() {
        Storage storage = storageWithTempFile();
        TaskList list = new TaskList();

        EllaException e = assertThrows(EllaException.class, () ->
                Parser.handle("   ", list, storage)
        );

        assertTrue(e.getMessage().contains("Give me something to work with"));
    }

    @Test
    public void handle_unknownCommand_throwsException() {
        Storage storage = storageWithTempFile();
        TaskList list = new TaskList();

        EllaException e = assertThrows(EllaException.class, () ->
                Parser.handle("nonsense", list, storage)
        );

        assertTrue(e.getMessage().contains("don't recognise"));
    }

    @Test
    public void todo_withoutDescription_throwsException() {
        Storage storage = storageWithTempFile();
        TaskList list = new TaskList();

        EllaException e = assertThrows(EllaException.class, () ->
                Parser.handle("todo", list, storage)
        );

        assertTrue(e.getMessage().contains("todo needs a description"));
    }

    @Test
    public void mark_withoutNumber_throwsException() {
        Storage storage = storageWithTempFile();
        TaskList list = new TaskList();

        EllaException e = assertThrows(EllaException.class, () ->
                Parser.handle("mark", list, storage)
        );

        assertTrue(e.getMessage().contains("Please provide a task number"));
        assertTrue(e.getMessage().contains("Use: mark <number>"));
    }

    @Test
    public void mark_nonNumeric_throwsException() {
        Storage storage = storageWithTempFile();
        TaskList list = new TaskList();

        EllaException e = assertThrows(EllaException.class, () ->
                Parser.handle("mark two", list, storage)
        );

        assertTrue(e.getMessage().contains("valid task number"));
    }

    @Test
    public void delete_whenNoTasks_throwsException() {
        Storage storage = storageWithTempFile();
        TaskList list = new TaskList();

        EllaException e = assertThrows(EllaException.class, () ->
                Parser.handle("delete 1", list, storage)
        );

        assertTrue(e.getMessage().toLowerCase().contains("no tasks"));
    }

    @Test
    public void deadline_missingBy_throwsException() {
        Storage storage = storageWithTempFile();
        TaskList list = new TaskList();

        EllaException e = assertThrows(EllaException.class, () ->
                Parser.handle("deadline submit report", list, storage)
        );

        assertTrue(e.getMessage().contains("Use: deadline"));
        assertTrue(e.getMessage().contains("/by"));
    }

    @Test
    public void event_missingFromTo_throwsException() {
        Storage storage = storageWithTempFile();
        TaskList list = new TaskList();

        EllaException e = assertThrows(EllaException.class, () ->
                Parser.handle("event project meeting", list, storage)
        );

        String msg = e.getMessage().toLowerCase();
        assertTrue(msg.contains("event"));
        assertTrue(msg.contains("/from"));
        assertTrue(msg.contains("/to"));
    }

    @Test
    public void addTodo_thenMark_updatesTaskAndResponse() throws Exception {
        Storage storage = storageWithTempFile();
        TaskList list = new TaskList();

        String add = Parser.handle("todo read book", list, storage);
        assertEquals(1, list.size());
        assertTrue(add.contains("Added this task"));

        String mark = Parser.handle("mark 1", list, storage);
        assertTrue(mark.contains("Marked as done"));
        assertTrue(list.get(0).toString().contains("[X]"));
    }

    @Test
    public void find_noMatches_returnsNoMatchesMessage() throws Exception {
        Storage storage = storageWithTempFile();
        TaskList list = new TaskList();

        Parser.handle("todo read book", list, storage);

        String resp = Parser.handle("find xyz", list, storage);
        assertTrue(resp.contains("couldn’t find anything matching"));
    }
}
