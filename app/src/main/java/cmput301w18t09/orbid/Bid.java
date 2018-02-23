package cmput301w18t09.orbid;


public class Bid {

    private User provider;
    private float price;
    private String description;

    public Bid(User provider, float price, String description)
    {
        this.provider = provider;
        this.price = price;
        this.description = description;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
