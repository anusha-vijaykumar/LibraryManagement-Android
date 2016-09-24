package com.example.anusha.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.client.Response;

public class BufferReaderOutput {



    public static String BufferReaderOutput(Response response) {
        String output = "";
        try {

            BufferedReader r = new BufferedReader(new InputStreamReader(response.getBody().in()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                result.append(line);
            }
            output=result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

}
