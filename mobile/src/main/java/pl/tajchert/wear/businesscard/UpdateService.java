package pl.tajchert.wear.businesscard;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import pl.tajchert.wear.businesscard.util.PreferencesSaver;
import pl.tajchert.wear.businesscard.util.Tools;
import pl.tajchert.wear.businesscard.util.ValuesCons;

public class UpdateService extends Service {
    private static final String TAG = UpdateService.class.getSimpleName();
    private PreferencesSaver prefs;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        prefs = new PreferencesSaver(this);
        new SendImageTask().execute();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }



    private class SendImageTask extends AsyncTask<String, Void, String> {
        private GoogleApiClient mGoogleAppiClient;
        private Bitmap qrCode;
        @Override
        protected String doInBackground(String... params) {
            try {
                qrCode = Tools.makeQRCode(prefs.getStringValue(ValuesCons.PREFS_QR_CONTENT));
            } catch (Exception e) {
                qrCode = null;//TODO check if it is even possible to get exception here
            }
            return "Executed";//TODO use string to pass result/or message
        }
        @Override
        protected void onPostExecute(String result) {
            if(qrCode != null){
                sendImage(qrCode);
            }
            //TODO else send error message
        }
        @Override
        protected void onPreExecute() {
            mGoogleAppiClient = new GoogleApiClient.Builder(UpdateService.this)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle connectionHint) {
                        }
                        @Override
                        public void onConnectionSuspended(int cause) {
                        }
                    }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                        }
                    })
                    .addApi(Wearable.API)
                    .build();
            mGoogleAppiClient.connect();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

        private void sendData(String key, String value) {
            if(value == null || value.length() < 3){
                //Empty string, do not send, 3 is for 'hot' word
                return;
            }
            value = value +  Calendar.getInstance().getTimeInMillis();
            PutDataMapRequest dataMap = PutDataMapRequest.create(ValuesCons.WEAR_PATH);
            dataMap.getDataMap().putString(key, value);
            PutDataRequest request = dataMap.asPutDataRequest();
            PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleAppiClient, request);
            pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {
                    mGoogleAppiClient.disconnect();
                    stopSelf();
                }
            });
        }

        private void sendImage(Bitmap bitmap){
            Asset asset = createAssetFromBitmap(bitmap);
            PutDataMapRequest dataMap = PutDataMapRequest.create(ValuesCons.WEAR_PATH);
            dataMap.getDataMap().putAsset(ValuesCons.WEAR_QR_CODE, asset);
            PutDataRequest request = dataMap.asPutDataRequest();
            PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                    .putDataItem(mGoogleAppiClient, request);
            pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {
                    mGoogleAppiClient.disconnect();
                    stopSelf();
                }
            });
        }

        private Asset createAssetFromBitmap(Bitmap bitmap) {
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 45, byteStream);
            return Asset.createFromBytes(byteStream.toByteArray());
        }
    }
}
