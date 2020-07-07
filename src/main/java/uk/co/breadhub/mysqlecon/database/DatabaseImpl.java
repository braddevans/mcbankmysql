package uk.co.breadhub.mysqlecon.database;

import uk.co.breadhub.mysqlecon.Main;

import java.sql.*;
import java.util.UUID;

public class DatabaseImpl {

    private static final String host = Main.getInstance().getConfig().getString("Database.Hostname");
    private static final String port = Main.getInstance().getConfig().getString("Database.Port");
    private static final String database = Main.getInstance().getConfig().getString("Database.Database");
    private static final String username = Main.getInstance().getConfig().getString("Database.Username");
    private static final String password = Main.getInstance().getConfig().getString("Database.Password");
    private static Connection con;

    /**
     * Connects to Database
     */
    public void connect() {
        String UrlString = "jdbc:mysql://" + host + ":" + port + "/" + database + "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                con = DriverManager.getConnection(UrlString, username, password);
            } catch(Exception ex) {
                System.out.println("Failed to create the database connection. :");
                ex.printStackTrace();
            }
        } catch(ClassNotFoundException ex) {
            System.out.println("Driver not found.");
        }
    }

    /**
     * Disconnects from the Database
     */
    public void disconnect(){
        try {
            getConnection().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Gets Database Connection
     * @return
     */
    public Connection getConnection() {
        try {
            if(con == null || con.isClosed()) {
                connect();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * Initialises the Database
     */
    public void init() {
        String sql = "CREATE TABLE IF NOT EXISTS `" + database + "`.`Global_banktable` ( " + "`UUID` VARCHAR(64) NOT NULL , " + "`balance` int(42) NOT NULL , UNIQUE(`UUID`), " + "PRIMARY KEY (`UUID`)) " + "ENGINE = InnoDB;";
        try {
            Statement stmt = getConnection().createStatement();
            stmt.executeUpdate(sql);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the User Balance
     * @param uniqueId
     * @return
     */
    public int userBalance(UUID uniqueId) {
        int balance = 0;
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT balance FROM Global_banktable WHERE UUID=?");
            ps.setString(1, uniqueId.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                balance = rs.getInt("balance");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return balance;
    }

    /**
     * Administrator Setting user Balance Function
     * @param uniqueId
     * @param amount
     */
    public void adminSet(UUID uniqueId, int amount) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT IGNORE INTO `Global_banktable` (`UUID`, `balance`) VALUES (?,?);");
            ps.setString(1, uniqueId.toString());
            ps.setInt(2, amount);
            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deposit into Database as the command Sender
     * @param uniqueId
     * @param amount
     */
    public void userDeposit(UUID uniqueId, int amount) {
        int ammount_ = userBalance(uniqueId) + amount;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT IGNORE INTO `Global_banktable` (`UUID`, `balance`) VALUES (?,?) on duplicate key update `balance` = ?;");
            ps.setString(1, uniqueId.toString());
            ps.setInt(2, ammount_);
            ps.setInt(3, ammount_);
            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Withdraw from Database as the command Sender into Current servers Econ plugin
     * @param uniqueId
     * @param amount
     */
    public void userWithdraw(UUID uniqueId, int amount) {
        int ammount_ = userBalance(uniqueId) - amount;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT IGNORE INTO `Global_banktable` (`UUID`, `balance`) VALUES (?,?) on duplicate key update `balance` = ?;");
            ps.setString(1, uniqueId.toString());
            ps.setInt(2, ammount_);
            ps.setInt(3, ammount_);
            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deposit into Database Through Admin Command
     * @param uniqueId
     * @param amount
     */
    public void adminDeposit(UUID uniqueId, int amount) {
        int ammount_ = userBalance(uniqueId) + amount;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT IGNORE INTO `Global_banktable` (`UUID`, `balance`) VALUES (?,?) on duplicate key update `balance` = ?;");
            ps.setString(1, uniqueId.toString());
            ps.setInt(2, ammount_);
            ps.setInt(3, ammount_);
            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove from Database Through Admin Command
     * @param uniqueId
     * @param amount
     */
    public void adminWithdraw(UUID uniqueId, int amount) {
        int ammount_ = userBalance(uniqueId) - amount;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT IGNORE INTO `Global_banktable` (`UUID`, `balance`) VALUES (?,?) on duplicate key update `balance` = ?;");
            ps.setString(1, uniqueId.toString());
            ps.setInt(2, ammount_);
            ps.setInt(3, ammount_);
            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
