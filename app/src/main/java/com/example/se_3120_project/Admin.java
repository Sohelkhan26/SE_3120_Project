package com.example.se_3120_project;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.se_3120_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin extends AppCompatActivity {
    DatabaseReference databaseReference= ConnectToFireBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        final EditText id= this.<EditText>findViewById(R.id.adminidID);
        final EditText pass= this.<EditText>findViewById(R.id.adminPasswordID);
        final Button adminBtn= this.<Button>findViewById(R.id.adminButtonID);

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String idTxt=id.getText().toString();
                final String passTxt=pass.getText().toString();

                if(idTxt.isEmpty() || passTxt.isEmpty())
                {
                    Toast.makeText(Admin.this,"Fill Both Info, please! ",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("user").child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(idTxt))
                            {
                                final String getPass=snapshot.child(idTxt).child("Password").getValue(String.class);
                                if(getPass.equals(passTxt))
                                {
                                    Toast.makeText(Admin.this,"Successfully Logged In",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Admin.this, Hospital_Name.class));
                                }
                                else
                                {
                                    Toast.makeText(Admin.this,"Password Wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(Admin.this,"Admin not found",Toast.LENGTH_SHORT).show();

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