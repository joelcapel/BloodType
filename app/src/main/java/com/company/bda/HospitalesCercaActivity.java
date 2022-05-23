package com.company.bda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HospitalesCercaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar toolbar;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitales_cerca);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hospitales cerca");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalesCercaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng hospital1 = new LatLng( 41.383692, 2.194255);
        LatLng hospital2 = new LatLng( 41.389511, 2.152219);
        LatLng hospital3 = new LatLng( 41.389879, 2.129715);
        LatLng hospital4 = new LatLng( 41.384167, 2.101944);
        mMap.addMarker(new MarkerOptions()
                .position(hospital1)
                .title("Hospital del mar"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospital1, 13));
        mMap.addMarker(new MarkerOptions()
                .position(hospital2)
                .title("Hospital clinic de Barcelona"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospital2, 13));
        mMap.addMarker(new MarkerOptions()
                .position(hospital3)
                .title("Hospital de Barcelona"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospital3, 13));
        mMap.addMarker(new MarkerOptions()
                .position(hospital4)
                .title("Hospital Sant Joan de Deu"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospital4, 13));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}