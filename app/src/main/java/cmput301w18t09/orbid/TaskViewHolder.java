package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Inflates the card for a task in the recycler view
 *
 * Created by aidankosik on 2018-03-01.
 *
 * @author Aidan Kosik
 */
public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView taskImage;
    public TextView taskTitle;
    public TextView taskDescription;
    private ItemClickListener clickListener;
    private int type;

    public TaskViewHolder(View view, final Context context, int type) {
        super(view);
        taskImage = view.findViewById(R.id.task_image);
        taskTitle = view.findViewById(R.id.details_task_title);
        taskDescription = view.findViewById(R.id.details_task_description);
        view.setOnClickListener(this);
        this.type = type;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null) clickListener.onClick(view, getAdapterPosition(), type);
    }
}
