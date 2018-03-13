package cmput301w18t09.orbid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FullScreenImage  extends Activity {
    private int isMyTask;
    private Task task;
    private Bitmap bitmap;
    private int pos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_screen_image);
        // Get and decote the bitmap image
        byte[] bytes = getIntent().getByteArrayExtra("BitmapImage");
        isMyTask = getIntent().getIntExtra("isMyTask",0);
        String id = getIntent().getStringExtra("_id");
        pos= getIntent().getIntExtra("position",0);

        if(isMyTask == 1) {
            Button deleteBtn = (Button) findViewById(R.id.deleteImageButton);
            deleteBtn.setVisibility(View.VISIBLE);
        }
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ImageView imgDisplay;
        imgDisplay = (ImageView) findViewById(R.id.BigImage);

        imgDisplay.setImageBitmap(bitmap);

        // Load the task
        ArrayList<Task> taskList = new ArrayList<>();
        ArrayList<String> query = new ArrayList<>();
        query.add("and");
        query.add("_id");
        query.add(id);
        DataManager.getTasks getTasks = new DataManager.getTasks(this);
        getTasks.execute(query);
        try {
            taskList = getTasks.get();
            task = taskList.get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    /**
     * Deletes the image if it's yours
     * @param view
     */
    public void deleteImage(View view) {
        ArrayList<Bitmap> temp;
        temp = task.getPhotoList();
        // Delete the bitmap from the photo list
        int prevSize = temp.size();
        temp.remove(pos);
        // Set the new photo list
        task.setPhotoList(temp);
        // Update the task
        ArrayList<Task> n = new ArrayList<>();
        n.add(task);
        DataManager.updateTasks object = new DataManager.updateTasks(this);
        object.execute(n);
        finish();
    }


}
