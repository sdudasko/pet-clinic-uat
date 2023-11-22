package org.springframework.samples.petclinic.rest.controller;

import java.net.*;
import java.io.*;

//public class RegisterPetOwnerFixture {
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getTelephone() {
//        return telephone;
//    }
//
//    public void setTelephone(String telephone) {
//        this.telephone = telephone;
//    }
//
//    public String firstName;
//    public String lastName;
//    public String address;
//    public String city;
//    public String telephone;
//
////    public RegisterPetOwnerFixture(String[] values)
////    {
////
////    }
//
//    // Example method to register an owner
//    // It should contain the logic to make an HTTP POST request to the application to create a new owner
//    public boolean registerOwner() throws IOException {
//        // Construct the URL for the registration endpoint
//        String apiUrl = "http://localhost:9966/petclinic/api/owners";
//        URL url = new URL(apiUrl);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json; utf-8");
//        conn.setRequestProperty("Accept", "application/json");
//        conn.setDoOutput(true);
//
//        // Create the JSON string from the input fields
//        String jsonInputString = String.format(
//            "{\"firstName\": \"%s\", \"lastName\": \"%s\", \"address\": \"%s\", \"city\": \"%s\", \"telephone\": \"%s\"}",
//            this.firstName, this.lastName, this.address, this.city, this.telephone);
//
//        // Write the JSON string to the request body
//        try(OutputStream os = conn.getOutputStream()) {
//            byte[] input = jsonInputString.getBytes("utf-8");
//            os.write(input, 0, input.length);
//        }
//        System.out.println("RESPONSE:");
//        System.out.println(conn.getErrorStream());
//
//        System.out.println("RESPONSE3:");
//        System.out.println(conn.getResponseCode());
//
//        System.out.println("RESPONSE4:");
//        System.out.println(conn.getContent());
//
//        System.out.println("RESPONSE1:");
//        System.out.println(conn.getResponseMessage());
//
//        System.out.println("RESPONSE2:");
//        System.out.println(conn.getOutputStream());
//
//        // Read the response code to determine the result of the registration attempt
//        int responseCode = conn.getResponseCode();
//
//        // For simplicity, let's assume a 200 OK response means the API call was successful.
//        return responseCode == 200;
//    }
//
//    public static void main(String[] args) {
//        try {
//            String ownerId = "1"; // Replace with the actual owner ID
//            String apiUrl = "http://localhost:9966/petclinic/api/owners/" + ownerId;
//            URL url = new URL(apiUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Accept", "application/json");
//
//            int responseCode = conn.getResponseCode();
//            System.out.println("GET Response Code :: " + responseCode);
//
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String inputLine;
//                StringBuffer response = new StringBuffer();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                // print result
//                System.out.println(response.toString());
//            } else {
//                System.out.println("GET request not worked");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class PetOwnerActions {

    String firstName;
    String lastName;
    String address;
    String city;
    String telephone;
    private int ownerId; // To store the ID of the registered owner

    public PetOwnerActions(String firstName, String lastName, String address, String city, String telephone)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.telephone = telephone;
    }

    // Method to register a new pet owner and store their ID
    public boolean registerWithNameAndAddress(String name, String address) throws IOException {

        System.out.println("input param name: ");
        System.out.println(name);

        System.out.println("input param address: ");
        System.out.println(address);

        URL url = new URL("http://localhost:9966/petclinic/api/owners");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        System.out.println("CALLED REGISTER OWNER");
        String jsonInputString = String.format(
            "{\"firstName\": \"%s\", \"lastName\": \"%s\", \"address\": \"%s\", \"city\": \"%s\", \"telephone\": \"%s\"}",
            this.firstName, this.lastName, this.address, this.city, this.telephone
        );

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Read the response to get the owner ID from the JSON response (if created)
        if (conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                // You'll need to parse the JSON response to extract the owner ID
                // This is just a placeholder for where you would do that.
                ownerId = extractOwnerIdFromResponse(response.toString());

                System.out.println("OWNER ID");
                System.out.println(ownerId);
                return true;
            }
        } else {
            System.out.println("FALSE");
            return false;
        }
    }

    // Method to check if an owner exists by their last name and compare with stored ID
    public boolean checkOwnerExists(int ownerId) throws IOException {
        URL url = new URL("http://localhost:9966/petclinic/api/owners?lastName=" + lastName);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                // You'll need to parse the JSON response to check if the owner ID matches
                // This is just a placeholder for where you would do that.
                return checkIfOwnerIdMatches(response.toString(), ownerId);
            }
        } else {
            return false;
        }
    }

    // Dummy method to extract the owner ID from the JSON response
    private int extractOwnerIdFromResponse(String jsonResponse) {
        // Parse the JSON response and extract the owner ID
        // Placeholder logic
        return 1; // Assuming 1 is the ID returned from the API
    }

    // Dummy method to check if the owner ID matches the expected ID
    private boolean checkIfOwnerIdMatches(String jsonResponse, int expectedId) {
        // Parse the JSON response and check if it contains the expected owner ID
        // Placeholder logic
        return true; // Assuming the check passes
    }
}
