package com.cabipassenger.route;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.cabipassenger.R;
import com.cabipassenger.fragments.BookTaxiNewFrag;
import com.cabipassenger.route.JSONParser;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Draw the route to the map object .
 * Routes are drawn with attributes according to the constructor its triggered.
 */
public class Route {
    GoogleMap mMap;
    Context context;
    String lang;
    int colorcode;
    static String LANGUAGE_SPANISH = "es";
    static String LANGUAGE_ENGLISH = "en";
    static String LANGUAGE_FRENCH = "fr";
    static String LANGUAGE_GERMAN = "de";
    static String LANGUAGE_CHINESE_SIMPLIFIED = "zh-CN";
    static String LANGUAGE_CHINESE_TRADITIONAL = "zh-TW";

    public boolean drawRoute(GoogleMap map, Context c, ArrayList<LatLng> points, boolean withIndications, String language, boolean optimize) {
        mMap = map;
        context = c;
        lang = language;
        if (points.size() == 2) {
            String url = makeURL(points.get(0).latitude, points.get(0).longitude, points.get(1).latitude, points.get(1).longitude, "driving");
            // new connectAsyncTask(url, withIndications).execute();
            return true;
        } else if (points.size() > 2) {
            String url = makeURL(points, "driving", optimize);
            // new connectAsyncTask(url, withIndications).execute();
            return true;
        }
        return false;
    }

    public boolean drawRoute(GoogleMap map, Context c, ArrayList<LatLng> points, String language, boolean optimize) {
        mMap = map;
        context = c;
        lang = language;
        if (points.size() == 2) {
            String url = makeURL(points.get(0).latitude, points.get(0).longitude, points.get(1).latitude, points.get(1).longitude, "driving");
            // new connectAsyncTask(url, false).execute();
            return true;
        } else if (points.size() > 2) {
            String url = makeURL(points, "driving", optimize);
            // new connectAsyncTask(url, false).execute();
            return true;
        }
        return false;
    }

    public String GetDistanceTime(Context c, ArrayList<LatLng> points, String language, boolean optimize) {
        context = c;
        lang = language;
        if (points.size() == 2) {
            String url = makeURL(points.get(0).latitude, points.get(0).longitude, points.get(1).latitude, points.get(1).longitude, "driving");
            // new connectAsyncTask(url, false).execute();
            return url;
        } else if (points.size() > 2) {
            String url = makeURL(points, "driving", optimize);
            // new connectAsyncTask(url, false).execute();
            return url;
        }
        return "";
    }

    public boolean drawRoute(GoogleMap map, Context c, ArrayList<LatLng> points, String mode, boolean withIndications, String language, boolean optimize) {
        mMap = map;
        context = c;
        lang = language;
        if (points.size() == 2) {
            String url = makeURL(points.get(0).latitude, points.get(0).longitude, points.get(1).latitude, points.get(1).longitude, mode);
            // new connectAsyncTask(url, withIndications).execute();
            return true;
        } else if (points.size() > 2) {
            String url = makeURL(points, mode, optimize);
            // new connectAsyncTask(url, withIndications).execute();
            return true;
        }
        return false;
    }

    //
    public void drawRoute(GoogleMap map, Context c, LatLng source, LatLng dest, boolean withIndications, String language) {
        mMap = map;
        context = c;
        String url = makeURL(source.latitude, source.longitude, dest.latitude, dest.longitude, "driving");
        // new connectAsyncTask(url, withIndications).execute();
        lang = language;
    }

