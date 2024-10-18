package dev.luan.vs.database;

import lombok.SneakyThrows;

import java.sql.*;

public class DatabaseManager {

    private Connection connection;
    private String hostname, database, username, password;
    private int port;

    public DatabaseManager(final String hostname, final int port, final String database, final String username, final String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.connect();
    }

    public void connect() {
        if(!isConnected()) {
            try {
                this.connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database +"?autoReconnect=true", username, password);
                System.out.println("[DatabaseManager] Connection to database " + this.hostname + "@" + database + " created!");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public boolean disconnect() {
        if (this.isConnected()) {
            try {
                this.connection.close();
                this.connection = null;
                return true;
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return false;
    }

    public void execute(String query, Object... objs) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            for (int i = 0; i < objs.length; i++) {
                preparedStatement.setObject(i + 1, objs[i]);
            }

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public void execute(String query) {
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean hasTable(String table) throws SQLException {
        ResultSet resultSet = this.connection.getMetaData().getTables(null, null, "%", null);
        while (resultSet.next()) {
            if (table.equalsIgnoreCase(resultSet.getString("TABLE_NAME"))) {
                resultSet.close();
                return true;
            }
        }
        resultSet.close();
        return false;
    }

    public boolean hasColumn(String table, String column) throws SQLException {
        if(!hasTable(table))
            return false;

        DatabaseMetaData databaseMetaData = this.connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getColumns(null, null, table, column);
        return resultSet.next();
    }

    public static class ConnectionException extends Exception {
        private static final long serialVersionUID = 8348749992936357317L;

        public ConnectionException(String msg) {
            super(msg);
        }
    }

    public boolean containsValue(String table, String column, String value) {
        ResultSet resultSet;
        try {
            resultSet = this.select(table, column, value);
            while (resultSet.next()) {
                if (resultSet.getString(column).equalsIgnoreCase(value)) {
                    return true;
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @SneakyThrows
    public void createTable(String query, String table) {
        if(hasTable(table)) return;
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
            System.out.println("Table " + table + " has been created");
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Could not create table " + table);
        }

    }

    public ResultSet select(String table, String where, String whereValue) {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + where + " = '" + whereValue + "';");
            statement.close();
            return resultSet;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public ResultSet select(String table) {
        try {
            Statement statement = this.connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + ";");
            statement.close();
            return resultSet;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void update(String table, String column, Object columnValue, String where, Object whereValue) {
        this.execute("UPDATE " + table + " SET " + column + " = ? WHERE " + where + " = ?;", columnValue, whereValue);
    }

    public void delete(String table, String where, Object whereValue) {
        this.execute("DELETE FROM " + table + " WHERE " + where + " = ?;", whereValue);
    }

    public boolean isConnected() {
        return this.connection != null;
    }

    public Connection getConnection() {
        return this.connection;
    }
}