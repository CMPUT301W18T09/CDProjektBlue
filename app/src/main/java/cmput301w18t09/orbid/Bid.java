package cmput301w18t09.orbid;


public class Bid {

    private String provider;
    private double price;
    private String description;

    public Bid(String provider, double price, String description)
    {
        this.provider = provider;
        this.price = price;
        this.description = description;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}