package com.alexplot.getdatafrombase;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.xml.crypto.Data;
import java.sql.*;

public class ParserForStat {

    private static Date startDate;
    private static Date endDate;

    public static JSONObject getResult(JSONObject inObject) {
        JSONObject resultObject = new JSONObject();
        Connection connection;

        String startDateString = (String) inObject.get("startDate");
        String endDateString = (String) inObject.get("endDate");


        startDate = Date.valueOf(startDateString);
        endDate = Date.valueOf(endDateString);

        resultObject.put("type", "stat");
        resultObject.put("totalDays", getCountDayBetweenDates(startDateString, endDateString));

        try {
            connection = DriverManager.getConnection(ConnectionData.URL,
                    ConnectionData.USER, ConnectionData.PASSWORD);

            System.out.println(createPersonJsonObject(connection).toJSONString());




            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return resultObject;
    }

    private static JSONArray createJsonProductsArray(Connection connection, long idPerson) {
        JSONArray productJsonArray = new JSONArray();

        final String SELECT_QUERY_GET_PRODUCTS =
                "SELECT products.name, sum(products.price) FROM buyers " +
                        "INNER JOIN buy " +
                        "ON buyers.id = buy.id_buyer " +
                        "INNER JOIN products " +
                        "ON buy.pruduct_id = products.id " +
                        "WHERE buyers.id = ? " +
                        "AND buy.purchase_date BETWEEN ? AND ?" +
                        "GROUP BY products.name " +
                        "ORDER BY sum(products.price) DESC";

        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_QUERY_GET_PRODUCTS);
            statement.setLong(1, idPerson);
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                JSONObject productJsonObject = new JSONObject();
                productJsonObject.put("name", resultSet.getString("name"));
                productJsonObject.put("expenses", resultSet.getLong("sum"));

                productJsonArray.add(productJsonObject);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productJsonArray;
    }

    private static JSONArray createPersonJsonObject(Connection connection) {
        JSONArray customersJsonArray = new JSONArray();

        String SELECT_QUERY_GET_CUSTOMERS =
                "SELECT buyers.id, buyers.last_name, buyers.first_name, sum(products.price) FROM buyers " +
                        "INNER JOIN buy " +
                        "ON buyers.id = buy.id_buyer " +
                        "INNER JOIN products " +
                        "ON buy.pruduct_id = products.id " +
                        "WHERE buy.purchase_date BETWEEN ? AND ? " +
                        "GROUP BY buyers.id, buyers.last_name, buyers.first_name " +
                        "ORDER BY sum(products.price) DESC";

        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_QUERY_GET_CUSTOMERS);
            statement.setDate(1, startDate);
            statement.setDate(2, endDate);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                JSONObject personJsonObject = new JSONObject();
                long id_customer = resultSet.getLong("id");

                personJsonObject.put("name", resultSet.getString("last_name")
                        + " " + resultSet.getString("first_name"));
                personJsonObject.put("purchases", createJsonProductsArray(connection,
                        id_customer));
                personJsonObject.put("totalExpenses", resultSet.getLong("sum"));
                customersJsonArray.add(personJsonObject);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customersJsonArray;
    }


    private static int getCountDayBetweenDates(String startDate, String endDate) {
        LocalDate startDt = LocalDate.parse(startDate);
        LocalDate endDt = LocalDate.parse(endDate);
        return Days.daysBetween(startDt, endDt).getDays();
    }

}
