package cmput301w18t09.orbid;


import android.media.Image;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

enum TaskStatus {
    REQUESTED, ACCEPTED, DECLINED, COMPLETED;
}

public class Task {
    private User requester;
    private String description;
    private String title;
    private String ID;
    private double price;
    private int status;
    private TaskStatus taskStatus;
    private Bid acceptedBid;
    private LatLng location;
    private ArrayList<Bid> bidList;
    private ArrayList<Image> photoList;

    public Task(User requester, String description, String title, double price, int status)
    {
        this.requester = requester;
        this.description = description;
        this.title = title;
        this.price = price;
        setStatus(status);

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
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getID() {
        return ID;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        switch(status) {
            case 1:
                taskStatus = taskStatus.REQUESTED;
            case 2:
                taskStatus = taskStatus.ACCEPTED;
            case 3:
                taskStatus = taskStatus.DECLINED;
            case 4:
                taskStatus = taskStatus.COMPLETED;
        }
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public ArrayList<Bid> getBidList() {
        return bidList;
    }

    public void setBidList(ArrayList<Bid> bidList) {
        this.bidList = bidList;
    }

    public ArrayList<Image> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(ArrayList<Image> photoList) {
        this.photoList = photoList;
    }

    public TaskStatus getTaskStatus() { return taskStatus; }


    public Bid getAcceptedBid() { return acceptedBid; }

    public void addBid(Bid bid) {

    }

    public void repost() {

    }

    public void addPhoto() {

    }

    public void acceptBid(int index) {
        acceptedBid = bidList.get(index);
    }

    public void declineBid(int index) {

    }

    public Boolean containsBid(Bid bid) {
        return bidList.contains(bid);
    }

    public Boolean containsPhoto(Image image) {
        return photoList.contains(image);
    }


}
