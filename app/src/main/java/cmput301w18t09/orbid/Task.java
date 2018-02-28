package cmput301w18t09.orbid;


import android.media.Image;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

public class Task {

    private User requester;
    private String description;
    private String title;

    @JestId
    private String ID;

    private float price;
    private int status;
    private LatLng location;
    private ArrayList<Bid> bidList;
    private ArrayList<Image> photoList;

    public Task(User requester, String description, String title, float price, int status)
    {
        this.requester = requester;
        this.description = description;
        this.title = title;
        this.price = price;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public void addBid(Bid bid)
    {

    }

    public void repost()
    {

    }

    public void addPhoto()
    {

    }

    public void acceptBid(int index)
    {

    }

    public void declineBid(int index)
    {

    }


}
