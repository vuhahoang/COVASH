package com.example.covash_demo;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapFragment extends Fragment  {
    GoogleMap map;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getContext(), R.raw.mapstyle));

                    if (!success) {
                        Log.e("Maps", "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("Maps", "Can't find style. Error: ", e);
                }
                map = googleMap;
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                String apiURL = "https://disease.sh/v2/countries";
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                        apiURL,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                try {

                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject countryJSONObject = response.getJSONObject(i);
                                        JSONObject LLJSON = countryJSONObject.getJSONObject("countryInfo");
                                        int cases = countryJSONObject.getInt("cases");
                                        double lat = LLJSON.getDouble("lat");
                                        double longg = LLJSON.getDouble("long");
                                        LatLng latLng = new LatLng(lat, longg);
                                        map.addCircle(new CircleOptions().center(latLng)
                                                .radius((double) cases/20)
                                                .strokeColor(0xffff0000)
                                                .fillColor(0x220000FF)
                                                .strokeWidth(5));
                                    }


                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                requestQueue.add(jsonArrayRequest);
            }
        });


        return view;
    }

   
}