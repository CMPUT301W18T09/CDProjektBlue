package cmput301w18t09.orbid;

import android.content.Context;
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

    public TaskViewHolder(View view, final Context context) {
        super(view);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Timber", "NICE CLICK BRO");
            }
        });
    }
}
