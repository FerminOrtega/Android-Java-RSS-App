package com.example.ferminortegadominguez.playground;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

@SuppressLint("Registered")
public class DownloadXML extends AppCompatActivity {
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "null";
    public static String URL = "";
    public static List<PGPortadaXMLParser.Item> articulos;
    public static boolean bandera;

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;
    public static String sPref = null;
    public static String categoria = "";

    public static void loadPage() {

        new DownloadXmlTask().execute(URL);

        //new DownloadXmlTask().execute(URL);
        /**
         *

         if ((sPref.equals(ANY)) && (wifiConnected || mobileConnected)) {

         } else if ((sPref.equals(WIFI)) && (wifiConnected)) {

         } else {
         // show error
         }
         */
    }


    private static class DownloadXmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                //return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                System.out.println(e.getMessage());
                //return getResources().getString(R.string.xml_error);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
            //setContentView(R.layout.activity_main);
            // Displays the HTML string in the UI via a WebView
            //WebView myWebView = (WebView) findViewById(R.id.webview);
            //myWebView.loadData(result, "text/html", null);
        }
    }

    public static InputStream stream;
    public static PGPortadaXMLParser pgPortadaXMLParser;
    public static List<PGPortadaXMLParser.Item> items;

    private static String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        if (articulos != null) {
            articulos.removeAll(articulos);
        }
        if (items != null) {
            items.removeAll(items);
        }

        stream = null;
        // Instantiate the parser
        pgPortadaXMLParser = new PGPortadaXMLParser();

        items = null;
        try {

            stream = downloadUrl(urlString);
            items = pgPortadaXMLParser.parse(stream);
            articulos = items;
        } finally {
            if (stream == null) {

                Objects.requireNonNull(stream).close();
            }
        }


        bandera = true;
        return "Correcto";
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    public static URL url;
    public static HttpsURLConnection conn;

    private static InputStream downloadUrl(String urlString) throws IOException {

        url = new URL(urlString);

        conn = (HttpsURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");

        conn.setDoInput(true);
        // Starts the query

        conn.connect();

        return conn.getInputStream();
    }
}
