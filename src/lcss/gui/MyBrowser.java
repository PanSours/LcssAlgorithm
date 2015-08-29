/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcss.gui;

import java.util.ArrayList;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lcss.structure.GeographicalSpots;
import lcss.structure.Structure;

/**
 *
 * @author nikolas
 */
public class MyBrowser extends Region {

    private WebView browser = null; 
    private WebEngine webEngine = null;

    /**
     * Initialize the Webview and the Web Engine to load the maps
     */
    public MyBrowser() {
        browser = new WebView();
        webEngine = browser.getEngine();
        webEngine.loadContent("");
        getChildren().add(browser);
    }
    /**
     * loads the new content
    */
    public void loadURL() {
        webEngine.loadContent(makeText());
    }

    public WebView getBrowser() {
        return browser;
    }

    public void setBrowser(WebView browser) {
        this.browser = browser;
    }

    public WebEngine getWebEngine() {
        return webEngine;
    }

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    /**
     * Creates the new content of map
     * @return 
     */
    public static String makeText() {

        ArrayList<GeographicalSpots> first = new ArrayList<>();
        ArrayList<GeographicalSpots> second = new ArrayList<>();

        first = Structure.getFirst();
        second = Structure.getSecond();
        String str;
        str = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">\n"
                + "<meta charset=\"utf-8\">\n"
                + "<title>Simple Polylines</title>\n"
                + "<style>\n"
                + "html, body, #map-canvas {\n"
                + "height: 100%;\n"
                + "margin: 0px;\n"
                + "padding: 0px\n"
                + "}\n"
                + "</style>\n"
                + "<script src=\"https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false\"></script>"
                + "\n"
                + "<script>\n"
                + "function initialize() {\n"
                + "var mapOptions = {\n"
                + "zoom: 8,\n"
                + "center: new google.maps.LatLng(39.92889, 116.38833),\n"
                + "mapTypeId: google.maps.MapTypeId.ROADMAP\n"
                + "};\n"
                + "\n"
                + "var map = new google.maps.Map(document.getElementById('map-canvas'),\n"
                + "mapOptions);\n"
                + "\n"
                + "\n"
                + "var flightPlanCoordinates = [\n"
                + getLatLon(first)
                + "];\n"
                + "\n"
                + "var flightSecondCoordinates = [\n"
                + getLatLon(second)
                + "];\n"
                + "\n"
                + "var flightPath = new google.maps.Polyline({\n"
                + "path: flightPlanCoordinates,\n"
                + "geodesic: true,\n"
                + "strokeColor: '#FF0000',\n"
                + "strokeOpacity: 1.0,\n"
                + "strokeWeight: 2\n"
                + "});\n"
                + "flightPath.setMap(map);\n"
                + "var flightPathSecond = new google.maps.Polyline({\n"
                + "path: flightSecondCoordinates,\n"
                + "geodesic: true,\n"
                + "strokeColor: '#00FFFF',\n"
                + "strokeOpacity: 1.0,\n"
                + "strokeWeight: 2\n"
                + "});\n"
                + "\n"
                + "flightPathSecond.setMap(map);\n"
                + "}\n"
                + "\n"
                + "google.maps.event.addDomListener(window, 'load', initialize);\n"
                + "\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "<div id=\"map-canvas\"></div>\n"
                + "</body>\n"
                + "</html>";

        return str;
    }

    
    public static String getLatLon(ArrayList<GeographicalSpots> list) {
        String str = "";

        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                str += "new google.maps.LatLng(" + list.get(i).getLatitude() + " , " + list.get(i).getLongitude() + "),\n";
            } else {
                str += "new google.maps.LatLng(" + list.get(i).getLatitude() + " , " + list.get(i).getLongitude() + ")\n";
            }
        }

        return str;

    }

}
