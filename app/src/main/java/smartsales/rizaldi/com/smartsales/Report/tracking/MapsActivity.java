package smartsales.rizaldi.com.smartsales.Report.tracking;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static Double lat=0.0,lon=0.0;
    static String description="";
    String idvisit="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getlatlon(String visitid) {
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_datatracking+visitid, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = null;
                        try {
                            json = jsonArray.getJSONObject(i);
                            lat=Double.parseDouble(json.getString("lat"));
                            lon=Double.parseDouble(json.getString("lng"));
                            description=json.getString("description");
                            LatLng sydney = new LatLng(lat, lon);
                            mMap.addMarker(new MarkerOptions().position(sydney).title(description));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
//                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(sydney)      // Sets the center of the map to Mountain View
                                    .zoom(17)                   // Sets the zoom
                                    .bearing(90)                // Sets the orientation of the camera to east
                                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle b=getIntent().getExtras();
        idvisit=b.getString("visitId");
        getlatlon(idvisit);
    }
}
