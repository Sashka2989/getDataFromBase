package com.alexplot.getdatafrombase;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ParserForSearch {

    public static JSONObject getResultByCriteria(JSONObject criteria) {
        JSONObject resultObject = new JSONObject();
        resultObject.put("criteria", criteria);

        if (criteria.get("lastName") != null) {


        } else if(criteria.get("productName") != null) {

        } else if(criteria.get("minExpenses") != null) {

        } else if(criteria.get("badCustomers") != null) {

        }


        return resultObject;
    }




}
