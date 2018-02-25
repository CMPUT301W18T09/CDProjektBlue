package cmput301w18t09.orbid;

import android.media.Image;

import static org.junit.Assert.*;


/**
 * Created by Chady on 2018-02-15.
 */

public class TaskTest {

    public TaskTest() {
    }

    public void testAddBid() {
        User testUser = new User("Test", "Test", "Test", "Test", "test");
        Bid testBid = new Bid(testUser, 3.14, "test" );
        Task testTask = new Task(testUser, "test", "test", 3.1, 5);
        testTask.addBid(testBid);
        assertTrue(testTask.containsBid(testBid));
    }

    public void testRepost() {
        User testUser = new User("Test", "Test", "Test", "Test", "test");
        Bid testBid = new Bid(testUser, 3.14, "test" );
        Task testTask = new Task(testUser, "test", "test", 3.1, 4);
        assertTrue(testTask.getTaskStatus() == TaskStatus.COMPLETED);
        testTask.repost();
        assertTrue(testTask.getTaskStatus() == TaskStatus.REQUESTED);
    }

    public void testAddPhoto() {
        User testUser = new User("Test", "Test", "Test", "Test", "test");
        Bid testBid = new Bid(testUser, 3.14, "test" );
        Task testTask = new Task(testUser, "test", "test", 3.1, 5);
        //Image cannot be instantiated, so unit test won't function until implementation of the method
        Image testImage = new Image();
        testTask.addPhoto(testImage);
        assertTrue(testTask.containsPhoto(testImage));
    }

    public void testAcceptBid() {
        User testUser = new User("Test", "Test", "Test", "Test", "test");
        Bid testBid = new Bid(testUser, 3.14, "test" );
        Task testTask = new Task(testUser, "test", "test", 3.1, 5);
        testTask.addBid(testBid);
        testTask.acceptBid(0);
        assertTrue(testTask.getAcceptedBid() == testBid);
    }

    public void testDeclineBid() {
        User testUser = new User("Test", "Test", "Test", "Test", "test");
        Bid testBid = new Bid(testUser, 3.14, "test" );
        Task testTask = new Task(testUser, "test", "test", 3.1, 5);
        testTask.addBid(testBid);
        testTask.declineBid(0);
        assertTrue(testTask.containsBid(testBid));
    }
}