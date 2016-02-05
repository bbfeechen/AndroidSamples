package andrew.cmu.edu.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author  : KAILIANG CHEN
 * Version : 0.1
 * Date    : 1/13/16
 */
public class ToDoItem {
    String task;
    Date created;

    public String getTask() {
        return task;
    }

    public Date getCreated() {
        return created;
    }

    public ToDoItem(String _task) {
        this(_task, new Date(System.currentTimeMillis()));
    }

    public ToDoItem(String _task, Date _created) {
        task = _task;
        created = _created;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String dateString = sdf.format(created);
        return "(" + dateString + ") " + task;
    }
}
