package com.alexplot.getdatafrombase;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.simple.JSONObject;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ParserForStat {
    private static final String SELECT_QUERY_GET_PRODUCTS =
            "SELECT products.name, sum(products.price) FROM buyers " +
                    "INNER JOIN buy " +
                    "ON buyers.id = buy.id_buyer " +
                    "INNER JOIN products " +
                    "ON buy.pruduct_id = products.id " +
                    "WHERE buyers.id = ? " +
                    "AND buy.purchase_date BETWEEN ? AND ?" +
                    "GROUP BY products.name " +
                    "ORDER BY sum(products.price) DESC";

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





            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }







        return resultObject;
    }

    private static int getCountDayBetweenDates(String startDate, String endDate) {
        LocalDate startDt = LocalDate.parse(startDate);
        LocalDate endDt = LocalDate.parse(endDate);
        return Days.daysBetween(startDt, endDt).getDays();
    }

}
