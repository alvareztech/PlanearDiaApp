package tech.alvarez.planeardia;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import io.realm.Realm;
import io.realm.RealmResults;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        realm = Realm.getDefaultInstance();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        RealmResults<Lugar> realmResults = realm.where(Lugar.class).findAll();

        PolylineOptions polylineOptions = new PolylineOptions()
                .color(ContextCompat.getColor(this, R.color.colorPrimary))
                .width(8);

        for (int i = 0; i < realmResults.size(); i++) {
            Lugar lu = realmResults.get(i);
            Log.i("MIAPP", "> " + lu.getNombre());

            LatLng punto = new LatLng(lu.getLatitud(), lu.getLongitud());
            mMap.addMarker(new MarkerOptions().
                    position(punto).
                    title(lu.getNombre())
                    .snippet(lu.getDireccion())
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, 11));

            polylineOptions.add(punto);
        }

        mMap.addPolyline(polylineOptions);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
