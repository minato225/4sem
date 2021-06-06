import java.sql.*;
import java.util.ArrayList;

public class DB_SqLite_Helper {
    private static final String url = "jdbc:sqlite:identifier.sqlite";

    public static void createNewTable(String tableName) throws ClassNotFoundException {
        // SQL statement for creating a new table
        String sql = "create table if not exists "+ tableName +"(\n" +
                "\tid integer primary key,\n" +
                "\tgenre TEXT,\n" +
                "\tname TEXT,\n" +
                "\tprice TEXT,\n" +
                "\tyear TEXT\n" +
                ");";

        Class.forName("org.sqlite.JDBC");
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement())
        {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addNewFilm(FilmInfo info, String tableName) throws ClassNotFoundException {
        String sql = "insert into "+ tableName + " (genre, name, price, year) values(" +
                String.format("'%s', '%s', '%s', '%s'",
                        info.genre,
                        info.name,
                        info.price,
                        info.year)+
                " );";

        Class.forName("org.sqlite.JDBC");
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement())
        {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<ArrayList<String>> GetData(String tableName) throws ClassNotFoundException {
        String sql = "select * from "+tableName;

        Class.forName("org.sqlite.JDBC");
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement())
        {
            return getArrayFromResultSet(stmt.executeQuery(sql));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static ArrayList<ArrayList<String>> getArrayFromResultSet(ResultSet resultSet) throws SQLException {
        var table = new ArrayList<ArrayList<String>>();
        while(resultSet.next()){
            var row = new ArrayList<String>();
            table.add(row);
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                row.add(resultSet.getString(i));
            }
        }
        return table;
    }
}