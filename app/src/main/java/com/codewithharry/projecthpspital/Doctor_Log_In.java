package com.codewithharry.projecthpspital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Doctor_Log_In extends AppCompatActivity {
    DatabaseReference databaseReference= ConnectToFireBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_log_in);
        final EditText id= this.<EditText>findViewById(R.id.doctorLogInidID);
        final EditText password= this.<EditText>findViewById(R.id.doctorLogInPasswordID);
        final Button LogInBtn= this.<Button>findViewById(R.id.doctorLogInButtonID);
        final Button RegBtn= this.<Button>findViewById(R.id.DoctorRegisterFromLogINID);
        final Spinner spinner= this.<Spinner>findViewById(R.id.spinnedoctorLogInID);
        final TextView textView=findViewById(R.id.texviewdoctorLogInID);
        final String[] selectedValue = new String[1];


        databaseReference.child("user").child("Hospital").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String>stringList =new ArrayList<>();

                for(DataSnapshot childSnapshot: snapshot.getChildren())
                {
                    String data=childSnapshot.child("Name").getValue(String.class);
                    if(data!=null)
                    {
                        stringList.add(data);
                    }
                }

                String[] strings=stringList.toArray(new String[0]);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Doctor_Log_In.this, android.R.layout.simple_spinner_item,strings);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedValue[0] =(String) adapterView.getItemAtPosition(i);
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

        LogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getID =id.getText().toString();
                String getPass=password.getText().toString();

                databaseReference.child("user").child("Hospital").child(selectedValue[0]).child("Doctors").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(getID))
                        {
                            //databaseReference.child("user").child("Hospital").child(selectedValue[0]).child("Doctors").child(getID).child("Name").setValue(getName);
                            //databaseReference.child("user").child("Hospital").child(selectedValue[0]).child("Doctors").child(getID).child("Password").
                            final String passTxt=snapshot.child(getID).child("Password").getValue(String.class);
                            final String nameTxt=snapshot.child(getID).child("Name").getValue(String.class);
                            if(passTxt.equals(getPass))
                            {
                                Toast.makeText(Doctor_Log_In.this, "Welcome Dr. "+nameTxt, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Doctor_Log_In.this, Doctors_Own_Page.class);
                                intent.putExtra("HospitalName",selectedValue[0]);
                                intent.putExtra("DoctorID",getID);
                                intent.putExtra("DoctorName",nameTxt);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(Doctor_Log_In.this, "Doctor ID isn't Exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Doctor_Log_In.this, Doctor.class));
            }
        });
    }
}