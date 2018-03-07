package cmput301w18t09.orbid;


import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;


// Todo: Repost

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
    private ArrayList<Bitmap> photoList;

    public enum TaskStatus {
        REQUESTED, BIDDED, ASSIGNED, COMPLETED;
    }

    public Task(User requester, String description, String title, double price, TaskStatus status)
    {
        this.requester = requester;
        this.description = description;
        this.title = title;
        this.price = price;
        this.bidList = new ArrayList<Bid>();
        this.photoList = new ArrayList<Bitmap>();
        this.status = status;

    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LatLng getLocation() {
        return this.location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public ArrayList<Bid> getBidList() {
        return this.bidList;
    }

    public void setBidList(ArrayList<Bid> bidList) {
        this.bidList = bidList;
    }

    public ArrayList<Bitmap> getPhotoList() {
        return this.photoList;
    }

    public void setPhotoList(ArrayList<Bitmap> photoList) {
        this.photoList = photoList;
    }


    public Bid getAcceptedBid() { return this.acceptedBid; }

    public void addBid(Bid bid) {
        this.bidList.add(bid);
    }

    public void repost() {

        // Todo: code to repost

        this.status = TaskStatus.REQUESTED;

    }

    public void addPhoto(Bitmap image) {
        this.photoList.add(image);
    }

    public void acceptBid(int index) {
        acceptedBid = bidList.get(index);
        status = TaskStatus.ASSIGNED;
    }

    public void declineBid(int index) {
        bidList.remove(index);
    }

    public Boolean containsBid(Bid bid) {
        return bidList.contains(bid);
    }

    public Boolean containsPhoto(Bitmap image) {
        return photoList.contains(image);
    }


}
