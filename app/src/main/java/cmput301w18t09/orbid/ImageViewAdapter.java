package cmput301w18t09.orbid;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Adapter class for the Recycler view to display images
 */
public class ImageViewAdapter extends ArrayAdapter {

    private Context context;
    private int imageLayout;
    private ArrayList<Bitmap> imageList;

    /**
     * Constructor for the adapter
     * @param context
     * @param imageList
     * @param imageLayout
     */
    public ImageViewAdapter(final Context context, ArrayList<Bitmap> imageList, int imageLayout) {
        super(context, imageLayout, imageList);
        this.context = context;
        this.imageList = imageList;
        this.imageLayout = imageLayout;
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
                    .inflate(imageLayout, parent, false);
        }

        Bitmap image = imageList.get(position);
        ImageView image_view = (ImageView) view.findViewById(R.id.stack_image);

        image_view.setImageBitmap(image);

        return view;
    }

    @Override
    public Bitmap getItem(int position) {
        return imageList.get(position);
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
