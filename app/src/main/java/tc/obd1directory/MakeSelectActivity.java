package tc.obd1directory;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MakeSelectActivity extends AppCompatActivity {

    private LinearLayout buttonLayout;
    private ArrayList<String> makes;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_select);
        ref = FirebaseDatabase.getInstance().getReference();
        makes = new ArrayList<>();
        buttonLayout = (LinearLayout)findViewById(R.id.buttonLayout);

        ref.child("MODEL1").addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    System.out.print(d.getKey());
                    makes.add(d.getKey());
                }
                data(makes);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void data(ArrayList<String> makes) {
        for (int i = 0; i < makes.size(); i++){
            TextView brand = new TextView(MakeSelectActivity.this);
            brand.setId(i);
            brand.setText(makes.get(i));
            brand.setTextSize(30);
            brand.setClickable(true);
            brand.setPadding(50,100,50,100);
            brand.setTextColor(Color.WHITE);
            brand.setGravity(Gravity.CENTER);
            final String n = makes.get(i);
            brand.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent is = new Intent(getApplicationContext(), ModelSelectActivity.class);
                    is.putExtra("MAKE", n);
                    startActivity(is);
                }
            });
            buttonLayout.addView(brand);
        }
    }
}
