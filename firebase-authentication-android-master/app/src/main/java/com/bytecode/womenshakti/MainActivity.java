package com.bytecode.womenshakti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.seismic.ShakeDetector;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity implements LocationListener, ShakeDetector.Listener{
    public static String mno1,mno2,mno3=null;
    public static final int REQUEST_CALL = 1;
    public static final int REQUEST_SMS = 0;
    private static final int GALLERY_INTENT_CODE = 1023;
    DrawerLayout drawerLayout = null;
    TextView fullName, email;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    // Button resendCode;
    // Button resetPassLocal;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;
    LocationManager locationManager;
   String tvCity, tvState, tvCountry, tvPin, tvLocality;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector shakeDetector = new ShakeDetector(this);
        shakeDetector.start(sensorManager);

        drawerLayout = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        fullName = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

//        tvCity = findViewById(R.id.tvCity);
//        tvState = findViewById(R.id.tvState);
//        tvCountry = findViewById(R.id.tvCountry);
//        tvPin = findViewById(R.id.tvPin);
//        tvLocality = findViewById(R.id.tvLocality);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationEnabled();
        getLocation();
        // MenuItem logout=findViewById(R.id.logout);
       /*phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);*/
        //  resetPassLocal = findViewById(R.id.resetPasswordLocal);

        profileImage = findViewById(R.id.navImage);
        /*changeProfileImage = findViewById(R.id.changeProfile);*/
        /*   gaurdinaDetails= findViewById(R.id.guardian_details);*/

        Button btncall = (Button) findViewById(R.id.BtnCall);
        btncall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendmessage();
                makephonecall();
                getLocation();
            }
        });
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                NavigationView navigationView = findViewById(R.id.navigation_view);
                View view = navigationView.getHeaderView(0);
                ImageView profileImage1=  view.findViewById(R.id.navImage);
                Picasso.get().load(uri).into(profileImage1);
            }
        });
        //resendCode = findViewById(R.id.resendCode);
        // verifyMsg = findViewById(R.id.verifyMsg);


        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

       /* if(!user.isEmailVerified()){
            verifyMsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent " + e.getMessage());
                        }
                    });
                }
            });
        }
*/


        DocumentReference documentReference = fStore.collection("users").document(userId);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
