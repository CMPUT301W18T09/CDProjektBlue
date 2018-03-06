package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;



public class TaskListAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private Context context;
    private ArrayList<Task> taskList;
    private ItemClickListener clickListener;
    private int type;

    public TaskListAdapter(final Context context, ArrayList<Task> taskList, int type) {
        this.context = context;
        this.taskList = taskList;
        // 0 for recent listings, 1 for my requested
        this.type = type;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.layout_task_card, parent, false);
        return new TaskViewHolder(view, this.context, type);
    }


    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        holder.setClickListener(clickListener);
        Task task = taskList.get(position);
//        holder.task_image.setImage(task.getPhotoList());
        holder.task_title.setText(task.getTitle());
        holder.task_description.setText(task.getDescription());

    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

}

