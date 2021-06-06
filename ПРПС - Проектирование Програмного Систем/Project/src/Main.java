import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("\t\tWelcome to bib system");
        var in = new Scanner(System.in);
        System.out.println("Enter login: ");
        var login = in.nextLine();
        System.out.println("Enter password: ");
        var password = in.nextLine();
        var isAdmin = password.equals("ADMIN");
        var user = isAdmin ? new Admin(login, password) : new RegularUser(login, password);
        user.ShowInfo();
        while (true) {
            System.out.println("Enter your choice");
            System.out.println("1 - " + (!isAdmin ? "Get publication list" : "Add publication"));
            System.out.println("2 - " + (!isAdmin ? "Get publication index" : "Delete publication"));
            System.out.println("3 - " + (!isAdmin ? "Find publication" : "Get list of dublicates"));
            System.out.println("0 - Quit");
            switch (in.nextLine()) {
                case "1" -> user.One();
                case "2" -> user.Two();
                case "3" -> user.Three();
                case "0" -> {
                    return;
                }
                default -> throw new IllegalStateException("Unexpected value");
            }
        }
    }
}