//                    phone.setText(documentSnapshot.getString("phone"));
                  /*  fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));*/
                    NavigationView navigationView = findViewById(R.id.navigation_view);
                    View view = navigationView.getHeaderView(0);
                    TextView navUserName = (TextView) view.findViewById(R.id.userName);
                    TextView navUserEmail = (TextView) view.findViewById(R.id.userEmail);
                    navUserName.setText(documentSnapshot.getString("fName"));
                    navUserEmail.setText(documentSnapshot.getString("email"));
                    Toast.makeText(getApplicationContext(), documentSnapshot.getString("fName"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), documentSnapshot.getString("email"), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

       /* resetPassLocal = findViewById(R.id.resetPasswordLocal);
        resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPassword = new EditText(v.getContext());

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter New Password > 6 Characters long.");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Password Reset Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // close
                    }
                });

                passwordResetDialog.create().show();

            }
        });
*/
        /*changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent i = new Intent(v.getContext(),EditProfile.class);
                i.putExtra("fullName",fullName.getText().toString());
                i.putExtra("email",email.getText().toString());
                i.putExtra("phone",phone.getText().toString());
                startActivity(i);
//

            }
        });*/
       /* gaurdinaDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent i = new Intent(v.getContext(),GuardianApp.class);

                startActivity(i);
//

            }
        });*/






   /* public void logout(MenuItem menu) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
*/

    }
    @Override
    public void hearShake() {
        // open camea
        // referesh the app
        // do any custum business logic
        Toast.makeText(this, "Shake has been detected!!", Toast.LENGTH_SHORT).show();
        sendmessage();
        makephonecall();
        getLocation();
    }

    public void makephonecall() {
        SharedPreferences sharedPreferences = getSharedPreferences("myspf", Context.MODE_PRIVATE);
        String mno = sharedPreferences.getString("phone1", "no value");
        Toast.makeText(this, "Mobile no. is" + mno, Toast.LENGTH_SHORT).show();
        if (mno.trim().length() > 0) {
            //checking for request
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                //if request not granted then request for permission
                ActivityCompat.requestPermissions(MainActivity.this, new
                        String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + sharedPreferences.getString("phone1", "no value")));
                Toast.makeText(this, "Mobile no. is" + mno, Toast.LENGTH_SHORT).show();

                //write the logic to call
                startActivity(intent);
            }
        } else {
            Toast.makeText(MainActivity.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            makephonecall();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == REQUEST_SMS) {
            myMessage();
            Toast.makeText(this, "testing only on request permission", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendmessage() {
        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            myMessage();
            Toast.makeText(this, "send button" + checkPermission, Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
        }
    }

    public void myMessage() {
        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences sharedPreferences = getSharedPreferences("myspf", Context.MODE_PRIVATE);
             mno1 = sharedPreferences.getString("phone1", "no value");
             mno2 = sharedPreferences.getString("phone2", "no value");
             mno3 = sharedPreferences.getString("phone3", "no value");
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mno1, null, "Help I am in danger", null, null);
            smsManager.sendTextMessage(mno2, null, "Help I am in danger", null, null);
            smsManager.sendTextMessage(mno3, null, "Help I am in danger", null, null);
            Toast.makeText(this, "Message sent" + "Help I am in danger" + " " + mno1, Toast.LENGTH_SHORT).show();
            //smsManager.sendTextMessage(phoneNumber, null, message, null, null, 0); } else { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void gaurdianDetails(MenuItem menuItem) {
        startActivity(new Intent(getApplicationContext(), GuardianApp.class));
    }
    public void video(MenuItem menuItem){
        startActivity(new Intent(getApplicationContext(), Video.class));
    }

    public void updaProfile(MenuItem menuItem) {
        startActivity(new Intent(getApplicationContext(), EditProfile.class));
    }
    public void profile(MenuItem menuItem){
        startActivity(new Intent(getApplicationContext(), Profile.class));
    }

    public void logout(MenuItem item) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

           /* tvCity.setText(addresses.get(0).getLocality());
            tvState.setText(addresses.get(0).getAdminArea());
            tvCountry.setText(addresses.get(0).getCountryName());
            tvPin.setText(addresses.get(0).getPostalCode());
            tvLocality.setText(addresses.get(0).getAddressLine(0));*/
            tvCity= addresses.get(0).getLocality();
            tvState=addresses.get(0).getAdminArea();
            tvCountry=addresses.get(0).getCountryName();
            tvPin=addresses.get(0).getPostalCode();
            tvLocality=addresses.get(0).getAddressLine(0);
            SmsManager smsManager = SmsManager.getDefault();
            StringBuffer smsBody=new StringBuffer();
            smsBody.append("http://maps.google.com?q=");
            smsBody.append(location.getLatitude());
            smsBody.append(",");
            smsBody.append(location.getLongitude());
            smsManager.sendTextMessage(mno1, null, "Help I am in danger" + tvLocality+" "+tvCity+
                    " "+tvState+" "+tvCountry+" "+tvPin+" "+smsBody.toString(), null, null);
            smsManager.sendTextMessage(mno2, null, "Help I am in danger" + tvLocality+" "+tvCity+
                    " "+tvState+" "+tvCountry+" "+tvPin+" "+smsBody.toString(), null, null);
            smsManager.sendTextMessage(mno3, null, "Help I am in danger" + tvLocality+" "+tvCity+
                    " "+tvState+" "+tvCountry+" "+tvPin+" "+smsBody.toString(), null, null);


        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

