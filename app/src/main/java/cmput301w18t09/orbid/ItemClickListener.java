package cmput301w18t09.orbid;

import android.view.View;

/**
 * Interface to be implemented by activities that utilize a Recycler view.
 */
public interface ItemClickListener {
    /**
     * Function to be implemented when a card in the recycler
     * view is selected.
     * @param view
     * @param position
     * @param type
     */
    void onClick(View view, int position, int type);
}
