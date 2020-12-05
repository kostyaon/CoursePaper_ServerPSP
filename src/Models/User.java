package Models;

import java.io.Serializable;

public class User implements Serializable {
    private int userID;
    private String nickname;
    private String specialization;
    private String country;

    public User(String nickname, String specialization, String country){
        if (country.equals("")){
            this.country = "UNKNOWN";
        }
        else{
            this.country = country;
        }

        this.nickname = nickname;
        this.specialization = specialization;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getCountry() {
        return country;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String toString() {
        String str;
        str = "Nickname: " + nickname + "\nSpecialization: " + specialization + "\nCountry: " + country;
        return str;
    }
}
