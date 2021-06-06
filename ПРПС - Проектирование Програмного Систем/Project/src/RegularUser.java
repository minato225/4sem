public class RegularUser extends User{

    public RegularUser(String login, String password) {
        super(login, password);
        Admin.a.add(new Publication("Some Text","roman"));
        Admin.a.add(new Publication("Some TExt 1","daniil"));
        Admin.a.add(new Publication("Some TExt 2","dima"));
        Admin.a.add(new Publication("Some TExt 3","oleg"));
        Admin.a.add(new Publication("Some TExt 4","rodion"));
    }

    public void FindPublication(String publication) {
        Admin.a.stream()
                .filter(x -> x.text.equals(publication))
                .forEach(x -> System.out.println(x + " "));
        System.out.println("FindPublication ! Completed");
    }

    public void GetBiblioIndex(int publicationIndex) {
        System.out.println(Admin.a.get(publicationIndex));
        System.out.println("FindPublication ! Completed");
    }

    public void GetPublicationList() {
        Admin.a.forEach(i -> System.out.printf("%s ", i));
        System.out.println("GetListOfDuplicates ! Completed");
    }

    @Override
    public void ShowInfo() {
        super.ShowInfo();
    }

    @Override
    public void One() {
        GetPublicationList();
    }

    @Override
    public void Two() {
        GetBiblioIndex(1);
    }

    @Override
    public void Three() {
        FindPublication("some Text");
    }
}
