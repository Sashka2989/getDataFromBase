package com.alexplot.getdatafrombase;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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


        JSONArray resultSet = new JSONArray();
        try {
            connection = DriverManager.getConnection(ConnectionData.URL,
                    ConnectionData.USER, ConnectionData.PASSWORD);

            if (criteria.get("lastName") != null) {


            } else if (criteria.get("productName") != null) {

            } else if (criteria.get("minExpenses") != null) {

            } else if (criteria.get("badCustomers") != null) {

            }


            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultObject;
    }




}
