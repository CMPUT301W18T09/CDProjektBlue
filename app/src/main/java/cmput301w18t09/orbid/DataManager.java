package cmput301w18t09.orbid;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Handles all communication between the application and the server
 *
 * @author Ceegan Hale, Zach Redfern, David Laycock
 * @see User
 * @see Task
 */

public class DataManager {

    private static JestDroidClient client;
    public static ArrayList<Task> backupTasks = new ArrayList<>();
    private static ArrayList<Task> cachedTasks;
    private static final String tasksFile = "taskList.sav";
    private static Context context;

    /**
     * Inserts new tasks into the database
     */
    public static class addTasks extends AsyncTask<Task, Void, Void> {

        public addTasks(Context cont){
            if (context == null){
                context = cont;
            }
        }

        /**
         * Inserts new tasks into the database
         *
         * @see addTasks
         * @param tasks A list of tasks to add to the date base
         * @return no return
         */
        @Override
        protected Void doInBackground(Task... tasks){
            verifySettings();

            for (Task task: tasks){
                Index index = new Index.Builder(task).index("cmput301w18t09").type("task").build();
                try{
                    DocumentResult result = client.execute(index);

                    if (result.isSucceeded()){
                        task.setID(result.getId());
                        Log.v("id", task.getID());
                        pushCached();
                    }
                    else{
                        cachedTasks.add(task);
                        saveInFile();
                    }
                }
                catch (Exception e){
                    Log.e("Error", "The application has failed to build and send the task");
                    cachedTasks.add(task);
                    saveInFile();
                }
            }

            return null;
        }
    }

    /**
     * Inserts new Users into the database
     */
    public static class addUsers extends AsyncTask<User, Void, Void> {

        public addUsers(Context cont){
            if (context == null){
                context = cont;
            }
        }

