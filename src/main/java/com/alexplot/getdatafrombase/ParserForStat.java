package com.alexplot.getdatafrombase;

import org.json.simple.JSONObject;

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

    public static JSONObject getResult(JSONObject inObject) {
        JSONObject resultObject = new JSONObject();







        return resultObject;
    }



}
