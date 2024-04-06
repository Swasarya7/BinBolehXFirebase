package com.example.binbolehxfirebase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OpenAIChatbot {

    public static void main(String[] args) {
        final String exitKeyword = "exit";
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Hello, ask me anything about recycling (type 'exit' to quit): ");
                String message = scanner.nextLine();

                String[] keywords = {"recycle", "plastic", "plastics", "metal", "metals",
                        "glass", "batteries", "battery", "trash", "bin", "bins", "compost",
                        "environment", "sustainability"};
                String[] queries = {"hello", "who are you", "what are you", "what can you do",
                        "hello, who are you", "hello who are you"};

                // Check if the message contains any of the keywords
                boolean containsKeyword = false;
                for (String keyword : keywords) {
                    if (message.toLowerCase().contains(keyword.toLowerCase())) {
                        containsKeyword = true;
                        break;
                    }
                }
                boolean containsQuery = false;
                for (String query : queries) {
                    if (message.toLowerCase().contains(query.toLowerCase())) {
                        containsQuery = true;
                        break;
                    }
                }

                // Check if the user wants to exit
                if (message.equalsIgnoreCase(exitKeyword)) {
                    System.out.println("Exiting program.");
                    break;
                }

                try {
                    // Proceed only if the message contains a keyword
                    if (containsKeyword) {
                        // Here you would call the method to process the message, for example:
                        String response = chatGPT(message);
                        System.out.println(response);
                    } else if(containsQuery){
                        System.out.println("Hello, I am Boleh Bot, here to answer all your recycling questions!");
                    }else{
                        System.out.println("Apologies, but I am just a recycling chatbot. Please ask questions about recycling only.");
                    }
                } catch (RuntimeException e) {
                    System.out.println("Failed to get response from OpenAI: " + e.getMessage());
                }
            }
        }
    }

    public static String chatGPT(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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"; // API key goes here, testing
        String model = "gpt-3.5-turbo"; // current model of chatgpt api

        try {
            // Create the HTTP POST request
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            // Build the request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // returns the extracted contents of the response.
            return extractContentFromResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // This method extracts the response expected from chatgpt and returns it.
    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content")+11; // Marker for where the content starts.
        int endMarker = response.indexOf("\"", startMarker); // Marker for where the content ends.
        return response.substring(startMarker, endMarker); // Returns the substring containing only the response.
    }
}