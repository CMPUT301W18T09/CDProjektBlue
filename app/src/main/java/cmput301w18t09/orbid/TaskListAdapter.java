package cmput301w18t09.orbid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;



public class TaskListAdapter extends ArrayAdapter<Task> {

    private Context context;
    private ArrayList<Task> taskList;

    public TaskListAdapter(Context context, ArrayList<Task> taskList)
    {
        super(context, 0, taskList);
        this.context = context;
        this.taskList = taskList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View taskItem = convertView;

        // A short example of inflating a bid item in the list

        // if(taskItem == null) {
        //    bidItem = LayoutInflater.from(context).inflate(R.layout.bid_item, parent, false);
        // }

        // Task task = taskList.get(position);
        // TextView tvDescription = (TextView) taskItem.findViewById(R.id.task_list_layout);
        // tvDescription.setText(task.getDescription());


        return taskItem;
    }

}
