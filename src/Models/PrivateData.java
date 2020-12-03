package Models;

public class PrivateData {
    private String password;
    private boolean role;

    public PrivateData(String password){
        this.password = password;
        this.role = false;
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
