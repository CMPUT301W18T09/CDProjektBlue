package cmput301w18t09.orbid;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by david on 28/03/18.
 */

public class ListReviewActivity extends NavigationActivity implements ItemClickListener {

    private ArrayList<Review> reviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(R.layout.activity_view_reviews, frameLayout);
    }

    @Override
    public void onClick(View view, int position, int type) {

    }
}
