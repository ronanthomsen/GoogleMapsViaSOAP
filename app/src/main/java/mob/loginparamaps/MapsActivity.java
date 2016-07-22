package mob.loginparamaps;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final int MY_LOCATION_REQUEST_CODE = 100;
    private boolean BUILD_MAP = true;
    private boolean FIRST_MAP = true;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    //define uma localizacao inicial para o mapa
    private void setMap() {
        //localizacao fiap
        LatLng fiappaulista = new LatLng(-23.564076, -46.6531797);
        mMap.addMarker(new MarkerOptions().position(fiappaulista).title("FIAP Paulista"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiappaulista, 17));

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

    public void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        }

        if (BUILD_MAP) {
            mMap.setMyLocationEnabled(true);
        }

    }

    private GoogleMap.OnMyLocationButtonClickListener myLocationChangeListener = new GoogleMap.OnMyLocationButtonClickListener() {

        @Override
        public boolean onMyLocationButtonClick() {

            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = service.getBestProvider(criteria, false);

            BUILD_MAP = false;
            checkPermission();

            Location location = service.getLastKnownLocation(provider);
            LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());

            mMap.addMarker(new MarkerOptions().position(loc).title("NOVO"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 17));

            return true;
        }
    };





//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng fiapPaulista = new LatLng(-23.564076, -46.6531797);
//        mMap.addMarker(new MarkerOptions().position(fiapPaulista).title("FIAP Paulista"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiapPaulista, 17));
//    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(myLocationChangeListener);

        checkPermission();

        if (FIRST_MAP) {
            setMap();
        }

        /* exemplo inicial

        //BitmapDescriptor icone = BitmapDescriptorFactory.fromResource(R.drawable.iciconemap);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-23564076, 466531797);
        LatLng fiap = new LatLng(-23.56447884, -46.65245533);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(fiap).title("FIAP MANO!!!!!"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiap,17)); */


    }

    public void voltaLogin(View v){

        Intent _voltaLogin = new Intent(this, MainActivity.class);
        startActivity(_voltaLogin);

    }
    public void localRandomico(View v){

        Random rand = new Random();
        int n = rand.nextInt(180);
        float c1 = (float) (n*0.28291);
        float c2 = (float) (n*0.4325);
        LatLng localRand = new LatLng(c1, c2);
        mMap.addMarker(new MarkerOptions().position(localRand).title("Local Randomico"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localRand, 5));

        Log.i("LOCALIDADE", "LOCAL RAND C1= "+c1+"  C2= "+c2);

    }


}
