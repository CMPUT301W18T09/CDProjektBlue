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

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Adapter class for the Recycler view to display images
 *
 * @author Chady Haidar
 */
public class ImageViewAdapter extends BaseAdapter {

    private ArrayList<Bitmap> imageList;

    /**
     * Constructor for the adapter
     *
     * @param context
     * @param imageList
     */
    public ImageViewAdapter(final Context context, ArrayList<Bitmap> imageList) {
        this.imageList = imageList;
    }

    /**
     * Get the view for the adapter
     *
     * @param position
     * @param view
     * @param parent
     * @return view
     */
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        ImageView imageView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_stack_view_item, parent, false);
            imageView = (ImageView) view.findViewById(R.id.stack_image);
            view.setTag(imageView);
        } else {
            imageView = (ImageView) view.getTag();
        }
        if (imageList.size() > 0) {
            imageView.setImageBitmap(imageList.get(position));
        }

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
     *
     * @return Integer size of the image list
     */
    @Override
    public int getCount() {
        return imageList.size();
    }

    public void updateList(ArrayList<Bitmap> b) {
        imageList.clear();
        imageList.addAll(b);
        this.notifyDataSetChanged();
    }

}
