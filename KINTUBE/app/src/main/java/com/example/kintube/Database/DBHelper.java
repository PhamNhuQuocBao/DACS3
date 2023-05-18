package com.example.kintube.Database;

import com.example.kintube.Model.User.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBHelper {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public void addNewUser(String path, User user) {
        databaseReference.child(path).child(user.getId()).setValue(user);
    }
}
