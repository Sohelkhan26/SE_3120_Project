package com.codewithharry.projecthpspital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// ... (imports and package statement)

public class Doctors_Own_Page extends AppCompatActivity {
    DatabaseReference databaseReference = ConnectToFireBase.getInstance();

    ArrayList<String> patientNames = new ArrayList<>(); // Updated from IDs to patientNames
    ArrayList<String> IDs = new ArrayList<>();

    TextView doctorName, doctorsNurseName;


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_own_page);

        doctorName = findViewById(R.id.doctorNameID);
        doctorsNurseName = findViewById(R.id.doctorsAppointedNurseID);
        listView = findViewById(R.id.doctorOwnListID);


        Intent intent = getIntent();
        final String doctorHospitalName = intent.getStringExtra("HospitalName");
        final String doctorID = intent.getStringExtra("DoctorID");
        final String getdoctorName = intent.getStringExtra("DoctorName");

        doctorName.setText(getdoctorName);

        databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment").child("Today").child("Serial")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot patientSnapshot : snapshot.getChildren()) {
                            String patientID = patientSnapshot.getKey();
                            String patientName = "Get the patient name here"; // You need to fetch the patient name
                            if (patientName != null) {
                                patientNames.add(patientName);
                                IDs.add(patientID);
                            }
                        }

                        // Update the ListView with the patient names
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Doctors_Own_Page.this, android.R.layout.simple_list_item_1, IDs);
                        listView.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors here
                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(Doctors_Own_Page.this, PatientCheckingForDoctor2.class);
                intent1.putExtra("PatientsID", IDs.get(position)); // Corrected reference to intent1
                startActivity(intent1);
            }
        });
    }
}
