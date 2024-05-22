package com.codewithharry.projecthpspital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Hospital extends AppCompatActivity {
    DatabaseReference databaseReference= ConnectToFireBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        final EditText id= this.<EditText>findViewById(R.id.setdoctorID);
        final Button btn= this.<Button>findViewById(R.id.setDoctorBtnID);
        final TextView textView= this.<TextView>findViewById(R.id.hospitalmanageID);

        String msg=getIntent().getStringExtra("ami jacchi baba");
        textView.setText(msg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getID=id.getText().toString();

                databaseReference.child("user").child("Hospital").child(msg).child("Doctors").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(getID))
                        {
                            Toast.makeText(Hospital.this, "Already Doctor Registered", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            databaseReference.child("user").child("Hospital").child(msg).child("Doctors").child(getID).child("ID").setValue(getID);
                            Toast.makeText(Hospital.this, "Doctor ID Registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Hospital.this, Doctor.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}