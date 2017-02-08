package youthstudio.com.kegelexercises;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ExerciseDetailActivity extends AppCompatActivity {
    public static final String ITEM_POSITION = "ITEM_POSITION";
    private static final String TAG = ExerciseDetailActivity.class.getSimpleName();
    int itemPosition;
    ExerciseItem mExerciseItem;
    GifImageView mGifExerciseImage;
    WebView mWebViewHtml;
    AdView mAdViewBottom, mAdViewTop;
    private Tracker mTracker;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);
        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();

        mGifExerciseImage = (GifImageView) findViewById(R.id.exercise_gif);
        mWebViewHtml = (WebView) findViewById(R.id.exercise_html);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ///
        getExerciseItem();
        setTitle(mExerciseItem.getInstructionName());
//        try {
//            GifDrawable gifFromAssets = new GifDrawable(this.getAssets(), mExerciseItem.imageName);
//            mGifExerciseImage.setImageDrawable(gifFromAssets);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        MobileAds.initialize(this, getString(R.string.account_ad_id));
        mWebViewHtml.loadUrl("file:///android_asset/" + mExerciseItem.getInstructionFile());
        findViewAndLoadAd();
        sendEvent();
        createInterstitialAds();
        requestNewInterstitial();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdViewBottom.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdViewBottom.resume();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdViewBottom.destroy();
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                Log.i(TAG, "navigateUpFromSameTask");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findViewAndLoadAd() {
        mAdViewBottom = (AdView) findViewById(R.id.adViewBottom);
//        mAdViewTop = (AdView) rootView.findViewById(R.id.adViewTop);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("FFC6AE2794F518945A9B86AC33207508").build();
//        AdRequest adRequestTop = new AdRequest.Builder().addTestDevice("FFC6AE2794F518945A9B86AC33207508").build();
        mAdViewBottom.loadAd(adRequest);
//        mAdViewTop.loadAd(adRequestTop);
    }

    private void createInterstitialAds() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(AppController.KGIA);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                finish();
            }
        });
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("58F1A780DB0AD902EA41C82DFC34F71E")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private void getExerciseItem() {
        itemPosition = getIntent().getIntExtra(ITEM_POSITION, 0);
        List<ExerciseItem> exerciseItems = RawFileReader.getInstance(this).readExerciseData();
        mExerciseItem = exerciseItems.get(itemPosition);
    }

    private void sendEvent() {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(TAG)
                .setAction("ItemSelected")
                .setLabel(mExerciseItem.getInstructionName())
                .build());
    }
}
