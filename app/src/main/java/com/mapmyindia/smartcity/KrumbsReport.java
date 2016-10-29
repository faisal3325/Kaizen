package com.mapmyindia.smartcity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.MapView;

import io.krumbs.sdk.KrumbsSDK;
import io.krumbs.sdk.dashboard.KDashboardFragment;
import io.krumbs.sdk.dashboard.KGadgetDataTimePeriod;
import io.krumbs.sdk.dashboard.KGadgetType;
import io.krumbs.sdk.krumbscapture.settings.KUserPreferences;

import static io.krumbs.sdk.dashboard.KGadgetDataTimePeriod.LAST_12_MONTHS;
import static io.krumbs.sdk.dashboard.KGadgetDataTimePeriod.LAST_24_HOURS;
import static io.krumbs.sdk.dashboard.KGadgetDataTimePeriod.LAST_30_DAYS;
import static io.krumbs.sdk.dashboard.KGadgetDataTimePeriod.TODAY;

public class KrumbsReport extends AppCompatActivity {

    private KGadgetDataTimePeriod defaultInitialTimePeriod = KGadgetDataTimePeriod.TODAY;
    private KDashboardFragment kDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preloadMaps();

        setContentView(R.layout.activity_krumbs_report);
        if (savedInstanceState == null) {
            kDashboard = buildDashboard();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, kDashboard).commit();
        }
        KrumbsSDK.setUserPreferences(
                new KUserPreferences.KUserPreferencesBuilder().audioRecordingEnabled(true).build());
    }

    private void preloadMaps() {
        // hack to load mapsgadget faster: http://stackoverflow
        // .com/questions/26265526/what-makes-my-map-fragment-loading-slow
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                } catch (Exception ignored){
                    Log.e("KRUMBS-ERROR", "error while init maps/ google play serv");
                }
            }
        });
        // alternatively: http://stackoverflow.com/questions/26178212/first-launch-of-activity-with-google-maps-is-very-slow
    }

    private KDashboardFragment buildDashboard() {
        return new KDashboardFragment.KDashboardBuilder()
                .addGadget(KGadgetType.REPORTS)
                .addGadget(KGadgetType.PEOPLE)
                .addGadget(KGadgetType.TOP_INTENTS)
                .addGadget(KGadgetType.TOP_PLACES)
                .timePeriod(defaultInitialTimePeriod).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_krumbs, menu);
        switch (defaultInitialTimePeriod) {
            case TODAY:
                menu.findItem(R.id.last_day).setChecked(true);
                break;
            case LAST_24_HOURS:
                menu.findItem(R.id.last_24h).setChecked(true);
                break;
            case LAST_30_DAYS:
                menu.findItem(R.id.last_month).setChecked(true);
                break;
            case LAST_12_MONTHS:
                menu.findItem(R.id.last_year).setChecked(true);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.last_day:
                defaultInitialTimePeriod = TODAY;
                break;
            case R.id.last_24h:
                defaultInitialTimePeriod = LAST_24_HOURS;
                break;
            case R.id.last_month:
                defaultInitialTimePeriod = LAST_30_DAYS;
                break;
            case R.id.last_year:
                defaultInitialTimePeriod = LAST_12_MONTHS;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        //send notification to the SDK to update the Dashboard
        if (kDashboard != null) {
            kDashboard.refreshDashboard(defaultInitialTimePeriod);
        }
        return true;
    }

    //    http://stackoverflow.com/questions/7469082/getting-exception-illegalstateexception-can-not-perform-this-action-after-onsa
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
}