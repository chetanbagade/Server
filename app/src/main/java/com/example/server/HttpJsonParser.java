package com.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

public class HttpJsonParser {
//static provid memory at the run time but before the execution
    static InputStream is = null;   // abtract class extends the object
    static JSONObject jObj = null; //A modifiable set of name/value mappings. Names are unique, non-null strings.
    static String json = "";   // strings of json
    HttpURLConnection urlConnection = null; //represent a communications link between the  aplication and a URL.p


    // function get json from url
    // by making HTTP POST or GET method
    //
    public JSONObject makeHttpRequest(String url, String method,
                                      Map<String, String> params) {

        try {
            Uri.Builder builder = new Uri.Builder();  //URI we vary the implementation depending on what the user passes in.
            URL urlObj;  // obj of url
            String encodedParams = ""; //object of string
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.appendQueryParameter(entry.getKey(), entry.getValue());
                }
            }
            // from .getEncodedQueary Gets the decoded fragment part of this URI
            if (builder.build().getEncodedQuery() != null) {
                encodedParams =  builder.build().getEncodedQuery();

            }
            if ("GET".equals(method)) {
                url = url + "?" + encodedParams;
                urlObj = new URL(url);
                urlConnection = (HttpURLConnection) urlObj.openConnection(); //except that the connection will be made through the specified proxy;
                                                                            // Protocol handlers that do notsupport proxing will ignore the proxy parameter and make a
                                                                            //normal connection.
                urlConnection.setRequestMethod(method);


            } else {
                urlObj = new URL(url);              //
                urlConnection = (HttpURLConnection) urlObj.openConnection();
                urlConnection.setRequestMethod(method);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(encodedParams.getBytes().length));
                urlConnection.getOutputStream().write(encodedParams.getBytes());
            }


            urlConnection.connect();
            is = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));  //at the set buffer it contains the temporory value like
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            jObj = new JSONObject(json);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        } catch (Exception e) {
            Log.e("Exception", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}