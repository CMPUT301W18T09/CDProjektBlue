package cmput301w18t09.orbid;


import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 *
 */
public class Task {
    private User requester;
    private String description;
    private String title;

    @JestId
    private String ID;
    private double price;
    private TaskStatus status;
    private Bid acceptedBid;
    private LatLng location;
    private ArrayList<Bid> bidList;
    private ArrayList<byte[]> photoList;

    // Enum
    public enum TaskStatus {
        REQUESTED, BIDDED, ASSIGNED, COMPLETED;
    }

    /**
     *
     * @param requester
     * @param description
     * @param title
     * @param price
     * @param status
     */
    public Task(User requester, String description, String title, double price, TaskStatus status)
    {
        this.requester = requester;
        this.description = description;
        this.title = title;
        this.price = price;
        this.bidList = new ArrayList<Bid>();
        //this.photoList = new ArrayList<Bitmap>();
        this.photoList = new ArrayList<byte[]>();
        this.status = status;

    }

    /**
     *
     * @return
     */
    public User getRequester() {
        return requester;
    }

    /**
     *
     * @param requester
     */
    public void setRequester(User requester) {
        this.requester = requester;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return this.title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getID() {
        return this.ID;
    }

    /**
     *
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     *
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @return
     */
    public TaskStatus getStatus() {
        return this.status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public LatLng getLocation() {
        return this.location;
    }

    /**
     *
     * @param location
     */
    public void setLocation(LatLng location) {
        this.location = location;
    }

    /**
     *
     * @return
     */
    public ArrayList<Bid> getBidList() {
        return this.bidList;
    }

    /**
     *
     * @param bidList
     */
    public void setBidList(ArrayList<Bid> bidList) {
        this.bidList = bidList;
    }


    /**
     *
     * @return
     */
    public Bid getAcceptedBid() { return this.acceptedBid; }

    /**
     *
     * @param bid
     */
    public void addBid(Bid bid) {
        this.bidList.add(bid);
    }


    /**
     *
     * @param image
     */
    public void addPhoto(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, stream);
        this.photoList.add(stream.toByteArray());
    }


    /**
     *
     * @param index
     */
    public void acceptBid(int index) {
        acceptedBid = bidList.get(index);
        status = TaskStatus.ASSIGNED;
    }

    /**
     *
     * @param index
     */
    public void declineBid(int index) {
        bidList.remove(index);
    }

    /**
     *
     * @param bid
     * @return
     */
    public Boolean containsBid(Bid bid) {
        return bidList.contains(bid);
    }




}