    public void drawRoute(GoogleMap map, Context c, LatLng source, LatLng dest, String language, int color) {
        Log.i("Pickup", "" + source);
        Log.i("Drop", "" + dest);
        mMap = map;
        context = c;
        colorcode = color;
        try {
            String url = makeURL(source.latitude, source.longitude, dest.latitude, dest.longitude, "driving");
            new connectAsyncTask(url, false, colorcode).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lang = language;
    }

    public void drawRoute(GoogleMap map, Context c, LatLng source, LatLng dest, String language, int color, boolean fromongoing) {
        Log.i("Pickup", "" + source);
        Log.i("Drop", "" + dest);
        mMap = map;
        context = c;
        colorcode = color;
        try {
            String url = makeURL(source.latitude, source.longitude, dest.latitude, dest.longitude, "driving");
            new connectAsyncTask(url, false, colorcode, fromongoing).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lang = language;
    }

    public void drawRoute(GoogleMap map, Context c, LatLng source, LatLng dest, String mode, boolean withIndications, String language) {
        mMap = map;
        context = c;
        String url = makeURL(source.latitude, source.longitude, dest.latitude, dest.longitude, mode);
        // new connectAsyncTask(url, withIndications).execute();
        lang = language;
    }

    /**
     * construct url needs to call for the google route api
     *
     * @param points   --> Latlong points
     * @param mode     -->mode (eg: driving,walk)
     * @param optimize --> wheater route need to be optimized
     * @return --> url in string format
     */
    private String makeURL(ArrayList<LatLng> points, String mode, boolean optimize) {
        StringBuilder urlString = new StringBuilder();
        if (mode == null)
            mode = "driving";
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(points.get(0).latitude);
        urlString.append(',');
        urlString.append(points.get(0).longitude);
        urlString.append("&destination=");
        urlString.append(points.get(points.size() - 1).latitude);
        urlString.append(',');
        urlString.append(points.get(points.size() - 1).longitude);
        urlString.append("&waypoints=");
        if (optimize)
            urlString.append("optimize:true|");
        urlString.append(points.get(1).latitude);
        urlString.append(',');
        urlString.append(points.get(1).longitude);
        for (int i = 2; i < points.size() - 1; i++) {
            urlString.append('|');
            urlString.append(points.get(i).latitude);
            urlString.append(',');
            urlString.append(points.get(i).longitude);
        }
        urlString.append("&sensor=true&mode=" + mode);
        //change this key with buisness key
        urlString.append("&key=" + context.getString(R.string.googleID));
        //	urlString.append("&key=AIzaSyCCnBymNHMMNg1fTM78pWCuR-NzkHdFE3s");
        System.out.println("urlstring " + urlString);
        return urlString.toString();
    }

    private String makeURL(double sourcelat, double sourcelog, double destlat, double destlog, String mode) {
        StringBuilder urlString = new StringBuilder();
        if (mode == null)
            mode = "driving";
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=" + mode + "&alternatives=true&language=" + lang);
        //change this key with buisness key
        urlString.append("&key=" + context.getString(R.string.googleID));
        System.out.println("urlstring " + urlString);
        return urlString.toString();
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng(((lat / 1E5)), ((lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private boolean fromOngoing;
        // private ProgressDialog progressDialog;
        String url;
        boolean steps;
        int colorroot;

        connectAsyncTask(String urlPass, boolean withSteps, int rootcolor) {
            url = urlPass;
            steps = withSteps;
            colorroot = rootcolor;
        }

        connectAsyncTask(String urlPass, boolean withSteps, int rootcolor, boolean ongoing) {
            url = urlPass;
            steps = withSteps;
            colorroot = rootcolor;
            fromOngoing = ongoing;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //TO Check weather it from ongoing or booktaxinewfrag
            System.out.println("drawing route" + BookTaxiNewFrag.booking_state);
            if (result != null)
                if (fromOngoing || BookTaxiNewFrag.booking_state != BookTaxiNewFrag.BOOKING_STATE.STATE_ONE) {
                    drawPath(result, steps, colorroot);
                }
        }
    }

    private void drawPath(String result, boolean withSteps, int colorCode) {
        try {
            System.out.println("drawing Route1");
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                // Polyline line =
                mMap.addPolyline(new PolylineOptions().add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude)).width(4).color(colorCode).geodesic(true));
            }
            if (withSteps) {
                JSONArray arrayLegs = routes.getJSONArray("legs");
                JSONObject legs = arrayLegs.getJSONObject(0);
                JSONArray stepsArray = legs.getJSONArray("steps");
                // put initial point
                for (int i = 0; i < stepsArray.length(); i++) {
                    Step step = new Step(stepsArray.getJSONObject(i));
                    mMap.addMarker(new MarkerOptions().position(step.location).title(step.distance).snippet(step.instructions).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Class that represent every step of the directions. It store distance, location and instructions
     */
    private class Step {
        public String distance;
        public LatLng location;
        public String instructions;

        Step(JSONObject stepJSON) {
            JSONObject startLocation;
            try {
                distance = stepJSON.getJSONObject("distance").getString("text");
                startLocation = stepJSON.getJSONObject("start_location");
                location = new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng"));
                try {
                    instructions = URLDecoder.decode(Html.fromHtml(stepJSON.getString("html_instructions")).toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}