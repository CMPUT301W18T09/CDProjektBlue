package cmput301w18t09.orbid;

import android.os.AsyncTask;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;

/**
 * Created by Ceegan on 2018-02-23.
 */

public class DataManager {
    private static JestDroidClient client;

    public DataManager(){
        DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080/CMPUT301W18T09");
        DroidClientConfig config = builder.build();

        JestClientFactory factory = new JestClientFactory();
        factory.setDroidClientConfig(config);
        client = (JestDroidClient) factory.getObject();
    }

    public static class addObject extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... tasks){

            return null;
        }
    }

    public static class getObject extends AsyncTask<String, Void, ArrayList<Task>>{

        @Override
        protected ArrayList<Task> doInBackground(String... search_Parameters){

            return null;
        }
    }

    public static class updateObject extends AsyncTask<Task, Void, Void>{

        @Override
        protected Void doInBackground(Task... tasks){

            return null;
        }
    }

    public static boolean doesUserExits(String username){

        return false;
    }

}
