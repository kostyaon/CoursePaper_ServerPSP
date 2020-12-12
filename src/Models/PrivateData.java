package Models;

import java.io.Serializable;

public class PrivateData implements Serializable {
    private String password;
    private boolean role;

    public PrivateData(){
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public boolean getRole(){
        return role;
    }
}
