package com.mapmyindia.smartcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONParser {
    public static String convertStreamToString(InputStream in) {
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        StringBuilder jsonstr=new StringBuilder();
        String line;
        try {
            while((line=br.readLine())!=null)
            {
                String t=line+"\n";
                jsonstr.append(t);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonstr.toString();
    }
}