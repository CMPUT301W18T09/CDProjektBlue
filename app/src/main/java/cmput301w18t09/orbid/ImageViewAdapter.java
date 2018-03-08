package cmput301w18t09.orbid;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Adapter class for the Recycler view to display images
 */
public class ImageViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Bitmap> imageList;
    private ImageViewHolder holder = null;
    private LayoutInflater inflater;

    /**
     * Constructor for the adapter
     * @param context
     * @param imageList
     */
    public ImageViewAdapter(final Context context, ArrayList<Bitmap> imageList) {
        this.context = context;
        this.imageList = imageList;
        this.inflater = LayoutInflater.from(context);
    }


    /**
     * getView for the adapter
     * @param position
     * @param view
     * @param parent
     * @return view
     */
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_stack_view_item, parent, false);
            holder = new ImageViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.stack_image);
            view.setTag(holder);
        } else {
            holder = (ImageViewHolder) view.getTag();
        }
        holder.image.setImageBitmap(imageList.get(position));


        return view;
    }

    @Override
    public Bitmap getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Returns the size of the image list
     * @return
     */
    @Override
    public int getCount() {
        return imageList.size();
    }

}
