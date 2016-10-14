package com.mapmyindia.smartcity;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;

import java.net.URL;

import io.krumbs.sdk.KIntentPanelConfiguration;
import io.krumbs.sdk.KrumbsSDK;
import io.krumbs.sdk.KrumbsUser;
import io.krumbs.sdk.data.model.Media;
import io.krumbs.sdk.krumbscapture.KMediaUploadListener;

public class KrumbsCamera extends Application   {

    public static final String KRUMBS_SDK_APPLICATION_ID = "io.krumbs.sdk.APPLICATION_ID";
    public static final String KRUMBS_SDK_CLIENT_KEY = "io.krumbs.sdk.CLIENT_KEY";
    public static final String SDK_STARTER_PROJECT_USER_FN = "Faisal";
    public static final String SDK_STARTER_PROJECT_USER_SN = "Public";

    @Override
    public void onCreate() {
        super.onCreate();

        String appID = getMetadata(getApplicationContext(), KRUMBS_SDK_APPLICATION_ID);
        String clientKey = getMetadata(getApplicationContext(), KRUMBS_SDK_CLIENT_KEY);
        if (appID != null && clientKey != null) {
            KrumbsSDK.initialize(getApplicationContext(), appID, clientKey);
            KMediaUploadListener kMediaUploadListener = new KMediaUploadListener() {
                // onMediaUpload listens to various status of media upload to the cloud.
                @Override
                public void onMediaUpload(String id, KMediaUploadListener.MediaUploadStatus mediaUploadStatus,
                                          Media.MediaType mediaType, URL mediaUrl) {
                    if (mediaUploadStatus != null) {
                        Log.i("KRUMBS-BROADCAST-RECV", mediaUploadStatus.toString());
                        if (mediaUploadStatus == KMediaUploadListener.MediaUploadStatus.UPLOAD_SUCCESS) {
                            if (mediaType != null && mediaUrl != null) {
                                Log.i("KRUMBS-BROADCAST-RECV", mediaType + ": " + id + ", " + mediaUrl);
                            }
                        }
                    }
                }
            };
            KrumbsSDK.setKMediaUploadListener(this, kMediaUploadListener);

            try {
                String assetPath = "IntentResourcesReacho";
                KrumbsSDK.registerIntentCategories(assetPath);
                KIntentPanelConfiguration defaults = KrumbsSDK.getIntentPanelViewConfigurationDefaults();
                KIntentPanelConfiguration.IntentPanelCategoryTextStyle ts = defaults.getIntentPanelCategoryTextStyle();
                ts.setTextColor(Color.YELLOW);
                KIntentPanelConfiguration newDefaults = new KIntentPanelConfiguration.KIntentPanelConfigurationBuilder()
                        .intentPanelBarColor("#029EE1")
                        .intentPanelTextStyle(ts)
                        .build();
                KrumbsSDK.setIntentPanelConfiguration(String.valueOf(getTheme()), newDefaults);
                String userEmail = "email";
                KrumbsSDK.registerUser(new KrumbsUser.KrumbsUserBuilder()
                        .email(userEmail)
                        .firstName(SDK_STARTER_PROJECT_USER_FN)
                        .lastName(SDK_STARTER_PROJECT_USER_SN).build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getMetadata(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString(name);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return null;
    }
}