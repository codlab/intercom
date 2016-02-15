package eu.codlab.intercom;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by kevinleperf on 15/02/16.
 */
public abstract class AbstractWearListenerService extends WearableListenerService {

    private GoogleApiClient mGoogleApiClient;

    public AbstractWearListenerService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public final void onMessageReceived(@NonNull MessageEvent messageEvent) {

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.blockingConnect();

        if (messageEvent.getPath() == null) {
            return;
        }

        String path = messageEvent.getPath();

        if (isManaged(path)) {
            DataMap map = DataMap.fromByteArray(messageEvent.getData());
            Bundle bundle = map.toBundle();

            onNewMessage(path, bundle);
        }
    }

    public abstract boolean isManaged(@NonNull String path);

    public abstract void onNewMessage(@NonNull String path, @NonNull Bundle bundle);

}