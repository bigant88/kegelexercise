package youthstudio.com.kegelexercises;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


/**
 * Created by thaodv on 8/23/16.
 */
public class RatingDialog {
    private static final long MIN_TIME_USING_APP_TO_SHOW_DIALOG = 1000 * 60 * 2; //5 mins
    private static final String PREF_CLICKED_RATE = "PREF_CLICKED___RATE";
    private static final String PREF_CLICKED_LATER = "PREF_CLICKED__LATER";
    private static final String PREF_CLICKED_NOT_GREAT = "PREF_CLICKED_NOT__GREAT";
    private static final String FEEDBACK_EMAIL_ADDRESS = "android.dev.hn2@gmail.com";
    private static final String FEEDBACK_SUBJECT = "Feedback ";

    private static final String TAG = RatingDialog.class.getSimpleName();
    private boolean isClickedRate, isClickedLater, isClickedNotGreate;
    private long startUsingAppTime;
    private Activity hostActivity;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPref;
    private MaterialDialog dialog;

    public void onCreate(Activity activity) {
        this.hostActivity = activity;
        startUsingAppTime = System.currentTimeMillis();
        mPref = PreferenceManager.getDefaultSharedPreferences(activity);
        mEditor = mPref.edit();
        getPreferences();
    }

    private void getPreferences() {
        isClickedRate = mPref.getBoolean(PREF_CLICKED_RATE, false);
        isClickedLater = mPref.getBoolean(PREF_CLICKED_LATER, false);
        isClickedNotGreate = mPref.getBoolean(PREF_CLICKED_NOT_GREAT, false);
    }

    private boolean isUsingAppLongEnough() {
        long passedTime = System.currentTimeMillis() - startUsingAppTime;
        if (passedTime > MIN_TIME_USING_APP_TO_SHOW_DIALOG) {
            return true;
        } else return false;
    }



    public boolean shouldShowAwesomePopup() {
        if (isClickedNotGreate || isClickedRate || !isUsingAppLongEnough()) {
            return false;
        } else return true;
    }

    public void showAwesomePopup() {
        MaterialDialog awesomeDialog = new MaterialDialog.Builder(hostActivity)
                .title(R.string.how_is_the_app)
                .items(R.array.greatOrNot)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which){
                            case 0:
                                showRatingPopup();
                                break;
                            case 1:
                                isClickedNotGreate = true;
                                mEditor.putBoolean(PREF_CLICKED_NOT_GREAT, true).commit();
                                showFeedbackPopup();
                                break;
                        }
                        return true; // allow selection
                    }
                })
//                .positiveText(R.string.great)
                .negativeText(R.string.cancel)
//                .neutralText(R.string.cancel)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        switch (which) {
                            case POSITIVE://great
                                showRatingPopup();
                                break;
                            case NEGATIVE://not great
                                isClickedNotGreate = true;
                                mEditor.putBoolean(PREF_CLICKED_NOT_GREAT, true).commit();
                                showFeedbackPopup();
                                break;
                            case NEUTRAL: //later
                                isClickedLater = true;
                                startUsingAppTime = System.currentTimeMillis();
                                mEditor.putBoolean(PREF_CLICKED_LATER, true).commit();
                                break;
                        }
                    }
                }).build();
        awesomeDialog.show();
    }

    public void showFeedbackPopup() {
        MaterialDialog feedbackDialog = new MaterialDialog.Builder(hostActivity)
                .content(R.string.ask_to_leave_feedback)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        switch (which) {
                            case POSITIVE://ok
                                sendFeedbackEmail();
                                break;
                            case NEGATIVE://do not show

                                break;

                        }
                    }
                }).build();
        feedbackDialog.show();
    }

    public void sendFeedbackEmail(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{FEEDBACK_EMAIL_ADDRESS});
        i.putExtra(Intent.EXTRA_SUBJECT, FEEDBACK_SUBJECT + hostActivity.getString(R.string.app_name));
        i.putExtra(Intent.EXTRA_TEXT   , "");
        try {
            hostActivity.startActivity(Intent.createChooser(i, hostActivity.getString(R.string.send_mail)));
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }

    private void showRatingPopup() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(hostActivity)
//                    .title(R.string.rate_inmapz)
                    .content(R.string.will_rate_inmapz)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.later)
//                    .neutralText(R.string.later)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            switch (which) {
                                case POSITIVE://ok
                                    isClickedRate = true;
                                    mEditor.putBoolean(PREF_CLICKED_RATE, true).commit();
                                    gotoStore();
                                    break;
//                                case NEGATIVE://do not show
//                                    isClickedNotGreate = true;
//                                    mEditor.putBoolean(PREF_CLICKED_NOT_GREAT, true).commit();
//                                    break;
                                case NEGATIVE: //later
                                    isClickedLater = true;
                                    startUsingAppTime = System.currentTimeMillis();
                                    mEditor.putBoolean(PREF_CLICKED_LATER, true).commit();
                                    break;
                            }
                        }
                    }).build();
        }
        dialog.show();
    }

    private void gotoStore() {
        final String appPackageName = hostActivity.getPackageName();// from Context or Activity object
        try {
            hostActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            hostActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));

        }
    }

}
