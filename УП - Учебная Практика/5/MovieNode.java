public class MovieNode {
    public String name;
    public float price;
    public String genre;
    public int year;

    MovieNode(String genre, String name, int year, float price) {
        this.genre = genre;
        this.name = name;
        this.year = year;
        this.price = price;
    }

    MovieNode(String record) {
        String[] params = record.split(",");
        if (params.length != 4) {
            throw new IllegalArgumentException("Invalid record string");
        }
        genre = params[0];
        name = params[1];
        year = Integer.parseInt(params[2]);
        price = Float.parseFloat(params[3]);
    }

    @Override
    public String toString() {
        return genre + "," + name + "," + year + "," + price;
    }

}
