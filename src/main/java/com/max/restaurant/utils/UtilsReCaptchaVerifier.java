package com.max.restaurant.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;
import static com.max.restaurant.utils.UtilsLoggerMsgs.TWO_PARAMS_MSG;

/**
 * reCaptcha-2 utility checking class. If there is no internet connection, will return true. To change this,
 * delete try section, line 41
 */
public class UtilsReCaptchaVerifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(UtilsReCaptchaVerifier.class);
    private static final String SECRET_KEY = "6LfF4qsiAAAAAMaqhgk6c7_iSAGKfyyfQdaNnyBb";
    public static final String SITE_KEY = "6LfF4qsiAAAAAI4HwgDTEbMbrGGimJXsgaLY7Y6Z";
    public static final String GOOGLE_URL = "https://www.google.com/recaptcha/api/siteverify";
    public static final String reCAPTCHA_ATTR = "g-recaptcha-response";

    private UtilsReCaptchaVerifier() {
    }

    public static boolean verify(HttpServletRequest request) throws IOException {
        LOGGER.info(METHOD_STARTS_MSG, "verify", "true");
        String gRecaptchaResponse = request.getParameter(reCAPTCHA_ATTR);
        if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
            return false;
        }
        URL connURL = new URL(GOOGLE_URL);
        HttpsURLConnection con;
        try {
            con = (HttpsURLConnection) connURL.openConnection();
        } catch (IOException e) {
            return true;
        }
        // add request header
        String userAgent = request.getHeader("User-Agent");
        String acceptLang = request.getHeader("Accept-Language");
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", userAgent);
        con.setRequestProperty("Accept-Language", acceptLang);
        String postParams = "secret=" + SECRET_KEY + "&response=" + gRecaptchaResponse;
        LOGGER.debug(TWO_PARAMS_MSG, userAgent, acceptLang);
        // Send post request to Google
        con.setDoOutput(true);
        DataOutputStream writer;
        try {
            writer = new DataOutputStream(con.getOutputStream());
        } catch (IOException e) {
            return true;
        }
        writer.writeBytes(postParams);
        writer.flush();
        writer.close();
        // get response from Google
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        LOGGER.debug(TWO_PARAMS_MSG, "response", response);
        //parse JSON response and return 'success' value
        JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        return jsonObject.getBoolean("success");
    }
}
