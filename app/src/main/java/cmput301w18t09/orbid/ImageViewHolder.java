package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by chadyhaidar on 2018-03-03.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    private Boolean isImageFitToScreen = false;

    /**
     * Image view holder which sets the onClickListener
     * @param view
     * @param context
     */
    public ImageViewHolder(View view, final Context context) {
        super(view);
        image = view.findViewById(R.id.Images);


        view.setOnClickListener(new View.OnClickListener() {
            /**
             * On click listener which handles events when user clicks an image
             * @param v
             */
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    image.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen=true;
                    image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });
    }
}
