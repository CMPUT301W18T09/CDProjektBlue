package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;



public class TaskListAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private Context context;
    private ArrayList<Task> taskList;

    public TaskListAdapter(final Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.activity_recent_listings, parent, false);
        return new TaskViewHolder(view, this.context);
    }


    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.task_image.

    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

}
