package org.springframework.samples.petclinic.rest.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AddOwnerFixture {

    String pettype = "";
    int createdPetID = -1;

    int createdOwnerID = -1;

    int createdResponseCode = 404;

    int createdPetResponseCode = 404;

    int removeResponseCode = 404;

    int updateResponseCode = 404;

    int createdPettypeResponseCode = 404;

    public AddOwnerFixture() {
    }
    public boolean addOwnerWithNameAndLastnameAndAddressAndCityAndTelephone(
        String firstName, String lastName, String address, String city, String telephone) {

        try {
            // Endpoint URL
            URL url = new URL("http://localhost:9966/petclinic/api/owners");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Setting request method and headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                "{\"firstName\": \"%s\", \"lastName\": \"%s\", \"address\": \"%s\", \"city\": \"%s\", \"telephone\": \"%s\"}",
                firstName, lastName, address, city, telephone
            );

            // Writing the JSON data to the request body
            try (OutputStream os = conn.getOutputStream()) {
                try {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                } catch (Exception e1) {
                    return false;
                }

            } catch (Exception ee)
            {
                return false;
            }

            createdResponseCode = conn.getResponseCode();
            System.out.println("fsfsadd owner POST Response Code :: " + createdResponseCode);

            if (createdResponseCode == HttpURLConnection.HTTP_CREATED) { // Success
                BufferedReader reader;

                if (conn.getErrorStream() != null) {
                    System.out.println("ERRORS");
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                } else {
                    System.out.println("NO ERRORS");
                    System.out.println(conn.getErrorStream());
                    System.out.println("enERRORS");

                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Parse the response using JSONObject
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extracting the owner ID
                createdOwnerID = jsonResponse.getInt("id");
                System.out.println("Owner ID: " + createdOwnerID);
            }

            return true;

        } catch (Exception e) {
            System.out.println("PRINTSTACKTRACE");
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOwnerWithNameAndLastnameAndAddressAndCityAndTelephone(
        String firstName, String lastName, String address, String city, String telephone) {

        try {
            // Endpoint URL
            URL url = new URL("http://localhost:9966/petclinic/api/owners/" + 2);

            // JSON payload string
            String jsonInputString = "{" +
                "\"firstName\": \"George\"," +
                "\"lastName\": \"Franklin\"," +
                "\"address\": \"110 W. Liberty St.\"," +
                "\"city\": \"Madison\"," +
                "\"telephone\": \"6085551023\"" +
                "}";

            // Create a connection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            // Send JSON input
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Check for the response code
            int responseCode = con.getResponseCode();
            System.out.println("PUT Response Code :: " + responseCode);

            if (responseCode == 204) {
                System.out.println("Update successful.");
                this.updateResponseCode = 204;
                return true;

            } else {
                System.out.println("PUT request not worked.");
            }
            this.updateResponseCode = 422;
            return false;

        } catch (Exception e) {
            System.out.println("PRINTSTACKTRACE");
            e.printStackTrace();
            this.updateResponseCode = 422;
            return false;
        }
    }

    public String responseMessage() {

        if (createdOwnerID == -1) {
            System.out.println("SHOULD NOT CONTINUE");
            return Integer.toString(422);
        }

        try {
            // Replace with the actual URL of the API
            String url = "http://localhost:9966/petclinic/api/owners/{ownerId}";

            // Replace {ownerId} with the actual owner ID you want to fetch
            URL obj = new URL(url.replace("{ownerId}", Integer.toString(createdOwnerID))); // example with owner ID 1
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

            int ownerId = jsonResponse.getInt("id");

            if (ownerId == createdOwnerID)
            {
                return Integer.toString(createdResponseCode);
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return Integer.toString(422);
    }

    public boolean addPettypeWithType(String type) {
        pettype = type;

        try {
            JSONObject json = new JSONObject();
            json.put("name", type);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9966/petclinic/api/pettypes"))
                .header("Content-Type", "application/json; utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Adding a pet");
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            this.createdPettypeResponseCode = 201;
            // Check if the status code is HTTP 200, indicating success
            return response.statusCode() == 201;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addPetWithNameAndBirthdate(String name, String birthdate) {
        String jsonInputString = String.format(
            "{\"name\": \"%s\", \"birthDate\": \"%s\", \"type\": {\"name\": \"%s\", \"id\": %d}}",
            name, birthdate, "cat", 2
        );

        try {
            // Endpoint URL
            URL url = new URL("http://localhost:9966/petclinic/api/owners/2/pets");

            // Open a connection (HttpURLConnection)
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            con.setRequestMethod("POST");

            // Set the request content-type header parameter
            con.setRequestProperty("Content-Type", "application/json; utf-8");

            // Enable input and output streams
            con.setDoOutput(true);

            // Create the Request Body
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read the Response from Input Stream
            try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            int responseCode = con.getResponseCode();
            String responseMessage = con.getResponseMessage();
            if (responseCode == 201)
            {
                this.createdPetResponseCode = 201;
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.createdPetResponseCode = 422;
        return false;
    }

    public boolean removeOwnerWithId(int ownerId)
    {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9966/petclinic/api/owners/"+ownerId)) // Replace with your actual API domain
            .DELETE() // This sets the request method to DELETE
            .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            this.removeResponseCode = 204;
            System.out.println("REMOVE RESPONSE BODY");
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addPettWithNameAndBirthdateAndPettype(String name, String birthdate, String pettype) {
        try {
            // Replace with your actual URL
            URL url = new URL("http://localhost:9966/petclinic/api/owners/" + this.createdOwnerID + "/pets");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // JSON Body for the POST request
            String jsonInputString = String.format(
                "{\"name\": \"%s\", \"birthDate\": \"%s\", \"type\": {\"name\": \"%s\"}}",
                name, birthdate, pettype
            );

            // Write JSON data to output stream
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read the response from the input stream
/*            try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }*/

            int responseCode = conn.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
                System.out.println("The pet to owner was successfully added.");
                this.createdPetResponseCode = 201;
                return true;
            } else {
                System.out.println("The request did not succeed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.createdPetResponseCode = 422;
        return false;
    }

    public boolean retrieveOwnerWithId(String ownerId)  {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:9966/petclinic/api/owners/" + ownerId)) // The owner ID is now a parameter
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("RETRIEVAL STATUS CODE");
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 200) {
                return true;
            }
        } catch (URISyntaxException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }

        return false;
    }

    public String responseMessagePet() {
        return Integer.toString(this.createdPetResponseCode);
    }
    public String responseMessageRemoval() {
        return Integer.toString(this.removeResponseCode);
    }
    public String responseMessageUpdate() {
        return Integer.toString(this.updateResponseCode);
    }
    public String responseMessagePettypecreate() {
        return Integer.toString(this.createdPettypeResponseCode);
    }


}
