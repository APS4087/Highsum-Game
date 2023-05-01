import java.io.Serializable;

public class User implements Serializable {

    private String loginName;
    private String hashedPassword;


    public User(String loginName, String password){
        this.loginName= loginName;
        this.hashedPassword= password;
    }


    public String getLoginName() {
        return loginName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }


}
