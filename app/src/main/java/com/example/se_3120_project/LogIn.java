package com.example.se_3120_project;

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

public class LogIn extends AppCompatActivity {
    DatabaseReference databaseReference= ConnectToFireBase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        final Button adminButton= this.<Button>findViewById(R.id.adminLogInID);
        final Button logInBtn= this.<Button>findViewById(R.id.logInButtonID);
        final Button toSignUpBtn= this.<Button>findViewById(R.id.toSignUpButtonID);
        final Button doctorBtn= this.<Button>findViewById(R.id.doctorLogInID);
        final Button hospitalBtn= this.<Button>findViewById(R.id.hospitalLogInID);

        //final Button name= this.<Button>findViewById(R.id.signUpNameID);
        final EditText card= this.<EditText>findViewById(R.id.logInCardID);
        final EditText email= this.<EditText>findViewById(R.id.logInEmailID);
        final EditText pass= this.<EditText>findViewById(R.id.logInPasswordID);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final String nameTxt=name.getText().toString();
                final String cardTxt=card.getText().toString();
                final String emailTxt=email.getText().toString();
                final String passTxt=pass.getText().toString();

                if(cardTxt.isEmpty() || emailTxt.isEmpty()|| passTxt.isEmpty())
                {
                    Toast.makeText(LogIn.this,"Please Fill All Requirement",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("user").child("patient").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(cardTxt))
                            {
                                //Toast.makeText(SignUp.this,"Already ID Exists",Toast.LENGTH_SHORT).show();
                                final String getEmail=snapshot.child(cardTxt).child("Email").getValue(String.class);
                                final String getPass=snapshot.child(cardTxt).child("Password").getValue(String.class);
                                if((getEmail.equals(emailTxt)) && (getPass.equals(passTxt)))
                                {
                                    Toast.makeText(LogIn.this,"Log in Successful",Toast.LENGTH_SHORT).show();

                                    // Revive Activity is going to be added shortly 4/25/2024

//                                    Intent intent=new Intent(LogIn.this, Revive.class);
//                                    intent.putExtra("PatientID",cardTxt);
//                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(LogIn.this,"Wrong Password ",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(LogIn.this,"ID not found",Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        toSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this,SignUp.class));
            }
        });


//        adminButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LogIn.this, Admin.class));
//            }
//        });
//
//        doctorBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LogIn.this, Doctor_Log_In.class));
//            }
//        });
//
//        hospitalBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LogIn.this, HospitalLogIN.class));
//            }
//        });
    }
}