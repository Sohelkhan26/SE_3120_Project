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

public class SignUp extends AppCompatActivity {
    DatabaseReference databaseReference= ConnectToFireBase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Button adminButton= this.<Button>findViewById(R.id.adminSignUpID);
        final Button signUPbutton= this.<Button>findViewById(R.id.signUpButtonID);
        final Button toLogINbutton= this.<Button>findViewById(R.id.toLogInButtonID);

        final EditText name= this.<EditText>findViewById(R.id.signUpNameID);
        final EditText card= this.<EditText>findViewById(R.id.signUpCardID);
        final EditText email= this.<EditText>findViewById(R.id.signUpEmailID);
        final EditText pass= this.<EditText>findViewById(R.id.signUpPasswordID);

        signUPbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nameTxt=name.getText().toString();
                final String cardTxt=card.getText().toString();
                final String emailTxt=email.getText().toString();
                final String passTxt=pass.getText().toString();

                if(nameTxt.isEmpty()|| cardTxt.isEmpty() || emailTxt.isEmpty()|| passTxt.isEmpty())
                {
                    Toast.makeText(SignUp.this,"Please Fill All Requirement",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("user").child("patient").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(cardTxt))
                            {
                                Toast.makeText(SignUp.this,"Already ID Exists",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                databaseReference.child("user").child("patient").child(cardTxt).child("Name").setValue(nameTxt);
                                databaseReference.child("user").child("patient").child(cardTxt).child("Email").setValue(emailTxt);
                                databaseReference.child("user").child("patient").child(cardTxt).child("Password").setValue(passTxt);
                                databaseReference.child("user").child("patient").child(cardTxt).child("Appointment");
                                Toast.makeText(SignUp.this,"Successfully Registered",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        toLogINbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,LogIn.class));
            }
        });

    }
}