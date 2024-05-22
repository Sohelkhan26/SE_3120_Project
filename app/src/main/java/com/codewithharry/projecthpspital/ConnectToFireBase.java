package com.codewithharry.projecthpspital;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConnectToFireBase {
    public static DatabaseReference databaseReference = null;
    public static DatabaseReference getInstance() {
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://se-3120-project-default-rtdb.firebaseio.com/");
        }
        return databaseReference;
    }
}
