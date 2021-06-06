public class User implements IUser , IFunctional{
    String login;
    String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public void ShowInfo() {
        System.out.println("login - " + this.login + ", password - " + this.password);
    }

    @Override
    public void One() {}
    @Override
    public void Two() {}
    @Override
    public void Three() {}
}
