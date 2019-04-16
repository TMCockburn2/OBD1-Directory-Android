package tc.obd1directory;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CodeActivity extends AppCompatActivity {

    private String[]vehicle;
    private DatabaseReference ref;
    private ArrayList<String> codes;
    private LinearLayout view;
    private TextView title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_select);
        view = (LinearLayout)findViewById(R.id.codeLayout);
        title = (TextView)findViewById(R.id.title);
        ref = FirebaseDatabase.getInstance().getReference();
        codes = new ArrayList<>();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null){
            vehicle = (String[]) b.get("VEHICLE");
        }
        title.setText("Engine codes for your " + vehicle[0] + " " + vehicle[1] + " " + vehicle[2]);
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

    private void data(ArrayList<String> codes) {
        for (int i = 0; i < codes.size(); i++) {
            TextView codeLabel = new TextView(CodeActivity.this);
            codeLabel.setText(createIndentedText(codes.get(i),20,170));
            codeLabel.setTextSize(20);
            codeLabel.setTextColor(Color.WHITE);
            codeLabel.setPadding(0,60,20,60);
            view.addView(codeLabel);

        }
    }
    //text indent
    static SpannableString createIndentedText(String text, int marginFirstLine, int marginNextLines) {
        SpannableString result=new SpannableString(text);
        result.setSpan(new LeadingMarginSpan.Standard(marginFirstLine, marginNextLines),0,text.length(),0);
        return result;
    }
}
