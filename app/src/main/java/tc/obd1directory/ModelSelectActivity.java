package tc.obd1directory;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdView;



import java.util.ArrayList;

public class ModelSelectActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private ArrayList<String> models;
    private String brand;
    private LinearLayout buttonView;
    private TextView modelSelect;
    private InterstitialAd mInterstitialAd;
    private boolean wasAdShown;
    private AdView mAdView;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_select);
        buttonView = (LinearLayout)findViewById(R.id.buttonView);
        modelSelect = (TextView)findViewById(R.id.modelSelect);
        wasAdShown = false;
        MobileAds.initialize(this,
                "ca-app-pub-4945302572759470~1152825065");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4945302572759470/4020986398");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        ref = FirebaseDatabase.getInstance().getReference();

        models = new ArrayList<>();
        //can make separate class for this
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null){
            brand = (String) b.get("MAKE");
        }

        modelSelect.setText("Select your " + brand + ":");
        ref.child("MODEL1").child(brand).addValueEventListener(new ValueEventListener() {

            int num = 1;
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    System.out.print(d.getKey());
                    models.add(d.getKey());
                }
                data(models);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void data(final ArrayList<String>models){
        //array containing the make and model of selected vehicle
        final String[]makeModel = new String[2];
        makeModel[0] = brand;
        for (int i = 0; i < models.size(); i++){
            TextView brandButton = new TextView(ModelSelectActivity.this);
            brandButton.setId(i);
            brandButton.setText(models.get(i));
            brandButton.setClickable(true);
            brandButton.setPadding(50,100,50,100);
            brandButton.setTextColor(Color.WHITE);
            brandButton.setGravity(Gravity.CENTER);
            brandButton.setTextSize(30);

            final String n = models.get(i);
            brandButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    makeModel[1] = n;
                    if (mInterstitialAd.isLoaded() && !wasAdShown) {
                        mInterstitialAd.show();
                        wasAdShown = true;
                    } else {
                        wasAdShown = false;
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                        Intent is = new Intent(getApplicationContext(), EngineSelectActivity.class);
                        is.putExtra("MODEL", makeModel);
                        startActivity(is);
                    }

                }
            });
            buttonView.addView(brandButton);
        }
    }


}
