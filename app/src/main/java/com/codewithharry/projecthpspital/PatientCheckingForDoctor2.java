package com.codewithharry.projecthpspital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PatientCheckingForDoctor2 extends AppCompatActivity {
    DatabaseReference databaseReference = ConnectToFireBase.getInstance();
    EditText diseaseEditText, reportEditText, medicineEditText;
    Button button;
    String patientName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_checking_for_doctor2);

        final TextView patientIDTextView = findViewById(R.id.PatientCheckingForDoctoridID);
        final TextView patientNameTextView = findViewById(R.id.PatientCheckingForDoctorNameID);
        diseaseEditText = findViewById(R.id.PatientCheckingForDoctorDiseaseID);
        reportEditText = findViewById(R.id.PatientCheckingForDoctorReportID);
        medicineEditText = findViewById(R.id.PatientCheckingForDoctorMedicineID);
        button=findViewById(R.id.PatientCheckingForDoctorButtonID);

        String patientID = getIntent().getStringExtra("PatientsID");
        patientIDTextView.setText(patientID);

        databaseReference.child("user").child("patient").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(patientID)) {
                    patientName = snapshot.child(patientID).child("Name").getValue(String.class);
                    patientNameTextView.setText(patientName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String disease=diseaseEditText.getText().toString();
                String report=reportEditText.getText().toString();
                String medicine=medicineEditText.getText().toString();
                updateMedicalHistory(patientID,disease,report,medicine);
            }
        });

    }

    // Add a method to update the patient's medical history
    private void updateMedicalHistory(String patientID, String disease, String report, String medicine) {
        DatabaseReference historyRef = databaseReference.child("user").child("patient").child(patientID).child("History");

        // Create a unique key for each medical history entry
        final Long[] i = new Long[1];
        databaseReference.child("user").child("patient").child(patientID).child("History").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                i[0] =snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String historyKey=Long.toString(i[0]);


        // Create a HashMap to store the medical history data
        HashMap<String, Object> medicalHistoryMap = new HashMap<>();
        medicalHistoryMap.put("DetectedDisease", disease);
        medicalHistoryMap.put("ReportResult", report);
        medicalHistoryMap.put("Medicine", medicine);

        // Update the patient's medical history under the "History" node
        historyRef.child(historyKey).setValue(medicalHistoryMap);
    }

}