        /**
         * Inserts new Users into the database
         *
         * @see addUsers
         * @param users A list of new users to be added
         * @return no return
         */
        @Override
        protected Void doInBackground(User... users){
            verifySettings();

            for (User user: users){
                user.setPassword(encrpytion(user.getPassword()));
                Index index = new Index.Builder(user).index("cmput301w18t09").type("user").build();

                try{
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()){
                        user.setID(result.getId());
                        Log.v("id", user.getID());
                    }
                }
                catch (Exception e){
                    Log.e("Error", "The application has failed to build and send the task");
                }
            }
            return null;
        }
    }

    /**
     * Gets a list of tasks based on the search parameters
     */
    public static class getTasks extends AsyncTask<ArrayList<String>, Void, ArrayList<Task>>{

        public getTasks(Context cont){
            if (context == null){
                context = cont;
            }
        }

        /**
         * Gets a list of tasks based on the search parameters
         *
         * @see getTasks
         * @param passed An array list of query parameters in key, value sequence i.e
         *               {key, value, key, value, ...}
         * @return An array list of tasks that matched the query parameters
         */
        @Override
        protected ArrayList<Task> doInBackground(ArrayList<String>... passed){
            verifySettings();
            ArrayList<Task> tasks = new ArrayList<>();
            ArrayList<String> search_Parameters = passed[0];

            BoolQueryBuilder query = QueryBuilders.boolQuery();

            for (int x=1; x < search_Parameters.size(); x+=2){
                if (search_Parameters.get(0).equals("and")){
                    if (search_Parameters.get(x).contains("bidList")){
                        query.must(QueryBuilders.termQuery(search_Parameters.get(x), search_Parameters.get(x+1)));
                    }
                    else{
                        query.must(QueryBuilders.matchQuery(search_Parameters.get(x), search_Parameters.get(x+1)));
                    }
                }
                else {
                    if (search_Parameters.get(x).contains("bidList")){
                        query.should(QueryBuilders.termQuery(search_Parameters.get(x), search_Parameters.get(x+1)));
                    }
                    else{
                        query.should(QueryBuilders.matchQuery(search_Parameters.get(x), search_Parameters.get(x+1)));
                    }
                }

            }

            //David modified the next line to change the returned query size, hopefully this doesn't break anything else
            Search search = new Search.Builder(new SearchSourceBuilder().size(1000).query(query).toString()).addIndex("cmput301w18t09").addType("task").build();
            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){
                    List<Task> foundTasks = result.getSourceAsObjectList(Task.class);
                    tasks.addAll(foundTasks);
                    pushCached();
                }
                else {
                    Log.e("Error", "Search query failed");
                }
            }
            catch (Exception e){
                Log.e("Error", "Failed to communicate to elastic search server");
            }

            Collections.reverse(tasks);
            return tasks;
        }
    }

    /**
     * Gets a list of users based on the search parameters
     */
    public static class getUsers extends AsyncTask<ArrayList<String>, Void, ArrayList<User>>{

        public getUsers(Context cont){
            if (context == null){
                context = cont;
            }
        }

        /**
         * Gets a list of users based on the search parameters
         *
         * @see getUsers
         * @param passed An array list of query parameters in key, value sequence i.e
         *               {key, value, key, value, ...}
         * @return An array list of users that matched the query parameters
         */
        @Override
        protected ArrayList<User> doInBackground(ArrayList<String>... passed){
            verifySettings();
            ArrayList<User> users = new ArrayList<>();
            ArrayList<String> search_Parameters = passed[0];

            BoolQueryBuilder query = QueryBuilders.boolQuery();
            for (int x=0; x < search_Parameters.size(); x+=2){
                if (search_Parameters.get(x).equals("password")){
                    search_Parameters.set(x+1, encrpytion(search_Parameters.get(x+1)));
                }
                query.must(QueryBuilders.matchQuery(search_Parameters.get(x), search_Parameters.get(x+1)));
            }

            Search search = new Search.Builder(new SearchSourceBuilder().size(1).query(query).toString()).addIndex("cmput301w18t09").addType("user").build();
            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){
                    List<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                }
                else {
                    Log.e("Error", "Search query failed");
                }
            }
            catch (Exception e){
                Log.e("Error", "Failed to communicate to elastic search server");
            }

            return users;
        }
    }

    /**
     * updates tasks in the database
     */
    public static class updateTasks extends AsyncTask<ArrayList<Task>, Void, Void>{

        public updateTasks(Context cont){
            if (context == null){
                context = cont;
            }
        }

        /**
         * @see updateTasks
         * @param passed An array list of tasks to update
         * @return no return
         */
        @Override
        protected Void doInBackground(ArrayList<Task>... passed){
            verifySettings();
            ArrayList<Task> tasks = passed[0];

            for (Task task: tasks){
                try {
                    DocumentResult result = client.execute(new Index.Builder(task).index("cmput301w18t09").type("task").id(task.getID()).build());

                    if (result.isSucceeded()){
                        Log.v("Success", "Task has been updated");
                        pushCached();
                    }
                    else{
                        Log.e("Error", "Update has failed");
                        cachedTasks.add(task);
                        saveInFile();
                    }

                }
                catch (Exception e){
                    Log.e("Error", "Failed to connect to the elastic search server");
                    cachedTasks.add(task);
                    saveInFile();
                }
            }

            return null;
        }
    }

    /**
     * Updates a task when it has no ID. Functions exactly like regular update task, only
     * this searches the cachedTasks list for a match of the task before update and replaces
     * it with the after update version. Class is used to resolve a special case where tasks are
     * added to the cached list twice.
     */
    public static class updateTaskNoID extends AsyncTask<ArrayList<Task>, Void, Void>{

        private Task oldTask;

        public updateTaskNoID(Context cont, Task oldTask){
            this.oldTask = oldTask;
            if (context == null){
                context = cont;
            }
        }

        /**
         * @see updateTasks
         * @param passed An array list of tasks to update
         * @return no return
         */
        @Override
        protected Void doInBackground(ArrayList<Task>... passed){
            verifySettings();
            ArrayList<Task> tasks = passed[0];

            for (Task task: tasks){
                try {
                    DocumentResult result = client.execute(new Index.Builder(task).index("cmput301w18t09").type("task").id(task.getID()).build());

                    if (result.isSucceeded()){
                        Log.v("Success", "Task has been updated");
                        pushCached();
                    }
                    else{
                        Log.e("Error", "Update has failed");
                        cachedTasks.add(task);
                        saveInFile();
                    }

                }
                catch (Exception e){
                    Log.e("Error", "Failed to connect to the elastic search server");

                    for (int i = 0; i < cachedTasks.size(); ++i) {
                        if (Task.compareTasks(oldTask, cachedTasks.get(i))) {
                            cachedTasks.set(i, task);
                            saveInFile();
                            break;
                        }
                    }
                }
            }

            return null;
        }
    }

    /**
     * updates users in the database
     */
    public static class updateUsers extends AsyncTask<ArrayList<User>, Void, Void>{

        public updateUsers(Context cont){
            if (context == null){
                context = cont;
            }
        }

        /**
         * @see updateUsers
         * @param passed An array list of tasks to update
         * @return no return
         */
        @Override
        protected Void doInBackground(ArrayList<User>... passed){
            verifySettings();
            ArrayList<User> users = passed[0];

            for (User user: users){
                if (!StringUtils.isNumeric(user.getPassword())){
                    user.setPassword(encrpytion(user.getPassword()));
                }
                try {
                    DocumentResult result = client.execute(new Index.Builder(user).index("cmput301w18t09").type("user").id(user.getID()).build());

                    if (result.isSucceeded()){
                        Log.v("Success", "User has been updated");
                    }
                    else{
                        Log.e("Error", "User update has failed");
                    }

                }
                catch (Exception e){
                    Log.e("Error", "Failed to connect to the elastic search server");
                }
            }

            return null;
        }
    }

    /**
     * Deletes tasks from the database
     */
    public static class deleteTasks extends AsyncTask<ArrayList<String>, Void, Void>{

        public deleteTasks(Context cont){
            if (context == null){
                context = cont;
            }
        }

        /**
         * @see deleteTasks
         * @param passed An array list of task IDs
         * @return no return
         */
        @Override
        protected Void doInBackground(ArrayList<String>... passed){
            verifySettings();
            ArrayList<String> taskIDs = passed[0];

            for (String ID: taskIDs){
                try {
                    DocumentResult result = client.execute(new Delete.Builder(ID).index("cmput301w18t09").type("task").build());

                    if (result.isSucceeded()){
                        Log.v("Success", "Task has been Deleted");
                        pushCached();
                    }
                    else{
                        Log.e("Error", "Task delete has failed");
                    }

                }
                catch (Exception e){
                    Log.e("Error", "Failed to connect to the elastic search server");
                }
            }

            return null;
        }
    }

    /**
     * Deletes users from the database
     */
    public static class deleteUsers extends AsyncTask<ArrayList<String>, Void, Void>{

        public deleteUsers(Context cont){
            if (context == null){
                context = cont;
            }
        }

        /**
         * Deletes users from the database
         *
         * @see deleteUsers
         * @param passed An array list of user IDs
         * @return no return
         */
        @Override
        protected Void doInBackground(ArrayList<String>... passed){
            verifySettings();
            ArrayList<String> userIDs = passed[0];

            for (String ID: userIDs){
                try {
                    DocumentResult result = client.execute(new Delete.Builder(ID).index("cmput301w18t09").type("user").build());

                    if (result.isSucceeded()){
                        Log.v("Success", "User has been deleted");
                    }
                    else{
                        Log.e("Error", "Delete has failed");
                    }

                }
                catch (Exception e){
                    Log.e("Error", "Failed to connect to the elastic search server");
                }
            }

            return null;
        }
    }

    public static class NotificationChecker {
        private Handler timerHandler = new Handler();
        private ArrayList<Task> taskList;
        private Boolean shouldSendNotification = false;
        private Context context;
        private NotificationCompat.Builder mBuilder;
        private NotificationManagerCompat notificationManager;
        private NotificationManager manager;
        private Boolean shouldContinue = true;
        private static final String NOTIFICATION_CHANNEL_ID = "4031";
        private String channelName = "Channel";



        public NotificationChecker(Context context) {
            this.context = context;
            notificationInit();
            timerRunnable.run();
        }

        /**
         * Tells the runnable method to run or not
         * @param shouldContinue
         */
        public void setShouldContinue(Boolean shouldContinue) {
            this.shouldContinue = shouldContinue;
        }

        /**
         * Runnable that runs every 5 seconds to check for notifications
         */
        Runnable timerRunnable = new Runnable() {
            public void run() {
                if(shouldContinue && isNetworkAvailable(context)) {
                    timerHandler.postDelayed(this, 10000);
                    sendNotification();
                }
            }
        };


        /**
         * Initializes the notification
         */
        private void notificationInit() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                String description = "notificationChannel";
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW);
                channel.setDescription(description);
                // Register the channel with the system
                manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);
            }
            notificationManager = NotificationManagerCompat.from(context);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // Create the intent that brings the user to the login page
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            // Setup the notification with the proper settings
            mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSound(alarmSound) // A sound for the notification
                    .setSmallIcon(R.drawable.ic_menu_send) //notification icon
                    .setAutoCancel(true) // Set to cancel when tapped
                    .setContentTitle("Orbid") // Notification Title
                    .setContentText("You have a new Bid!") // Notification description
                    .setContentIntent(pendingIntent); // Intent to take you to my requests
            // Create the notification manager and send the notification
        }

        /**
         * Loads the tasks that have the notification flag set to true
         */
        private void loadTasks() {
            ArrayList<String> query = new ArrayList<>();
            query.add("and");
            query.add("requester");
            query.add(NavigationActivity.thisUser);
            //query.add("shouldNotify");
            //query.add("true");
            getTasks getTasks = new getTasks(context);
            getTasks.execute(query);
            try {
                taskList = getTasks.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            ArrayList<Task> removeList = new ArrayList<>();
            for(Task t: taskList) {
                if(!t.getShouldNotify()) {
                    removeList.add(t);
                } else {
                    // Sends the notification if there are any new tasks that need to notify the user
                    shouldSendNotification = true;
                }
            }
            taskList.removeAll(removeList);
            // Clear all the notification flags:
            for(Task t: taskList) {
                t.setShouldNotify(false);
            }
        }


        /**
         * Sends the notification to the user
         */
        private void sendNotification() {
            loadTasks();
            if(shouldSendNotification) {
                // Send the notification
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.notify(1, mBuilder.build());
                } else {
                    notificationManager.notify(1, mBuilder.build());
                }
                shouldSendNotification = false;
                // Update the task in DM
                ArrayList<Task> tempList = new ArrayList<>();
                for(Task t: taskList) {
                    tempList.add(t);
                }
                updateTasks updateTasks = new updateTasks(context);
                updateTasks.execute(tempList);
                // Clear the taskList
                taskList.removeAll(taskList);
            }
        }
    }

    /**
     * Verifies that the client has been established before trying to call server
     */
    private static void verifySettings(){
        if (client == null){
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();

            cachedTasks = new ArrayList<>();
        } else if (cachedTasks.isEmpty()){
            loadFromFile();
        }
    }

    /**
     * Loads cached tasks that need to be synced to the server
     */
    private static void loadFromFile(){
        try{
            Gson gson = new Gson();
            FileInputStream fis = context.openFileInput(tasksFile);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            cachedTasks = gson.fromJson(in, new TypeToken<ArrayList<Task>>(){}.getType());
            Log.i("Offline", "Cached tasks have been loaded");
        }catch (FileNotFoundException e){
            Log.e("Error", "Cached tasks file failed to open");
            e.printStackTrace();
        }
    }

    /**
     * Pushes all cached tasks to server once a successful server call has been made
     */
    private static void pushCached(){
        ArrayList<Task> pushed = new ArrayList<>();
        for (Task task: cachedTasks){
            Log.i("push", task.getTitle());
            Index index;
            if (task.getID() == null){
                index = new Index.Builder(task).index("cmput301w18t09").type("task").build();
            }
            else{
                index = new Index.Builder(task).index("cmput301w18t09").type("task").id(task.getID()).build();
            }

            try{
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()){
                    if (task.getID() == null) {
                        task.setID(result.getId());
                        Log.v("id", task.getID());
                    }
                    pushed.add(task);
                }

            }
            catch (Exception e){
                Log.e("Error", "The application has failed to communicate with the server");
            }
        }
        cachedTasks.removeAll(pushed);
        saveInFile();
        Log.i("Offline", "cached tasks have been pushed to server");
    }

    /**
     * Saves the cached tasks to a file for better offline behaviour
     */
    private static void saveInFile(){

        try{
            FileOutputStream fos = context.openFileOutput(tasksFile, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(cachedTasks, writer);
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        Log.i("Offline", "cached tasks have been stored");
    }

    /**
     * Tests if the device has an available internet connection by pinging a server
     * @return True if the device has internet connection, false otherwise
     */
    public static boolean isNetworkAvailable(Context context, String...username) {
        verifySettings();
        //David modified the next line to change the returned query size, hopefully this doesn't break anything else
        getUsers getUsers = new getUsers(context);
        ArrayList<String> query = new ArrayList<>();
        query.add("username");
        if(username.length > 0) {
            query.add(username[0]);
        } else {
            query.add(NavigationActivity.thisUser);
        }
        ArrayList<User> result = new ArrayList<>();
        try{
            result = getUsers.execute(query).get();
        }catch (Exception e){
        }
        System.out.println(result.size());
        return !result.isEmpty();
       /* // ICMP
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;*/
    }

    public static String encrpytion(String pass){
        String encrypt = "";
        for(int x = 0; x < pass.length(); x++){
            encrypt += pass.charAt(x) + 4;
        }
        return encrypt;
    }

}
