import java.sql.*;

public class Database {

    // The database file will be created in your project folder
    private static final String URL = "jdbc:sqlite:reservation.db";

    // Opens a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Creates the tables and adds sample data (runs once at app start)
    public static void initialize() {
        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            // Table 1: users who can log in
            st.execute("CREATE TABLE IF NOT EXISTS users ("
                    + "username TEXT PRIMARY KEY, "
                    + "password TEXT NOT NULL)");

            // Table 2: trains
            st.execute("CREATE TABLE IF NOT EXISTS trains ("
                    + "train_no INTEGER PRIMARY KEY, "
                    + "train_name TEXT NOT NULL)");

            // Table 3: bookings (PNR is created automatically)
            st.execute("CREATE TABLE IF NOT EXISTS reservations ("
                    + "pnr INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "passenger_name TEXT NOT NULL, "
                    + "train_no INTEGER NOT NULL, "
                    + "train_name TEXT NOT NULL, "
                    + "class_type TEXT NOT NULL, "
                    + "journey_date TEXT NOT NULL, "
                    + "source TEXT NOT NULL, "
                    + "destination TEXT NOT NULL)");

            // Default login: username = admin, password = admin123
            st.execute("INSERT OR IGNORE INTO users VALUES ('admin', 'admin123')");

            // Sample trains
            st.execute("INSERT OR IGNORE INTO trains VALUES (12123, 'Deccan Queen')");
            st.execute("INSERT OR IGNORE INTO trains VALUES (12126, 'Pragati Express')");
            st.execute("INSERT OR IGNORE INTO trains VALUES (12301, 'Howrah Rajdhani')");
            st.execute("INSERT OR IGNORE INTO trains VALUES (12951, 'Mumbai Rajdhani')");
            st.execute("INSERT OR IGNORE INTO trains VALUES (12009, 'Shatabdi Express')");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Checks username and password. Returns true if correct.
    public static boolean checkLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if a matching row exists
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    // Finds the train name from the train number. Returns null if not found.
    public static String getTrainName(int trainNo) {
        String sql = "SELECT train_name FROM trains WHERE train_no = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, trainNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("train_name");
            }
        } catch (SQLException e) {
            System.out.println("Train error: " + e.getMessage());
        }
        return null;
    }
        // Saves a booking and returns the PNR number. Returns -1 if it fails.
    public static int bookTicket(String name, int trainNo, String trainName,
                                 String classType, String date,
                                 String source, String destination) {
        String sql = "INSERT INTO reservations "
                + "(passenger_name, train_no, train_name, class_type, journey_date, source, destination) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setInt(2, trainNo);
            ps.setString(3, trainName);
            ps.setString(4, classType);
            ps.setString(5, date);
            ps.setString(6, source);
            ps.setString(7, destination);
            ps.executeUpdate();

            // Get the PNR that the database created
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Booking error: " + e.getMessage());
        }
        return -1;
    }
        // Finds a booking by PNR. Returns the details as text, or null if not found.
    public static String[] getBooking(int pnr) {
        String sql = "SELECT * FROM reservations WHERE pnr = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pnr);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new String[]{
                    String.valueOf(rs.getInt("pnr")),
                    rs.getString("passenger_name"),
                    String.valueOf(rs.getInt("train_no")),
                    rs.getString("train_name"),
                    rs.getString("class_type"),
                    rs.getString("journey_date"),
                    rs.getString("source"),
                    rs.getString("destination")
                };
            }
        } catch (SQLException e) {
            System.out.println("Fetch error: " + e.getMessage());
        }
        return null;
    }

    // Deletes a booking. Returns true if it was deleted.
    public static boolean cancelBooking(int pnr) {
        String sql = "DELETE FROM reservations WHERE pnr = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pnr);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Cancel error: " + e.getMessage());
            return false;
        }
    }
}