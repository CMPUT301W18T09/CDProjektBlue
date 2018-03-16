package cmput301w18t09.orbid;


import android.util.Log;

import java.util.ArrayList;

/**
 * A model representation of a review made for a user.
 *
 * @author CDProjektBlue
 * @see User
 */
public class Review {

    private double rating;
    private String description;

    /**
     * Review class constructor
     *
     * @param rating The user rating associated with the review
     * @param description The textual description of the review
     */
    public Review(double rating, String description)
    {
        this.rating = rating;
        this.description = description;
    }

    /**
     * Gets the user rating associated with the review
     *
     * @return The user rating associated with the review
     */
    public double getRating() {
        return rating;
    }

    /**
     * Sets the user rating associated with the review
     *
     * @param rating The user rating associated with the review
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * Gets the textual description of the review
     *
     * @return The textual description of the review
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the textual description of the review
     *
     * @param description The textual description of the review
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
