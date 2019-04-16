package tc.obd1directory;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EngineSelectActivity extends AppCompatActivity {

    private LinearLayout buttonView;
    private ArrayList<String> engines;
    String[] model;
    DatabaseReference ref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_select);
        buttonView = (LinearLayout)findViewById(R.id.buttonlayout);
        ref = FirebaseDatabase.getInstance().getReference();
        engines = new ArrayList<>();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null){
            model = (String[]) b.get("MODEL");
        }
        //Branch >> model1 >> model[0] = make >> model[1] = model
        ref.child("MODEL1").child(model[0]).child(model[1]).addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    System.out.print(d.getKey());
                    engines.add(d.getKey());
                }
                data(engines);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void data(ArrayList<String> engines) {
        final String[]makeModelMotor = new String[3];
        makeModelMotor[0] = model[0];
        for (int i = 0; i < engines.size(); i++){
            TextView engineButton = new TextView(EngineSelectActivity.this);
            engineButton.setId(i);
            engineButton.setText(engines.get(i));
            engineButton.setClickable(true);
            engineButton.setPadding(50,100,50,100);
            engineButton.setTextColor(Color.WHITE);
            engineButton.setGravity(Gravity.CENTER);
            engineButton.setTextSize(30);
            makeModelMotor[1] = model[1];

            final String n = engines.get(i);
            engineButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    makeModelMotor[2] = n;
                    Intent is = new Intent(getApplicationContext(), MenuActivity.class);
                    is.putExtra("VEHICLE", makeModelMotor);
                    startActivity(is);
                }
            });
            buttonView.addView(engineButton);
        }

    }
}
