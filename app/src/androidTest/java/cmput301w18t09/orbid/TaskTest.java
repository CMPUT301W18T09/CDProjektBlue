//package cmput301w18t09.orbid;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.Image;
//import android.test.ActivityInstrumentationTestCase2;
//
//import java.io.IOException;
//
//import static org.junit.Assert.*;
//
//
///**
// * Created by Chady on 2018-02-15.
// */
//
//public class TaskTest extends ActivityInstrumentationTestCase2 {
//
//    public TaskTest() {
//        super(Task.class);
//    }
//
//    public void testAddBid() {
//        User testUser = new User("CoolGuy123", "coolguy@hotmail.com", "123-123-5678", "Cool", "Guy");
//        Bid testBid = new Bid(testUser, 3.14, "I have a GrassCutter3000, so I can do it quickly." );
//        Task testTask = new Task(testUser, "test", "test", 3.1, Task.TaskStatus.REQUESTED);
//        testTask.addBid(testBid);
//        assertTrue(testTask.getBidList().contains(testBid));
//    }
//
//    public void testRepost() {
//        User testUser = new User("CoolGuy123", "coolguy@hotmail.com", "123-123-5678", "Cool", "Guy");
//        Task testTask = new Task(testUser, "test", "test", 3.1, Task.TaskStatus.ASSIGNED);
//        assertEquals(testTask.getStatus(), Task.TaskStatus.ASSIGNED);
//        testTask.repost();
//        assertEquals(testTask.getStatus(), Task.TaskStatus.REQUESTED);
//    }
//
//    public void testAddPhoto() {
//        User testUser = new User("CoolGuy123", "coolguy@hotmail.com", "123-123-5678", "Cool", "Guy");
//        Task testTask = new Task(testUser, "test", "test", 3.1, Task.TaskStatus.REQUESTED);
//
//        // Sample image
//        Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
//        testTask.addPhoto(image);
//        assertTrue(testTask.getPhotoList().contains(image));
//    }
//
//    public void testContainsPhoto() {
//        User testUser = new User("CoolGuy123", "coolguy@hotmail.com", "123-123-5678", "Cool", "Guy");
//        Task testTask = new Task(testUser, "test", "test", 3.1, Task.TaskStatus.REQUESTED);
//
//        // Sample image
//        Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
//        testTask.addPhoto(image);
//        assertTrue(testTask.containsPhoto(image));
//    }
//
//    public void testContainsBid() {
//        User testUser = new User("CoolGuy123", "coolguy@hotmail.com", "123-123-5678", "Cool", "Guy");
//        Bid testBid = new Bid(testUser, 3.14, "I have a GrassCutter3000, so I can do it quickly." );
//        Task testTask = new Task(testUser, "test", "test", 3.1, Task.TaskStatus.REQUESTED);
//        testTask.addBid(testBid);
//        assertTrue(testTask.getBidList().contains(testBid));
//    }
//
//    public void testAcceptBid() {
//        User testUser = new User("CoolGuy123", "coolguy@hotmail.com", "123-123-5678", "Cool", "Guy");
//        Bid testBid = new Bid(testUser, 3.14, "I have a GrassCutter3000, so I can do it quickly." );
//        Task testTask = new Task(testUser, "test", "test", 3.1, Task.TaskStatus.REQUESTED);
//        testTask.addBid(testBid);
//        testTask.acceptBid(0);
//        assertTrue(testTask.getAcceptedBid() == testBid);
//        assertEquals(testTask.getStatus(), Task.TaskStatus.ASSIGNED);
//    }
//
//    public void testDeclineBid() {
//        User testUser = new User("CoolGuy123", "coolguy@hotmail.com", "123-123-5678", "Cool", "Guy");
//        Bid testBid = new Bid(testUser, 3.14, "I have a GrassCutter3000, so I can do it quickly." );
//        Task testTask = new Task(testUser, "test", "test", 3.1, Task.TaskStatus.REQUESTED);
//        testTask.addBid(testBid);
//        testTask.declineBid(0);
//        assertFalse(testTask.containsBid(testBid));
//    }
//
//
//}