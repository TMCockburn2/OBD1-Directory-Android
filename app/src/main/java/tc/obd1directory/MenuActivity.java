package tc.obd1directory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdView;

import org.w3c.dom.Text;

public class MenuActivity extends AppCompatActivity {

    private TextView titleLabel;
    private TextView toCodes;
    private TextView toInstructions;
    String[]vehicle;
    private InterstitialAd mInterstitialAd;
    private boolean wasAdShown;
    private AdView mAdView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_select);
        titleLabel = (TextView)findViewById(R.id.titleLabel);
        toCodes = (TextView) findViewById(R.id.toCodes);
        toCodes.setClickable(true);
        toInstructions = (TextView) findViewById(R.id.toInstructions);
        toInstructions.setClickable(true);
        vehicle = new String[3];
        wasAdShown = false;
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
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null){
                vehicle = (String[]) b.get("VEHICLE");
        }
        titleLabel.setText(vehicle[0] + " " + vehicle[1]);
        toCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded() && !wasAdShown) {
                    mInterstitialAd.show();
                    wasAdShown = true;
                } else {
                    wasAdShown = false;
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    Intent myIntent = new Intent(MenuActivity.this, CodeActivity.class);
                    myIntent.putExtra("VEHICLE", vehicle);
                    MenuActivity.this.startActivity(myIntent);
                }


            }
        });
        toInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MenuActivity.this, InstructionActivity.class);
                myIntent.putExtra("VEHICLE", vehicle);
                MenuActivity.this.startActivity(myIntent);

            }
        });

    }

}
