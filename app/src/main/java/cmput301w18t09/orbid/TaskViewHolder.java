package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by aidankosik on 2018-03-01.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView task_image;
    public TextView task_title;
    public TextView task_description;
    private ItemClickListener clickListener;
    private Context context;
    private int type;

    public TaskViewHolder(View view, final Context context, int type) {
        super(view);
        task_image = view.findViewById(R.id.task_image);
        task_title = view.findViewById(R.id.details_task_title);
        task_description = view.findViewById(R.id.details_task_description);
        this.context = context;
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
