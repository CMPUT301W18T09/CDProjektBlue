package cmput301w18t09.orbid;


import android.util.Log;

import java.util.ArrayList;

public class Review {

    private double rating;
    private String description;

    public Review(double rating, String description)
    {
        this.rating = rating;
        this.description = description;
    }

    public double getRating() {
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
