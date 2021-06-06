import java.util.ArrayList;

public class Admin extends User implements IUser {
    public static ArrayList<Publication> a = new ArrayList<>();

    public Admin(String login, String password) {
        super(login, password);
    }

    @Override
    public void ShowInfo() {
        super.ShowInfo();
    }

    @Override
    public void One() {
        this.AddPublication(new Publication("Garry Potter", "Joan Rouling"));
    }

    @Override
    public void Two() {
        this.DeletePublication();
    }

    @Override
    public void Three() {
        GetListOfDuplicates();
    }

    public void AddPublication(Publication pub) {
        a.add(pub);
        System.out.println("AddPublication ! Completed");
    }

    public void DeletePublication() {
        System.out.println("DeletePublication ! Completed");
    }

    public void GetListOfDuplicates() {
        a.forEach(i -> System.out.printf("%s ", i));
        System.out.println("GetListOfDuplicates ! Completed");
    }
}
