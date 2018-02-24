package cmput301w18t09.orbid;

import java.util.ArrayList;

/**
 * Created by david on 23/02/18.
 */

public class DataManager {

    private final String url = "http://cmput301.softwareprocess.es:8080/CMPUT301W18T09/";
    private ArrayList<Task> cachedTasks;
    private ArrayList<User> cachedUsers;

    public DataManager() {}

    public void addObjects(Object T) {}

    public Object getObjects() {}

    public void updateObject(Object T) {}

    public Boolean userExists(String username) {}

    private Boolean isServerConnectable() {}
}
