package com.example.eriko.updateditmmockup.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eriko.updateditmmockup.interfaces.CustomerVolleyArrayCallback;
import com.example.eriko.updateditmmockup.interfaces.VolleyArrayListCallback;
import com.example.eriko.updateditmmockup.interfaces.VolleyJsonObjectCallback;
import com.example.eriko.updateditmmockup.interfaces.VolleyStringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RESTManager {

    private Context context;

    private RequestQueue requestQueue;

    public static int requests = 0;
    public static int projects = 0;
    public static boolean isMultiApp = false;

    public RESTManager(Context context, RequestQueue requestQueue) {
        this.context = context;
        this.requestQueue = requestQueue;
    }

    public Bitmap stringToBitmap(String image) {
        try {
            byte[] encodedByte = Base64.decode(image.getBytes(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void getJsonObjectFromUrl(final String url, final VolleyJsonObjectCallback callback) {
        requests += 1;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void getStringFromJsonObjectFromUrl(final String url, final String string, final VolleyStringCallback callback) {
        requests += 1;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onSuccess(response.getString(string));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void getStringFromJsonArrayFromJsonObjectFromUrl(final String url, final String string, final VolleyStringCallback callback) {
        requests += 1;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONArray jsonArray = response.getJSONArray("Apps");
                    //String string = jsonArray.get(number).toString();
                    callback.onSuccess(response.getJSONArray("Apps").getJSONObject(0).getString(string));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void getStringFromJsonArrayFromUrl(final String url, final String string, final VolleyArrayListCallback callback) {
        requests += 1;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++) {
                        arrayList.add(response.getJSONObject(i).getString(string));
                    }
                    callback.onSuccess(arrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void getCustomerStringFromJsonArrayFromUrl(final String url, final String string, final CustomerVolleyArrayCallback callback) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++) {
                        arrayList.add(response.getJSONObject(i).getString(string));
                    }
                    callback.onSuccess(arrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFailed(string);
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}