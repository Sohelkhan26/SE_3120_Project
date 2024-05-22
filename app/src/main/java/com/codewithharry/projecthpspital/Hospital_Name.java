package com.codewithharry.projecthpspital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Hospital_Name extends AppCompatActivity {
    DatabaseReference databaseReference= ConnectToFireBase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_name);

        final EditText Name=findViewById(R.id.hospitalNameID);
        //final EditText id=findViewById(R.id.hospitalidID);
        final EditText password=findViewById(R.id.hospitalPasswordID);
        final Button createBtn=findViewById(R.id.HospitalButtonID);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getName=Name.getText().toString();
                //final String getID=id.getText().toString();
                final String getPass=password.getText().toString();

                databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("user").child("Hospital").child(getName).child("Name").setValue(getName);
                        databaseReference.child("user").child("Hospital").child(getName).child("Password").setValue(getPass);
                        Toast.makeText(Hospital_Name.this,"Created",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}