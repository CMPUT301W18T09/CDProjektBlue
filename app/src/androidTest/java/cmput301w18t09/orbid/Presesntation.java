package cmput301w18t09.orbid;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by micag on 2018-04-12.
 */

public class Presesntation extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public Presesntation(){
        super(LoginActivity.class);
    }

    public void setUp(){
        solo = new Solo(getInstrumentation(), getActivity());
    }


    /**
     * This test shows that a task that has a status of assigned is shown to the
     * task requester correctly with its status, details, and accepted bid (UC 06.02.01).
     */
    public void testPresentationData() {
        // Create a test user
        User testProvider = new User("zach36", "test", "zach@ualberta.com", "7809396963", "Zach", "Redfern");
        testProvider.addReview(new Review(1.0f, "Poor work ethic", Review.reviewType.PROVIDER_REVIEW, "bigBertaBoy"));
        User requestor = new User("ceeg", "heil", "ceegan@hale.com", "4039769234", "Ceegan", "Hale");
        User testBerta = new User("bigBertaBoy", "test", "bertaBoy@hotmail.com", "7804231897", "George", "Kosik");
        DataManager.addUsers addUsers = new DataManager.addUsers(solo.getCurrentActivity().getBaseContext());
        addUsers.execute(testProvider, requestor, testBerta);

        // Create a new task and accept a bid on it
        Task task = new Task("zach36", "I'm going on vacation without my dog.", "Looking for a dog sitter", 99.99, Task.TaskStatus.REQUESTED);
        Task task2 = new Task("coolguy123", "My toilet broke.", "Plumber requested", 99.99, Task.TaskStatus.REQUESTED);
        Task task3 = new Task("bigBertaBoy", "Skidoo engine failure, I need someone to fix this now!!", "Skidoo fix", 99.99, Task.TaskStatus.REQUESTED);
        Task task4 = new Task("johnWick780","Need vet, dog sick", "Dog sick", 99.99, Task.TaskStatus.REQUESTED);
        Task task5 = new Task("kenWong301","Need developers for new app", "Software developers needed", 99.99, Task.TaskStatus.REQUESTED);
        Task task6 = new Task("AidanSuccs", "I need help studying", "Math tutor", 99.99, Task.TaskStatus.REQUESTED);

        Task task7 = new Task("ceeg", "I need someone to mow my lawn", "Mow my lawn", 100, Task.TaskStatus.BIDDED);
        task7.addBid(new Bid("zach36", 50.0, "I can do it next week"));
        task7.addBid(new Bid("bigBertaBoy", 30.00, "I am strong"));
        DataManager.addTasks addTasks = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task, task2);

        solo.sleep(1000);
        DataManager.addTasks addTasks2 = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks2.execute(task3, task4);

        solo.sleep(1000);
        DataManager.addTasks addTasks3 = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks3.execute(task5, task6, task7);

        /*DataManager.addTasks addTasks4 = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task, task2, task3, task4, task5, task6, task7);

        // Create a new task and accept a bid on it
        /*Task task2 = new Task("Micag", "I'm going on vacation without my dog.", "Looking for a dog sitter", 99.99, Task.TaskStatus.REQUESTED);
        DataManager.addTasks addTasks2 = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task2);*/
    }
}
