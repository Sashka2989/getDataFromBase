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

            System.out.println(createJsonProductsArray(connection, 2).toJSONString());




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
            System.out.println("Error query");
            e.printStackTrace();
        }

        return productJsonArray;
    }


    private static int getCountDayBetweenDates(String startDate, String endDate) {
        LocalDate startDt = LocalDate.parse(startDate);
        LocalDate endDt = LocalDate.parse(endDate);
        return Days.daysBetween(startDt, endDt).getDays();
    }

}
