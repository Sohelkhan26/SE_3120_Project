package com.codewithharry.projecthpspital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class HospitalLogIN extends AppCompatActivity {
    DatabaseReference databaseReference= ConnectToFireBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_log_in);

        final Button loginBtn= this.<Button>findViewById(R.id.LogINHospitalButtonID);

        final EditText Name= this.<EditText>findViewById(R.id.LogINhospitalNameID);
        final EditText password= this.<EditText>findViewById(R.id.LogINhospitalPasswordID);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getName=Name.getText().toString();
                String getPass=password.getText().toString();
                
                if(getName.isEmpty() || getPass.isEmpty())
                {
                    Toast.makeText(HospitalLogIN.this, "Fill All the All", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("user").child("Hospital").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(getName))
                            {
                                //final String NameTxt=snapshot.child(cardTxt).child("Email").getValue(String.class);
                                final String PassTxt=snapshot.child(getName).child("Password").getValue(String.class);
                                if((PassTxt.equals(getPass)))
                                {
                                    Toast.makeText(HospitalLogIN.this,"Log in Successful",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(HospitalLogIN.this, Hospital.class);
                                    intent.putExtra("ami jacchi baba",getName);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(HospitalLogIN.this,"Wrong Password ",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(HospitalLogIN.this,"ID not found",Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
}