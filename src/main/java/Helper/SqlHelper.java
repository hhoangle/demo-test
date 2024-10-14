package Helper;
import java.sql.*;

import commons.CommonConstants;

public class SqlHelper {
    public static String findValue(String url, String username, String password, String tableName, String filterColumn, String filterValue, String columnName) throws SQLException {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String value = null;

        try {
            // Establish connection
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();

            // Construct SQL query with filter condition
            String query = "SELECT * FROM " + tableName + " WHERE " + filterColumn + " = '" + filterValue + "'";

            // Execute query
            resultSet = statement.executeQuery(query);

            // Retrieve value from result set
            if (resultSet.next()) {
                value = resultSet.getString(columnName);
            } else {
                throw new SQLException("No results found for the query.");
            }
        } finally {
            // Close connections
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return value;
    }

    // Example usage
    public static void main(String[] args) {
        String url = CommonConstants.DB_URL;
        String username = CommonConstants.USERNAME_DB;
        String password = CommonConstants.PASSWORD_DB;
        String tableName = CommonConstants.TABLE_NAME_DB;
        String filterColumn = CommonConstants.FILTER_COLUMN_DB;
        String filterValue = CommonConstants.FILTER_VALUE;
        String columnName = CommonConstants.COLUMN_NAME_DB;

        try {
            String retrievedValue = SqlHelper.findValue(url, username, password, tableName, filterColumn, filterValue, columnName);
            System.out.println("Retrieved value: " + retrievedValue);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
