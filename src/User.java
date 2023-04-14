public class User {

    private String loginName;
    private String password; //temp plain password

    public User(String loginName, String password){
        this.loginName= loginName;
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}
