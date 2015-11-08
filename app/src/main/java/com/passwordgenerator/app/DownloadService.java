package com.passwordgenerator.app;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DownloadService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";


    public DownloadService() {
        super(DownloadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        try {
            Log.i(TAG,"BG THREAD SLEEPING");
            Thread.sleep((1000 * 60));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i(TAG,"BG THREAD WOKEN UP");

        removePasswordFromClipboard();

        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private void removePasswordFromClipboard(){

        ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(getApplicationContext().getString(R.string.app_name), null));
        Log.i("TAG", "CLIPBOARD PASSWORD REMOVED");
    }




    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
