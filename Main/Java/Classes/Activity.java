package worldOfZuul.Main.Java.Classes;

public class Activity {
    private final String id;
    private final String displayName; // The name of the activity. Will show up in possible activities.
    private final boolean daily; // Determines if the task is daily (Daily=true, non-daily=false)
    private int successPoints; // Positive number for number of points to receive when completing the activity.
    private int failurePoints; // Positive number to subtract from points when not completing or failing the activity.
    private int powerCost; // Amount of power required to complete the activity.
    private boolean done = false; // False if activity is not done, true if activity is done.

    public Activity(String id, String displayName, int successPoints, int failurePoints, int powerCost, boolean daily) {
        this.id = id;
        this.displayName = displayName;
        this.daily = daily;

        // successPoint, failurePoint and powerCost uses another method, which makes sure that all the values non-negative.
        this.setSuccessPoints(successPoints);
        this.setFailurePoints(failurePoints);
        this.setPowerCost(powerCost);
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getSuccessPoints() {
        return this.successPoints;
    }

    private void setSuccessPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("successPoints cannot be negative");
        } else {
            this.successPoints = points;
        }
    }

    public int getFailurePoints() {
        return this.failurePoints;
    }

    private void setFailurePoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("failurePoints cannot be negative");
        } else {
            this.failurePoints = points;
        }
    }

    public int getPowerCost() {
        return this.powerCost;
    }

    private void setPowerCost(int powerCost) {
        if (powerCost < 0) {
            throw new IllegalArgumentException("powerCost cannot be negative");
        } else {
            this.powerCost = powerCost;
        }
    }

    public boolean isDaily() {
        return this.daily;
    }

    public boolean isDone() {
        return this.done;
    }

    public void setAsDone() {
        this.setDoneState(true);
    }

    public void setDoneState(boolean state) {
        this.done = state;
    }

    public String doneToString() {
        if (this.done) {
            return "done";
        } else {
            return "not done";
        }
    }

    @Override
    public String toString() {
        return String.format("'%s' power=%d %s", this.displayName, this.getPowerCost(), this.doneToString());
    }
}