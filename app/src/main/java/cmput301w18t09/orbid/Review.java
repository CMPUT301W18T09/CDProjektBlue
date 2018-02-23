package cmput301w18t09.orbid;


public class Review {

    private float rating;
    private String description;

    public Review(float rating, String description)
    {
        this.rating = rating;
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
