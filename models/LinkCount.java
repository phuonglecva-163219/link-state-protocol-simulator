package models;

public class LinkCount {
    private String destinationName;
    private int cost;
    public final static int MAX_AGE = 50;

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "LinkCount{" +
                "destinationName='" + destinationName + '\'' +
                ", cost=" + cost +
                '}';
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public LinkCount(String destinationName, int cost) {
        this.destinationName = destinationName;
        this.cost = cost;
    }
}
