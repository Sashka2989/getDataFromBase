package com.alexplot.getdatafrombase;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
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


    public static JSONObject getResult(JSONObject inFile) {
        JSONObject resultObject = new JSONObject();
        resultObject.put("type", "search");

        JSONArray jsonArrayForResultSearch = new JSONArray();
        JSONArray jsonCriteriasArray = (JSONArray) inFile.get("criterias");
        for(int i = 0; i < jsonCriteriasArray.size(); i++) {
            JSONObject jsonObjectCriteria = ParserForSearch.getResultForCriteria((JSONObject) jsonCriteriasArray.get(i));
            if (jsonObjectCriteria.get("Type") != null) {
                return jsonObjectCriteria;
            }
            jsonArrayForResultSearch.add(jsonObjectCriteria);
        }
        resultObject.put("results", jsonArrayForResultSearch);


        return resultObject;
    }


    private static JSONObject getResultForCriteria(JSONObject criteria) {
        Connection connection;
        JSONObject resultObject = new JSONObject();
        resultObject.put("criteria", criteria);

        JSONArray resultArray;
        PreparedStatement statement;

        try {
            connection = DriverManager.getConnection(ConnectionData.URL,
                    ConnectionData.USER, ConnectionData.PASSWORD);

            if (criteria.get("lastName") != null) {
                statement = connection.prepareStatement(SELECT_QUERY_FIRSTNAME_BYERS);

                if (!(criteria.get("lastName") instanceof String)) {
                    JSONObject errorObject = new JSONObject();
                    errorObject.put("Type", "Error");
                    errorObject.put("Message", "Criteria lastName is not correct");
                    return errorObject;
                }

                statement.setString(1, (String) criteria.get("lastName"));
            } else if (criteria.get("productName") != null) {
                statement = connection.prepareStatement(SELECT_QUERY_BUYERS_BUY_PRODUCT);

                if (!(criteria.get("productName") instanceof String)
                        || !(criteria.get("minTimes") instanceof Long)) {
                    JSONObject errorObject = new JSONObject();

                    errorObject.put("Type", "Error");
                    errorObject.put("Message", "Criteria productName or minTimes is not correct");
                    return errorObject;
                }

                statement.setString(1, (String) criteria.get("productName"));
                statement.setLong(2, (Long) criteria.get("minTimes"));
            } else if (criteria.get("minExpenses") != null) {
                statement = connection.prepareStatement(SELECT_QUERY_BUYERS_BETWEEN_SUM);

                if (!(criteria.get("minExpenses") instanceof Long)
                        || !(criteria.get("maxExpenses") instanceof Long)) {
                    JSONObject errorObject = new JSONObject();
                    errorObject.put("Type", "Error");
                    errorObject.put("Message", "Criteria minExpenses or maxExpenses is not correct");
                    return errorObject;
                }
                if ((Long) criteria.get("minExpenses") > (Long) criteria.get("maxExpenses")) {
                    JSONObject errorObject = new JSONObject();
                    errorObject.put("Type", "Error");
                    errorObject.put("Message", "Criteria minExpenses greater then maxExpenses");
                    return errorObject;
                }

                statement.setLong(1, (Long) criteria.get("minExpenses"));
                statement.setLong(2, (Long) criteria.get("maxExpenses"));
            } else if (criteria.get("badCustomers") != null) {
                statement = connection.prepareStatement(SELECT_QUERY_PASSIVE_BUYERS);

                if (!(criteria.get("badCustomers") instanceof Long)) {
                    JSONObject errorObject = new JSONObject();
                    errorObject.put("Type", "Error");
                    errorObject.put("Message", "Criteria badCustomers is not correct");
                    return errorObject;
                }
                if ((Long) criteria.get("badCustomers") < 0) {
                    JSONObject errorObject = new JSONObject();
                    errorObject.put("Type", "Error");
                    errorObject.put("Message", "Criteria badCustomers smaller then 0");
                    return errorObject;
                }

                statement.setLong(1, (Long) criteria.get("badCustomers"));
            } else {
                JSONObject errorObject = new JSONObject();
                errorObject.put("Type", "Error");
                errorObject.put("Message", "Unknown criteria");
                return errorObject;
            }
            resultArray = getArrayCustomers(statement);
            resultObject.put("results", resultArray);

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultObject;
    }

    private static JSONArray getArrayCustomers(PreparedStatement statement) throws SQLException {
        JSONArray resultArray = new JSONArray();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            JSONObject person = new JSONObject();
            person.put("lastName", resultSet.getString("last_name"));
            person.put("firstName", resultSet.getString("first_name"));
            resultArray.add(person);
        }
        return resultArray;
    }



}
