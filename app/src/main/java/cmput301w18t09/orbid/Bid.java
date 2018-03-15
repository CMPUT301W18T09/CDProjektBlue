package cmput301w18t09.orbid;

/**
 *
 */
public class Bid {

    private User provider;
    private double price;
    private String description;

    /**
     *
     * @param provider
     * @param price
     * @param description
     */
    public Bid(User provider, double price, String description)
    {
        this.provider = provider;
        this.price = price;
        this.description = description;
    }

    /**
     *
     * @return
     */
    public User getProvider() {
        return provider;
    }

    /**
     *
     * @param provider
     */
    public void setProvider(User provider) {
        this.provider = provider;
    }

    /**
     *
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
