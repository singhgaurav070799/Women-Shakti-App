package com.bytecode.womenshakti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GuardianApp extends AppCompatActivity {

    EditText etpe1,etpp1,etpe2,etpp2,etpe3,etpp3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_app);
        etpe1=(EditText)findViewById(R.id.etpe1);
        etpp1=(EditText)findViewById(R.id.etpn1);
        etpe2=(EditText)findViewById(R.id.etpe2);
        etpp2=(EditText)findViewById(R.id.etpn2);
        etpe3=(EditText)findViewById(R.id.etpe3);
        etpp3=(EditText)findViewById(R.id.etpn3);
    }
    public void save(View view) {
        String email1 = etpe1.getText().toString().trim();
        String phn1 = etpp1.getText().toString().trim();
        String email2 = etpe2.getText().toString().trim();
        String phn2 = etpp2.getText().toString().trim();
        String email3 = etpe1.getText().toString().trim();
        String phn3 = etpp1.getText().toString().trim();
        if(TextUtils.isEmpty(email1)){
            etpe1.setError("Email is Required.");
            return;
        }
        if(TextUtils.isEmpty(phn1)|| phn1.length()<10 || phn1.length()>10){
            etpp1.setError("Please enter valid phone number");
            return;
        }
        if(TextUtils.isEmpty(email2)){
            etpe2.setError("Email is Required.");
            return;
        }
        if(TextUtils.isEmpty(phn2) || phn2.length()<10 || phn2.length()>10){
            etpp2.setError("Please enter valid phone number");
            return;
        }
        if(TextUtils.isEmpty(email3)){
            etpe3.setError("Email is Required.");
            return;
        }
        if(TextUtils.isEmpty(phn3)|| phn3.length()<10 || phn3.length()>10){
            etpp3.setError("Please enter valid phone number");
            return;
        }
        SharedPreferences spf=getSharedPreferences("myspf",Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = spf.edit();
        spe.putString("email1",etpe1.getText().toString());
        spe.putString("phone1",etpp1.getText().toString());
        spe.putString("email2",etpe2.getText().toString());
        spe.putString("phone2",etpp2.getText().toString());
        spe.putString("email3",etpe3.getText().toString());
        spe.putString("phone3",etpp3.getText().toString());

        spe.commit();

        finish();
        Toast.makeText(GuardianApp.this, "Guardian Details Added Successfully.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(view.getContext(),MainDrawer.class);

        startActivity(intent);

    }
}