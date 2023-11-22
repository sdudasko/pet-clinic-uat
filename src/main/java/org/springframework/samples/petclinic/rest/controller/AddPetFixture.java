package org.springframework.samples.petclinic.rest.controller;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddPetFixture {
    int createdPetID = -1;
    int createdResponseCode = 404;

    public boolean addPetWithTypeAndBirthdateAndTypename(String name, String birthdate, String typename) {

        try {
            // Endpoint URL
            URL url = new URL("http://localhost:9966/petclinic/api/pets");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Setting request method and headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                "{\"name\": \"%s\", " +
                    "\"birthDate\": \"%s\", " +
                    "\"type\": { \"name\": \"%s\", \"id\": %d }}",
                name, birthdate, typename, 1);

            System.out.println(jsonInputString);
            // Writing the JSON data to the request body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Reading the response
            int responseCode = conn.getResponseCode();
            System.out.println("PETS - POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_CREATED) { // Success
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extracting the owner ID
                createdPetID = jsonResponse.getInt("id");
                System.out.println("Pet ID: " + createdPetID);

                createdResponseCode = responseCode;
            }

        } catch (Exception e) {
//            System.out.println("BIG ERROR");
//            e.printStackTrace();
        }
        return true;
    }

    public String responseMessage() {
        if (createdPetID == -1)
        {
            return Integer.toString(422);
        }
        try {
            // Replace with the actual URL of the API
            String url = "http://localhost:9966/petclinic/api/pets/{petId}";

            // Replace {ownerId} with the actual owner ID you want to fetch
            URL obj = new URL(url.replace("{petId}", Integer.toString(createdPetID))); // example with owner ID 1
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Optional, default is GET
            con.setRequestMethod("GET");

            //add request header if needed, e.g., con.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = con.getResponseCode();


            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Assuming response is in JSON format
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Extracting the owner ID
            int petId = jsonResponse.getInt("id");

            if (petId == createdPetID)
            {
                return Integer.toString(createdResponseCode);
            }
            System.out.println("Pet ID: " + petId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
