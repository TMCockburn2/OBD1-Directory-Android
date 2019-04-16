package tc.obd1directory;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseCall {
    private DatabaseReference ref;
    ArrayList<String>codes;



    public ArrayList<String>getData(String[]vehicle){
        ref = FirebaseDatabase.getInstance().getReference();
        codes = new ArrayList<>();
        ref.child("MODEL1").child(vehicle[0]).child(vehicle[1]).child(vehicle[2]).addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    System.out.print(d.getKey());
                    codes.add((String)d.getValue());
                }
                data(codes);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
