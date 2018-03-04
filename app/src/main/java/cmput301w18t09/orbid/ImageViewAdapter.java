package cmput301w18t09.orbid;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Adapter class for the Recycler view to display images
 */
public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private Context context;
    private ArrayList<Bitmap> imageList;

    /**
     * Constructor for the adapter
     * @param context
     * @param imageList
     */
    public ImageViewAdapter(final Context context, ArrayList<Bitmap> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    /**
     * Links to the ImageViewholder class
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.image_card, parent, false);
        return new ImageViewHolder(view, this.context);
    }

    /**
     * Sets the imageView to the bitmap
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Bitmap bitmap = imageList.get(position);

        // Todo fill out bid information here
        holder.image.setImageBitmap(bitmap);


    }

    /**
     * Returns the size of the image list
     * @return
     */
    @Override
    public int getItemCount() {
        return imageList.size();
    }

}
