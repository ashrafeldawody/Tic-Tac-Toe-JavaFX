/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.models;

import org.json.JSONObject;

/**
 *
 * @author Eagle
 */
public class Helpers {

    public static String getStatusObject(String status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status);
        return jsonObject.toString();
    }



}
