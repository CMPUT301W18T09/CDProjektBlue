package cmput301w18t09.orbid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by aidankosik on 2018-03-01.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder {

    public ImageView task_image;
    public TextView task_title;
    public TextView task_description;
    private Context context;
    private int type;

    public TaskViewHolder(View view, final Context context, int type) {
        super(view);
        task_image = view.findViewById(R.id.task_image);
        task_title = view.findViewById(R.id.task_title);
        task_description = view.findViewById(R.id.task_description);
        this.context = context;
        this.type = type;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = getAdapterPosition();
                onTaskClick(position);
            }
        });
    }

    public void onTaskClick(int position) {
        if(type == 0) {
            Intent intent = new Intent(context, TaskDetailsActivity.class);
            intent.putExtra("task_details_layout_id", R.layout.activity_task_details);
            intent.putExtra("position", position);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, AddEditTaskActivity.class);
            intent.putExtra("addedit_layout_id", R.layout.activity_add_edit_task);
            intent.putExtra("position", position);
            intent.putExtra("isAdd", 0);
            context.startActivity(intent);
        }
    }
}
