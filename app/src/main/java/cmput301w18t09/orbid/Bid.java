package cmput301w18t09.orbid;

/**
 * A model representation of a bid made on a given task.
 *
 * @author Zach Redfern
 * @see Task
 */
public class Bid {

    private String provider;
    private double price;
    private String description;

    /**
     * Bid class constructor
     *
     * @param provider The task provider that bid belongs to
     * @param price The price the task provider has set for their bid
     * @param description The description the task provider has supplied with the bid
     */
    public Bid(String provider, double price, String description)
    {
        this.provider = provider;
        this.price = price;
        this.description = description;
    }

    /**
     * Gets the task provider
     *
     * @return The task provider the bid belongs to
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the task provider
     *
     * @param provider The task provider the bid belongs to
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * Gets the price of the bid
     *
     * @return The price the task provider has set for their bid
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the bid
     *
     * @param price The price the task provider has set for their bid
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the description the task provider has supplied with the bid
     *
     * @return The description the task provider has supplied with the bid
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description the task provider has supplied with the bid
     *
     * @param description The description the task provider has supplied with the bid
     */
    public void setDescription(String description) {
        this.description = description;
    }
}