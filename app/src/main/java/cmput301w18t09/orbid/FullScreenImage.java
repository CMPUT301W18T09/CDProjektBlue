package cmput301w18t09.orbid;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Shows a clicked image in a larger view by scaling it to the available screen size
 *
 * @author Chady Haidar
 */
public class FullScreenImage extends Activity {

    private int isMyTask;
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        // Get and decode the bitmap image
        byte[] bytes = getIntent().getByteArrayExtra("BitmapImage");
        isMyTask = getIntent().getIntExtra("isMyTask",0);
        String id = getIntent().getStringExtra("_id");
        //pos = getIntent().getIntExtra("position",0);

        if(isMyTask == 1) {
            Button deleteBtn = (Button) findViewById(R.id.deleteImageButton);
            deleteBtn.setVisibility(View.VISIBLE);
        }
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ImageView imgDisplay;
        imgDisplay = (ImageView) findViewById(R.id.BigImage);

        imgDisplay.setImageBitmap(bitmap);
    }

    /**
     * Deletes the image if it's yours
     *
     * @param view
     */
    public void deleteImage(View view) {
        setResult(188);
        finish();
    }
}
