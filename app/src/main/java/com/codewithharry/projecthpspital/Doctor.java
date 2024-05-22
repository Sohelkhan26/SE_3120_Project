package com.codewithharry.projecthpspital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Doctor extends AppCompatActivity {
    DatabaseReference databaseReference = ConnectToFireBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        final EditText Name = findViewById(R.id.doctorNameID);
        final EditText id = findViewById(R.id.doctoridID);
        final EditText password = findViewById(R.id.doctorPasswordID);
        final Button createBtn = findViewById(R.id.doctorButtonID);
        final Spinner spinner = findViewById(R.id.spinneID);
        final TextView textView = findViewById(R.id.texviewID);
        final String[] selectedValue = new String[1];

        databaseReference.child("user").child("Hospital").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> stringList = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String data = childSnapshot.child("Name").getValue(String.class);
                    if (data != null) {
                        stringList.add(data);
                    }
                }

                String[] strings = stringList.toArray(new String[0]);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Doctor.this, android.R.layout.simple_spinner_item, strings);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedValue[0] = (String) adapterView.getItemAtPosition(i);
                        textView.setText(selectedValue[0]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getName = Name.getText().toString();
                String getID = id.getText().toString();
                String getPass = password.getText().toString();

                databaseReference.child("user").child("Hospital").child(selectedValue[0]).child("Doctors").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(getID)) {
                            databaseReference.child("user").child("Hospital").child(selectedValue[0]).child("Doctors").child(getID).child("Name").setValue(getName);
                            databaseReference.child("user").child("Hospital").child(selectedValue[0]).child("Doctors").child(getID).child("Password").setValue(getPass);
                            databaseReference.child("user").child("Doctors").child(getID).child("ID").setValue(getID);
                            databaseReference.child("user").child("Doctors").child(getID).child("Name").setValue(getName);
                            databaseReference.child("user").child("Doctors").child(getID).child("Serial");
                            databaseReference.child("user").child("Doctors").child(getID).child("Appointment").child("Today").child("Date").setValue(getCurrentDate());
                            databaseReference.child("user").child("Doctors").child(getID).child("Appointment").child("Tomorrow").child("Date").setValue(getNextDate());
                            databaseReference.child("user").child("Doctors").child(getID).child("Appointment").child("Today").child("Serial1").setValue(null);
                            databaseReference.child("user").child("Doctors").child(getID).child("Appointment").child("Tomorrow").child("Serial2").setValue(null);

                            Toast.makeText(Doctor.this, "Successfully Doctor ID Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Doctor.this, "Doctor ID isn't permitted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    private String getNextDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }
}
