package cmput301w18t09.orbid;


import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * A model representation of a user of the application (requester or provider).
 *
 * @author Zach Redfern, Ceegan Hale
 * @see Review
 */
public class User {

    private String username;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String password;

    @JestId
    private String id;
    private ArrayList<Review> reviewList;

    /**
     * User class constructor
     *
     * @param username The username belonging to the user
     * @param password The password belonging to the user
     * @param email The e-mail belonging to the user
     * @param phoneNumber The phone number belonging to the user
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     */
    public User(String username, String password, String email, String phoneNumber, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.reviewList = new ArrayList<>();
    }

    /**
     * Gets the username belonging to the user
     *
     * @return The username belonging to the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username belonging to the user
     *
     * @param username The username belonging to the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password belonging to the user
     *
     * @return The password belonging to the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password belonging to the user
     *
     * @param password The username belonging to the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the e-mail belonging to the user
     *
     * @return The e-mail belonging to the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the e-mail belonging to the user
     *
     * @param email The e-mail belonging to the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number belonging to the user
     *
     * @return The phone number belonging to the user
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number belonging to the user
     *
     * @param phoneNumber The phone number belonging to the user
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the first name of the user
     *
     * @return The first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user
     *
     * @param firstName The first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user
     *
     * @return The last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user
     *
     * @param lastName The last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the unique Elasticsearch ID
     *
     * @see DataManager
     * @return The unique Elasticsearch ID
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the unique Elasticsearch ID
     *
     * @see DataManager
     * @param ID The unique Elasticsearch ID
     */
    public void setID(String ID) {
        this.id = ID;
    }

    /**
     * Gets the list of reviews associated with the user
     *
     * @see Review
     * @return The list of reviews associated with the user
     */
    public ArrayList<Review> getReviewList() {
        return reviewList;
    }

    /**
     * Sets the list of reviews associated with the user
     *
     * @see Review
     * @param reviewList The list of reviews associated with the user
     */
    public void setReviewList(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }

    /**
     * Adds a review pertaining to the user (made by other users)
     *
     * @see Review
     * @param review
     */
    public void addReview(Review review)
    {
        reviewList.add(review);
    }
}