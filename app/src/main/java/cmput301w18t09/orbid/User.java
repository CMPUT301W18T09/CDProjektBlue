package cmput301w18t09.orbid;


import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 *
 */
public class User {

    private String username;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    @JestId
    private String ID;
    private ArrayList<Review> reviewList;


    /**
     *
     * @param username
     * @param email
     * @param phoneNumber
     * @param firstName
     * @param lastName
     */
    public User(String username, String email, String phoneNumber, String firstName, String lastName)
    {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        reviewList = new ArrayList<Review>();
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return
     */
    public String getID() {
        return ID;
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
    public ArrayList<Review> getReviewList() {
        return reviewList;
    }

    /**
     *
     * @param reviewList
     */
    public void setReviewList(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }

    /**
     * 
     * @param review
     */
    public void addReview(Review review)
    {
        this.reviewList.add(review);
    }
}
