package com.codewithharry.projecthpspital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Revive extends AppCompatActivity {

    DatabaseReference databaseReference = ConnectToFireBase.getInstance();
    Spinner spinner;
    ArrayList<String> doctorNames = new ArrayList<>(),IDs=new ArrayList<>();
    ListView listView;
    TextView textView;
    String patientID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revive);

        listView = this.<ListView>findViewById(R.id.patientListID);
        textView= this.<TextView>findViewById(R.id.patientTextID);
        spinner=findViewById(R.id.patientSpinnerID);

        // Set up a listener for hospital selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String selectedHospital = (String) adapterView.getItemAtPosition(i);

                // Clear the doctorNames list when a new hospital is selected
                doctorNames.clear();
                textView.setText(selectedHospital);
                // Retrieve doctors for the selected hospital
                databaseReference.child("user").child("Hospital").child(selectedHospital).child("Doctors")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                                    String doctorName = doctorSnapshot.child("Name").getValue(String.class);
                                    String id=doctorSnapshot.child("ID").getValue(String.class);
                                    if (doctorName != null) {
                                        doctorNames.add(doctorName);
                                        IDs.add(id);
                                    }
                                }

                                // Update the ListView with the doctor names
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Revive.this, android.R.layout.simple_list_item_1, doctorNames);
                                listView.setAdapter(arrayAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle errors here
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case where nothing is selected in the Spinner
            }
        });

        // Retrieve the list of hospitals for the Spinner
        databaseReference.child("user").child("Hospital").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> hospitalNames = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String hospitalName = childSnapshot.child("Name").getValue(String.class);
                    if (hospitalName != null) {
                        hospitalNames.add(hospitalName);
                    }
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Revive.this, android.R.layout.simple_spinner_item, hospitalNames);
                spinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1=getIntent();
                patientID=intent1.getStringExtra("PatientID");
                Intent intent=new Intent(Revive.this, DoctorDetails.class);
                intent.putExtra("A",doctorNames.get(position));
                intent.putExtra("B", IDs.get(position));
                intent.putExtra("PatientID",patientID);
                startActivity(intent);
            }
        });
    }
}
