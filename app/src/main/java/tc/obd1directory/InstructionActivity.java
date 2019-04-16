package tc.obd1directory;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InstructionActivity extends AppCompatActivity {

    private LinearLayout instructionLayout;
    private DatabaseReference ref;
    private ArrayList<String>instructions;
    private String[]vehicle;
    private TextView nextButton;
    private InterstitialAd mInterstitialAd;
    private boolean wasAdShown;
    private AdView mAdView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_select);
        instructionLayout = (LinearLayout)findViewById(R.id.instructionLayout);
        nextButton = (TextView) findViewById(R.id.nextButton);
        nextButton.setClickable(true);
        wasAdShown = false;
        //removed google ad codes for git
        MobileAds.initialize(this,
                "");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        ref = FirebaseDatabase.getInstance().getReference();
        instructions = new ArrayList<>();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null){
            vehicle = (String[]) b.get("VEHICLE");
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded() && !wasAdShown) {
                    mInterstitialAd.show();
                    wasAdShown = true;
                } else {
                    wasAdShown = false;
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    Intent myIntent = new Intent(InstructionActivity.this, CodeActivity.class);
                    myIntent.putExtra("VEHICLE", vehicle);
                    InstructionActivity.this.startActivity(myIntent);
                }


            }
        });
        //create class for database reference
        ref.child("PHOTOS").child(vehicle[0]).addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    System.out.print(d.getKey());
                    instructions.add((String)d.getValue());

                }
                data(instructions);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
               //idk why i would need this portion here




            }
        });

    }

    private void data(final ArrayList<String> instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            TextView infoLabel = new TextView(InstructionActivity.this);
            infoLabel.setText((i+1) + ") " + instructions.get(i));
            infoLabel.setTextSize(20);
            infoLabel.setTextColor(Color.WHITE);
            infoLabel.setPadding(20,60,20,60);
            instructionLayout.addView(infoLabel);

        }


    }
}
