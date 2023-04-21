public class User {

    private final String loginName;
    private final String password; //temp plain password

    public User(String loginName, String password){
        this.loginName= loginName;
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    // temp not using this method
    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}
