package Models;

import java.io.Serializable;

public class Rating implements Serializable {
    private String testTheme;
    private int testLevel;
    private float rating;
    private int userID;

    public Rating(){

    }
    public Rating(String theme, int level, float rating){
        this.testTheme = theme;
        this.testLevel = level;
        this.rating = rating;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setTestLevel(int testLevel) {
        this.testLevel = testLevel;
    }

    public void setTestTheme(String testTheme) {
        this.testTheme = testTheme;
    }

    public float getRating() {
        return rating;
    }

    public int getTestLevel() {
        return testLevel;
    }

    public String getTestTheme() {
        return testTheme;
    }

    @Override
    public String toString() {
        String str;
        str = "Theme: " + testTheme + "\nLevel: " + testLevel + "\nRating:" + rating + "%";
        return super.toString();
    }
}
