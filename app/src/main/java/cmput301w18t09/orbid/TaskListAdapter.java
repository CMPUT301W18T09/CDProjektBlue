package cmput301w18t09.orbid;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;


/**
 * Binds the task view holder with the recycler view task list
 *
 * @author Aidan Kosik
 * @see Task
 */
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
        /*if (task.getPhotoList() != null) {
            if (!task.getPhotoList().isEmpty()) {
                Log.i("IMG", task.getPhotoList().get(0).toString());
                holder.task_image.setImageBitmap();
            }
        }*/
        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}

