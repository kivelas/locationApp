package fi.kivelas.locationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView latitudeReadout, longitudeReadout, distance;
    Button buttonLocation, buttonDistance;
    EditText latitude, longitude;
    FusedLocationProviderClient fusedLocationClient;
    protected LocationRequest locationRequest;
    protected Location location;
    protected LocationManager locationManager;
    protected Context context;
    protected LocationListener locationListener;
    LocationCallback locationCallback;
    double latitudeNumber, longitudeNumber;
    double givenLatitude, givenLongitude, distanceResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeReadout = (TextView) findViewById(R.id.latitude_readout);
        longitudeReadout = (TextView) findViewById(R.id.longitude_readout);
        distance = (TextView) findViewById(R.id.distance_result);

        buttonLocation = (Button) findViewById(R.id.location_button);
        buttonDistance = (Button) findViewById(R.id.distance_button);

        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);

        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testLocation();
            }
        });

        buttonDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateDistance();
            }
        });
    }

    public void testLocation() {
        Log.i("test", "inside testLocation");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                latitudeNumber = location.getLatitude();
                longitudeNumber = location.getLongitude();
                latitudeReadout.setText("Latitude: " + latitudeNumber);
                longitudeReadout.setText("Longitude: " + longitudeNumber);
            }
        });

    }

    public void calculateDistance() {
        givenLatitude = Double.parseDouble(latitude.getText().toString());
        givenLongitude = Double.parseDouble(longitude.getText().toString());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                DecimalFormat df2 = new DecimalFormat("#.##");
                Location newLocation = new Location("newlocation");
                newLocation.setLatitude(givenLatitude);
                newLocation.setLongitude(givenLongitude);
                distanceResult = location.distanceTo(newLocation) /1000;

                distance.setText("Distance: " + df2.format(distanceResult) + " km");
            }
        });
    }
}