import java.io.Serializable;
import java.security.MessageDigest;

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
    public void setLoginName(){
        this.loginName = loginName;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    // temp not using this method
    public boolean checkPassword(String password){
        return this.hashedPassword.equals(password);
    }
}
