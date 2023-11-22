package org.springframework.samples.petclinic.rest.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PetTypeDatabaseActions {

    public int ownerId;
    public PetTypeDatabaseActions(int ownerId) {
        this.ownerId = ownerId;
    }


    public void table(List<List<String>> table) {
        // optional function
    }

    public List<List<List<String>>> query() {

        List<List<List<String>>> formattedData = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:9966/petclinic/api/pettypes");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            JSONArray jsonArray = new JSONArray(result.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                List<List<String>> row = new ArrayList<>();

                Iterator<String> keys = jsonObj.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (!(jsonObj.get(key) instanceof JSONObject || jsonObj.get(key) instanceof JSONArray)) {
                        List<String> cell = new ArrayList<>();
                        cell.add(key);
                        Object value = jsonObj.get(key);
                        cell.add(String.valueOf(value)); // Convert any type of value to string
                        row.add(cell);
                    }
                }
                formattedData.add(row);
            }

            System.out.println("TYPES FORMATTED DATA");
            System.out.println(formattedData);

            String output;

            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedData;
    }
}
