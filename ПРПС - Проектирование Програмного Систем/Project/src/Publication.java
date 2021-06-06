public class Publication {
    public static int biblioIndex = 0;
    public String text;
    public String author;

    public Publication(String text, String author) {
        this.text = text;
        this.author = author;
        biblioIndex++;
    }

    @Override
    public String toString() {
        return "text: " + text + " Author: " + author + "\n";
    }
}
