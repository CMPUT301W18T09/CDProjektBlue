package cmput301w18t09.orbid;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Handles all communication between the application and the server
 */

public class DataManager {
    private static JestDroidClient client;
    private ArrayList<Task> cachedTasks;
    private ArrayList<User> cachedUsers;

    /**
     * Inserts new tasks into the database
     *
     */
    public static class addTasks extends AsyncTask<Task, Void, Void> {

        /**
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
                    }
                    else{
                        //TODO: Offline behaviour
                    }
                }
                catch (Exception e){
                    Log.e("Error", "The application has failed to build and send the task");
                    //TODO: Offline behaviour
                }
            }

            verifySettings();
            for (Task task: tasks){
                try {
                    DocumentResult result = client.execute(new Index.Builder(task).index("cmput301w18t09").type("task").id(task.getID()).build());

                    if (result.isSucceeded()){
                        Log.v("Success", "Task has been updated");
                    }
                    else{
                        Log.e("Error", "Update has failed");
                        //TODO: Offline behaviour
                    }

                }
                catch (Exception e){
                    Log.e("Error", "Failed to connect to the elastic search server");
                    //TODO: Offline behaviour
                }
            }

            return null;
        }
    }

    /**
     * Inserts new Users into the database
     */
    public static class addUsers extends AsyncTask<User, Void, Void> {

        /**
         * @see addUsers
         * @param users A list of new users to be added
         * @return no return
         */
        @Override
        protected Void doInBackground(User... users){
            verifySettings();

            for (User user: users){
                Index index = new Index.Builder(user).index("cmput301w18t09").type("user").build();

                try{
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()){
                        user.setID(result.getId());
                        Log.v("id", user.getID());
                    }
                    else{
                        //TODO: Offline behaviour
                    }
                }
                catch (Exception e){
                    Log.e("Error", "The application has failed to build and send the task");
                    //TODO: Offline behaviour
                }
            }

            verifySettings();
            for (User user: users){
                try {
                    DocumentResult result = client.execute(new Index.Builder(user).index("cmput301w18t09").type("user").id(user.getID()).build());

                    if (result.isSucceeded()){
                        Log.v("Success", "User has been updated");
                    }
                    else{
                        Log.e("Error", "Update has failed");
                        //TODO: Offline behavior
                    }

                }
                catch (Exception e){
                    Log.e("Error", "Failed to connect to the elastic search server");
                    //TODO: Offline behaviour
                }
            }

            return null;
        }
    }

    /**
     * gets a list of tasks based on the search parameters
     */
    public static class getTasks extends AsyncTask<ArrayList<String>, Void, ArrayList<Task>>{

        /**
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

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            for (int x=0; x < search_Parameters.size(); x+=2){
                searchSourceBuilder.query(QueryBuilders.matchQuery(search_Parameters.get(x), search_Parameters.get(x+1)));
            }

            Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("cmput301w18t09").addType("task").build();
            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){
                    List<Task> foundTasks = result.getSourceAsObjectList(Task.class);
                    tasks.addAll(foundTasks);
                }
                else {
                    Log.e("Error", "Search query failed");
                    //TODO: Offline behaviour
                }
            }
            catch (Exception e){
                Log.e("Error", "Failed to communicate to elastic search server");
                e.printStackTrace();
                //TODO: Offline behaviour
            }

            return tasks;
        }
    }

    /**
     * gets a list of users based on the search parameters
     */
    public static class getUsers extends AsyncTask<ArrayList<String>, Void, ArrayList<User>>{

        /**
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

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            for (int x=0; x < search_Parameters.size(); x+=2){
                searchSourceBuilder.query(QueryBuilders.matchQuery(search_Parameters.get(x), search_Parameters.get(x+1)));
            }

            Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("cmput301w18t09").addType("user").build();
            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){
                    List<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                }
                else {
                    Log.e("Error", "Search query failed");
                    //TODO: Offline behaviour
                }
            }
            catch (Exception e){
                Log.e("Error", "Failed to communicate to elastic search server");
                e.printStackTrace();
                //TODO: Offline behaviour
            }

            return users;
        }
    }

    /**
     * updates tasks in the database
     */
    public static class updateTasks extends AsyncTask<ArrayList<Task>, Void, Void>{

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
                    }
                    else{
                        Log.e("Error", "Update has failed");
                        //TODO: Offline behaviour
                    }

                }
                catch (Exception e){
                    Log.e("Error", "Failed to connect to the elastic search server");
                    //TODO: Offline behaviour
                }
            }

            return null;
        }
    }

    /**
     * updates users in the database
     */
    public static class updateUsers extends AsyncTask<ArrayList<User>, Void, Void>{

        /**
         * @see updateUsers
         * @param passed An array list of users to update
         * @return no return
         */
        @Override
        protected Void doInBackground(ArrayList<User>... passed){
            verifySettings();
            ArrayList<User> users = passed[0];

            for (User user: users){
                try {
                    DocumentResult result = client.execute(new Index.Builder(user).index("cmput301w18t09").type("user").id(user.getID()).build());

                    if (result.isSucceeded()){
                        Log.v("Success", "User has been updated");
                    }
                    else{
                        Log.e("Error", "Update has failed");
                        //TODO: Offline behavior
                    }

                }
                catch (Exception e){
                    Log.e("Error", "Failed to connect to the elastic search server");
                    //TODO: Offline behaviour
                }
            }

            return null;
        }
    }

    /**
     * Deletes tasks from the database
     */
    public static class deleteTasks extends AsyncTask<ArrayList<String>, Void, Void>{

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
                    }
                    else{
                        Log.e("Error", "Delete has failed");
                        //TODO: Offline behaviour
                    }

                }
                catch (Exception e){
                    Log.e("Error", "Failed to connect to the elastic search server");
                    //TODO: Offline behaviour
                }
            }

            return null;
        }
    }

    /**
     * Deletes users from the database
     */
    public static class deleteUsers extends AsyncTask<ArrayList<String>, Void, Void>{

        /**
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
                        //TODO: Offline behavior
                    }

                }
                catch (Exception e){
                    Log.e("Error", "Failed to connect to the elastic search server");
                    //TODO: Offline behaviour
                }
            }

            return null;
        }
    }

    /**
     * Verifies that the client has been established before trying to call server
     */
    public static void verifySettings(){
        if (client == null){
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
