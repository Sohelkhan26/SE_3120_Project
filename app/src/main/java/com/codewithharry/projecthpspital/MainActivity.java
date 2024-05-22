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

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference= ConnectToFireBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner spinner= this.<Spinner>findViewById(R.id.patientSpinnerID);
        final String[] selectedValue = new String[1];


        databaseReference.child("user").child("Hospital").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String>stringList =new ArrayList<>();
                stringList.add("CLICK ME");
                for(DataSnapshot childSnapshot: snapshot.getChildren())
                {
                    String data=childSnapshot.child("Name").getValue(String.class);
                    if(data!=null)
                    {
                        stringList.add(data);
                    }
                }

                String[] strings=stringList.toArray(new String[0]);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item,strings);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedValue[0] =(String) adapterView.getItemAtPosition(i);
                        //startActivity(new Intent(MainActivity.this, Hospital.class));
                        if(selectedValue[0]!="CLICK ME")
                        {
                            Toast.makeText(MainActivity.this, "I'm Clicked " + selectedValue[0], Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, Hospital.class);
                            intent.putExtra("ami jacchi baba", selectedValue[0]);
                            startActivity(intent);
                        }
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

        /*spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }
}