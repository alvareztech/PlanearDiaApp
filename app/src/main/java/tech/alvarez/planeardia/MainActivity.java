package tech.alvarez.planeardia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
    }

    public void adicionarLugar(View view) {

        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {

            startActivityForResult(intentBuilder.build(this), 777);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 777) {
            if (resultCode == RESULT_OK) {
                Place lugar = PlacePicker.getPlace(this, data);
                String nombre = lugar.getName().toString();
                String direccion = lugar.getAddress().toString();
                String id = lugar.getId();
                double latitud = lugar.getLatLng().latitude;
                double longitud = lugar.getLatLng().longitude;
                int tipo = lugar.getPlaceTypes().get(0);

                Log.i("PLACES", id);

                realm.beginTransaction();
                Lugar lu = realm.createObject(Lugar.class);
                lu.setId(id);
                lu.setDireccion(direccion);
                lu.setNombre(nombre);
                lu.setLatitud(latitud);
                lu.setLongitud(longitud);
                lu.setTipo(tipo);
                realm.commitTransaction();

                actualizarDatos();


//                infoTextView.setText(nombre + "\n" + direccion + "\n" + id);
            }
        }
    }

    private void actualizarDatos() {

        Log.i("MIAPP", "actualizarDatos");

        RealmResults<Lugar> realmResults = realm.where(Lugar.class).findAll();

        for (int i = 0; i < realmResults.size(); i++) {
            Lugar lu = realmResults.get(i);
            Log.i("MIAPP", "> " + lu.getNombre());
        }


    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.mapaMenuItem) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
    }
}
