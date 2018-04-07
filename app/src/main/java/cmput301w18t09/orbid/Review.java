package cmput301w18t09.orbid;

/**
 * A model representation of a review made for a user.
 *
 * @author CDProjektBlue
 * @see User
 */
public class Review {

    private float rating;
    private String description;
    private reviewType type;
    private String submittingUser;

    public enum reviewType {
        REQUESTOR_REVIEW, PROVIDER_REVIEW
    }

    /**
     * Review class constructor
     *
     * @param rating The user rating associated with the review
     * @param description The textual description of the review
     */
    public Review(float rating, String description, reviewType type, String submittingUser) {
        this.rating = rating;
        this.description = description;
        this.type = type;
        this.submittingUser = submittingUser;
    }

    /**
     * Returns the user who submitted the review
     *
     * @return The user who submitted the review
     */
    public String getSubmittingUser() {
        return submittingUser;
    }

    /**
     * Sets the user who is submitting the review
     *
     * @param submittingUser The user who is submitting the review
     */
    public void setSubmittingUser(String submittingUser) {
        this.submittingUser = submittingUser;
    }

    /**
     * Gets the review type.  Determines if it was created by a requestor or provider
     *
     * @return The review type associated with the review
     */
    public reviewType getType() {return type;}

    /**
     * Sets the review type. Determines if it was created by a requestor or provider
     *
     * @param type The review type associated with the review
     */
    public void setType(reviewType type) {this.type = type;}

    /**
     * Gets the user rating associated with the review
     *
     * @return The user rating associated with the review
     */
    public float getRating() {
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
