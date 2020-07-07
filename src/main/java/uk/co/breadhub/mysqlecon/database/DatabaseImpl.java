package uk.co.breadhub.mysqlecon.database;

import uk.co.breadhub.mysqlecon.Main;

import java.sql.*;
import java.util.UUID;

public class DatabaseImpl implements DatabaseApi {

    private static final String host = Main.getInstance().getConfig().getString("Database.Hostname");
    private static final String port = Main.getInstance().getConfig().getString("Database.Port");
    private static final String database = Main.getInstance().getConfig().getString("Database.Database");
    private static final String username = Main.getInstance().getConfig().getString("Database.Username");
    private static final String password = Main.getInstance().getConfig().getString("Database.Password");
    private static Connection con;

    @Override
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

    @Override
    public void disconnect(){
        try {
            getConnection().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

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

    @Override
    public void init() {
        String sql = "CREATE TABLE IF NOT EXISTS `" + database + "`.`Global_banktable` ( " + "`UUID` VARCHAR(64) NOT NULL , " + "`balance` int(42) NOT NULL , " + "PRIMARY KEY (`UUID`)) " + "ENGINE = InnoDB;";
        try {
            Statement stmt = getConnection().createStatement();
            stmt.executeUpdate(sql);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
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

    @Override
    public void adminSet(UUID uniqueId, int amount) {

    }

    @Override
    public void userDeposit(UUID uniqueId, int amount) {

    }

    @Override
    public void userWithdraw(UUID uniqueId, int amount) {

    }

    @Override
    public void adminDeposit(UUID uniqueId, int amount) {

    }

    @Override
    public void adminWithdraw(UUID uniqueId, int amount) {

    }
}
