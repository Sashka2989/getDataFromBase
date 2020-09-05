package com.alexplot.getdatafrombase;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;

public class ParserForSearch {

    private static final String SELECT_QUERY_FIRSTNAME_BYERS =
            "SELECT last_name, first_name FROM buyers " +
                    "WHERE last_name = ?";
    private static final String SELECT_QUERY_BUYERS_BUY_PRODUCT =
            "SELECT buyers.last_name, buyers.first_name " +
                    "FROM buy " +
                    "INNER JOIN products " +
                    "ON buy.pruduct_id = products.id " +
                    "INNER JOIN buyers " +
                    "ON buyers.id = buy.id_buyer " +
                    "WHERE products.name = ? " +
                    "GROUP BY buyers.last_name, buyers.first_name " +
                    "HAVING  count(products.name) > ?";
    private static final String SELECT_QUERY_BUYERS_BETWEEN_SUM =
            "SELECT buyers.last_name, buyers.first_name FROM buy " +
                    "INNER JOIN buyers " +
                    "ON buyers.id = buy.id_buyer " +
                    "INNER JOIN products " +
                    "ON buy.pruduct_id = products.id " +
                    "GROUP BY buyers.last_name, buyers.first_name " +
                    "HAVING sum(products.price) BETWEEN ? AND ?";
    private static final String SELECT_QUERY_PASSIVE_BUYERS =
            "SELECT buyers.last_name, buyers.first_name FROM buy " +
                    "INNER JOIN buyers " +
                    "ON buyers.id = buy.id_buyer " +
                    "GROUP BY buyers.last_name, buyers.first_name " +
                    "ORDER BY count(buy.pruduct_id) " +
                    "LIMIT ?";



    public static JSONObject getResultByCriteria(JSONObject criteria) {
        Connection connection = null;
        JSONObject resultObject = new JSONObject();
        resultObject.put("criteria", criteria);

        JSONArray resultArray = new JSONArray();
        PreparedStatement statement;

        try {
            connection = DriverManager.getConnection(ConnectionData.URL,
                    ConnectionData.USER, ConnectionData.PASSWORD);

            if (criteria.get("lastName") != null) {
                statement = connection.prepareStatement(SELECT_QUERY_FIRSTNAME_BYERS);
                statement.setString(1, (String) criteria.get("lastName"));
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    JSONObject person = new JSONObject();
                    person.put("lastName", resultSet.getString("last_name"));
                    person.put("firstName", resultSet.getString("first_name"));
                    resultArray.add(person);
                }
            } else if (criteria.get("productName") != null) {
                statement = connection.prepareStatement(SELECT_QUERY_BUYERS_BUY_PRODUCT);
                statement.setString(1, (String) criteria.get("productName"));
                statement.setLong(2, (Long) criteria.get("minTimes"));
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    JSONObject person = new JSONObject();
                    person.put("lastName", resultSet.getString("last_name"));
                    person.put("firstName", resultSet.getString("first_name"));
                    resultArray.add(person);
                }

            } else if (criteria.get("minExpenses") != null) {
                statement = connection.prepareStatement(SELECT_QUERY_BUYERS_BETWEEN_SUM);
                statement.setLong(1, (Long) criteria.get("minExpenses"));
                statement.setLong(2, (Long) criteria.get("maxExpenses"));
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    JSONObject person = new JSONObject();
                    person.put("lastName", resultSet.getString("last_name"));
                    person.put("firstName", resultSet.getString("first_name"));
                    resultArray.add(person);
                }

            } else if (criteria.get("badCustomers") != null) {

            }

            resultObject.put("results", resultArray);

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(resultObject.toJSONString());
        return resultObject;
    }




}
