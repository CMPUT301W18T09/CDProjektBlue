package cmput301w18t09.orbid;

import android.test.ActivityInstrumentationTestCase2;

import static junit.framework.Assert.assertTrue;

/**
 * Created by zachredfern on 2018-02-25.
 */

@SuppressWarnings("ALL")
public class UserTest extends ActivityInstrumentationTestCase2 {

    public UserTest() {
        super(User.class);
    }

    public void testAddReview() {
        assertTrue(true);

        User user = new User("CoolGuy123", "coolguy@hotmail.com","123-123-5678", "Cool", "Guy");
        Task task = new Task("user", "Mow my lawn.", "Mow Lawn", 30.00, Task.TaskStatus.REQUESTED);
        Review review = new Review(3.5, "Paid on time.");
        user.addReview(review);
        assertTrue(user.getReviewList().contains(review));
    }
}
