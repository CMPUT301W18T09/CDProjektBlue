package cmput301w18t09.orbid;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * A model representation of a task made by a task provider and to be completed by a task provider.
 *
 * @author Zach Redfern, Ceegan Hale, Chady Haidar
 * @see Bid
 */
public class Task {
    private String requester;
    private String description;
    private String title;

    @JestId
    private String ID;
    private double price;
    private TaskStatus status;
    private int acceptedBid;
    private LatLng location;
    private ArrayList<Bid> bidList;
    private Boolean shouldNotify = false;
    private ArrayList<byte[]> photoList;

    public enum TaskStatus {
        REQUESTED, BIDDED, ASSIGNED, COMPLETED;
    }

    /**
     * Task class constructor
     *
     * @param requester The username of the user who requested the task
     * @param description The textual description of the task
     * @param title The title of the task
     * @param price The current bid price of the task
     * @param status The current status of the task (requested, bidded, assigned, completed)
     */
    public Task(String requester, String description, String title, double price, TaskStatus status) {
        this.requester = requester;
        this.description = description;
        this.title = title;
        this.price = price;
        this.bidList = new ArrayList<Bid>();
        this.photoList = new ArrayList<byte[]>();
        this.status = status;
    }

    /**
     * Gets the username of the user who requested the task
     *
     * @return The username of the user who requested the task
     */
    public String getRequester() {
        return requester;
    }

    /**
     * Sets the username of the user who requested the task
     *
     * @param requester The username of the user who requested the task
     */
    public void setRequester(String requester) {
        this.requester = requester;
    }

    /**
     * Gets the textual description of the task
     *
     * @return The textual description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the textual description of the task
     *
     * @param description The textual description of the task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the title of the task
     *
     * @return The title of the task
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title of the task
     *
     * @param title The title of the task
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the unique Elasticsearch ID
     *
     * @see DataManager
     * @return The unique Elasticsearch ID
     */
    public String getID() {
        return this.ID;
    }

    /**
     * Sets the unique Elasticsearch ID
     *
     * @see DataManager
     * @param ID The unique Elasticsearch ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Gets the current bid price of the task
     *
     * @see Bid
     * @return The current bid price of the task
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the current bid price of the task
     *
     * @see Bid
     * @param price The current bid price of the task
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the current status of the task (requested, bidded, assigned, completed)
     *
     * @return The current status of the task (requested, bidded, assigned, completed)
     */
    public TaskStatus getStatus() {
        return this.status;
    }

    /**
     * Sets the current status of the task (requested, bidded, assigned, completed)
     *
     * @param status The current status of the task (requested, bidded, assigned, completed)
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Gets the latitude and longitude (coordinates) of the task
     *
     * @return The latitude and longitude (coordinates) of the task
     */
    public LatLng getLocation() {
        return this.location;
    }

    /**
     * Sets the latitude and longitude (coordinates) of the task
     *
     * @param location The latitude and longitude (coordinates) of the task
     */
    public void setLocation(LatLng location) {
        this.location = location;
    }

    /**
     * Gets the list of bids made on the task
     *
     * @see Bid
     * @return The list of bids made on the task
     */
    public ArrayList<Bid> getBidList() {
        return this.bidList;
    }

    /**
     * Sets the list of bids made on the task
     *
     * @see Bid
     * @param bidList The list of bids made on the task
     */
    public void setBidList(ArrayList<Bid> bidList) {
        this.bidList = bidList;
    }


    /**
     * Gets the list of photos attached to the task
     *
     * @return The list of photos attached to the task
     */
    public ArrayList<Bitmap> getPhotoList() {
        ArrayList<Bitmap> list = new ArrayList<>();
        for(byte[] g : photoList) {
            list.add(BitmapFactory.decodeByteArray(g, 0, g.length));
        }
        return list;
    }

    /**
     * Sets the list of photos attached to the task
     *
     * @param photoList The list of photos attached to the task
     */
    public void setPhotoList(ArrayList<Bitmap> photoList) {
        ArrayList<byte[]> tempList = new ArrayList<>();
        for(Bitmap g:photoList) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            g.compress(Bitmap.CompressFormat.PNG, 0, stream);
            tempList.add(stream.toByteArray());
        }
        this.photoList = tempList;
    }

    /**
     * Gets the bid chosen to fulfill the task
     *
     * @see Bid
     * @return The bid chosen to fulfill the task
     */
    public int getAcceptedBid() {
        return this.acceptedBid;
    }


    /**
     * Adds a bid to the list of bids made on the task
     *
     * @see Bid
     * @param bid The bid to be made on the task
     */
    public void addBid(Bid bid) {
        this.bidList.add(bid);
    }

    /**
     * Removes a bid from the list of bids made on the task
     *
     * @see Bid
     * @param bid The bid to be removed from the task
     */
    public void removeBid(Bid bid){
        this.bidList.remove(bid);
    }

    /**
     * Adds a photo to the task
     *
     * @param image The image to be added to the task
     */
    public void addPhoto(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, stream);
        this.photoList.add(stream.toByteArray());
    }

    /**
     * Assigns/identifies the bid chosen to fulfill the task
     *
     * @see Bid
     * @param bid The bid chosen to fulfill the task
     */
    public void acceptBid(int bid) {
        acceptedBid = bid;
        status = TaskStatus.ASSIGNED;
    }

    /**
     * Sets the flag to true if a new bid was placed on this task
     *
     * @param b True if a new bid was placed on the task, false otherwise
     */
    public void setShouldNotify(Boolean b) {
        this.shouldNotify = b;
    }

    /**
     * Gets the flag that specifies if a new bid was placed on this task
     *
     * @return True if a new bid was placed on the task, false otherwise
     */
    public Boolean getShouldNotify() {
        return shouldNotify;
    }

    /**
     * Compares two tasks to see if they are equals. Only works for tasks that have not yet
     * been placed on the server and subsequently received a unique ID.
     *
     * @param t1 The first task to compare against
     * @param t2 The second task to compare against
     * @return True if the tasks are the same
     */
    public static boolean compareTasks(Task t1, Task t2) {
        if (!t1.getTitle().equals(t2.getTitle())) {
            return false;
        }
        if (!t1.getDescription().equals(t2.getDescription())) {
            return false;
        }
        if (t1.getStatus() != t2.getStatus()) {
            return false;
        }
        if (t1.getLocation() != t2.getLocation()) {
            return false;
        }
        return true;
    }

}