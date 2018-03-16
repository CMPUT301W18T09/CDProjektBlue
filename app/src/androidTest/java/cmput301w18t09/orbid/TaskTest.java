package cmput301w18t09.orbid;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;


/**
 * Created by Chady on 2018-02-15.
 */

public class TaskTest extends ActivityInstrumentationTestCase2 {

    public TaskTest() {
        super(Task.class);
    }

    public void testAddBid() {
        User testUser = new User("CoolGuy123", "coolguy@hotmail.com",
                "123-123-5678", "Cool", "Guy");
        Bid testBid = new Bid("testUser", 3.14,
                "I have a GrassCutter3000, so I can do it quickly." );
        Task testTask = new Task("testUser", "test", "test",
                3.1, Task.TaskStatus.REQUESTED);
        testTask.addBid(testBid);
        assertTrue(testTask.getBidList().contains(testBid));
    }

    public void testRemoveBid() {
        User testUser = new User("CoolGuy123", "coolguy@hotmail.com",
                "123-123-5678", "Cool", "Guy");
        Bid testBid = new Bid("testUser", 3.14,
                "I have a GrassCutter3000, so I can do it quickly." );
        Task testTask = new Task("testUser", "test", "test",
                3.1, Task.TaskStatus.REQUESTED);
        testTask.addBid(testBid);
        assertTrue(testTask.getBidList().contains(testBid));
        testTask.removeBid(testBid);
        assertFalse(testTask.getBidList().contains(testBid));
    }

    public void testAddPhoto() {
        Task testTask = new Task("testUser", "test", "test", 3.1, Task.TaskStatus.REQUESTED);

        // Sample image
        Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        testTask.addPhoto(image);
        assertFalse((testTask.getPhotoList().isEmpty()));
    }

    public void testAcceptBid() {
        User testUser = new User("CoolGuy123", "coolguy@hotmail.com",
                "123-123-5678", "Cool", "Guy");
        Bid testBid = new Bid("testUser", 3.14,
                "I have a GrassCutter3000, so I can do it quickly." );
        Task testTask = new Task("testUser", "test", "test",
                3.1, Task.TaskStatus.REQUESTED);
        testTask.addBid(testBid);
        testTask.acceptBid(0);
        assertTrue((testTask.getBidList().get(testTask.getAcceptedBid()) == testBid));
        assertEquals(testTask.getStatus(), Task.TaskStatus.ASSIGNED);
    }

}
