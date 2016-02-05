package andrew.cmu.edu.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Author  : KAILIANG CHEN
 * Version : 0.1
 * Date    : 1/13/16
 */
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    private int resource;

    public ToDoItemAdapter(Context context, int resource, List<ToDoItem> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout todoView;
        ToDoItem item = getItem(position);
        String taskString = item.getTask();
        Date created = item.getCreated();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String dateString = sdf.format(created);

        if (convertView == null) {
            todoView = new LinearLayout(getContext());
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, todoView, true);
        } else {
            todoView = (LinearLayout) convertView;
        }

        TextView date = (TextView)todoView.findViewById(R.id.rowDate);
        TextView task = (TextView)todoView.findViewById(R.id.row);
        date.setText(dateString);
        task.setText(taskString);

        return todoView;
    }
}
