package com.codewithharry.projecthpspital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DoctorDetails extends AppCompatActivity {
    DatabaseReference databaseReference = ConnectToFireBase.getInstance();
    TextView textView, textView1, textView2, textView3;
    Button button, datebtn;
    boolean hasObtainedSerial = false;
    String doctorID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        textView = findViewById(R.id.doctordetailsID);
        textView1 = findViewById(R.id.doctorserialdateID);
        textView2 = findViewById(R.id.doctorserialNumberID);
        textView3= this.<TextView>findViewById(R.id.doctordetailsPatientSerialOrderID);
        button = findViewById(R.id.doctorserialID);
        datebtn = findViewById(R.id.doctorserialdatebtnID);

        Intent intent = getIntent();
        String name = intent.getStringExtra("A");
        doctorID = intent.getStringExtra("B");
        String patientID=intent.getStringExtra("PatientID");
        textView.setText(name);

        checkAndMoveData();
        setSerialNumber();

        // Toast.makeText(DoctorDetails.this, "You have already obtained a serial.", Toast.LENGTH_SHORT).show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment").child("Tomorrow").child("Serial").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(patientID))
                        {
                            Toast.makeText(DoctorDetails.this, "The Patient is already appointed for the next day", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment").child("Tomorrow").child("Serial").child(patientID).setValue(patientID);
                            databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment").child("Tomorrow").child("Date").setValue(getNextDate());

                            Toast.makeText(DoctorDetails.this, "Your appointment number is ", Toast.LENGTH_SHORT).show();
                            setSerialNumber();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                textView1.setText(currentDate);
            }
        });
    }

    private String getNextDate() {
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DAY_OF_YEAR, 1);
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calender.getTime());
    }
    private void setSerialNumber() {
        databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment").child("Tomorrow").child("Serial").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long appointmentsCount = snapshot.getChildrenCount();
                textView2.setText(String.valueOf(appointmentsCount));

                // Update the doctor's serial number with the count of appointments for the specific day
                databaseReference.child("user").child("Doctors").child(doctorID).child("Serial").setValue(appointmentsCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DoctorDetails.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkAndMoveData() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        DatabaseReference appointmentsRef = databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment");

        // Check if the date in "Tomorrow" matches the current date
        appointmentsRef.child("Tomorrow").child("Date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tomorrowDate = snapshot.getValue(String.class);
                Toast.makeText(DoctorDetails.this, tomorrowDate+ "OUTSIDE IF", Toast.LENGTH_SHORT).show();
                if (tomorrowDate != null && tomorrowDate.equals(currentDate)) {
                    // Move data from "Tomorrow" to "Today"
                    moveData("Tomorrow", "Today");

                    // Clear the "Tomorrow" node
                    //clearNode("Tomorrow");
                    Toast.makeText(DoctorDetails.this, "inside if " + currentDate, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }



     private void moveData(String sourceNode, String destinationNode) {
        DatabaseReference sourceRef = databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment").child(sourceNode).child("Serial");
        DatabaseReference destinationRef = databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment").child(destinationNode).child("Serial");

        sourceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Move data from source to destination
                destinationRef.setValue(snapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Successfully moved data
                            Toast.makeText(DoctorDetails.this, "Successfully moved data from " + sourceNode + " to " + destinationNode, Toast.LENGTH_SHORT).show();
                             databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment").child("Tomorrow").child("Serial").setValue(0);

                            // Clear the source node after moving data
                            //clearNode(sourceNode);
                        } else {
                            // Handle error
                            Toast.makeText(DoctorDetails.this, "Error in moving data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(DoctorDetails.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment").child("Tomorrow").child("Date").setValue(getNextDate());
        databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment").child("Today").child("Date").setValue(getCurrentDate());

       // databaseReference.child("user").child("Doctors").child(doctorID).child("Appointment").child("Tomorrow").child("Serial").setValue(0);
        finish();
    }
    private String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }
}
