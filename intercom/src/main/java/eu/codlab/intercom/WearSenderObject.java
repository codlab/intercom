package eu.codlab.intercom;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevinleperf on 15/02/16.
 */
public class WearSenderObject {
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private GoogleApiClient mGoogleApiClient;
    private Context mParent;

    public WearSenderObject(Context parent) {
        mParent = parent;
        mHandlerThread = new HandlerThread("WEAR_SENDER");

        mHandlerThread.start();

        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public void stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                mHandlerThread.quitSafely();
            } catch (Exception e) {

            }
        } else {
            mHandlerThread.stop();
        }

        mHandler = null;
    }

    public void sendMessage(String route, Bundle map) {
        if (mHandler != null) {
            mHandler.post(createRunnable(route, map));
        }
    }

    private Runnable createRunnable(final String route, Bundle bundle) {
        final DataMap map = DataMap.fromBundle(bundle);

        return new Runnable() {
            @Override
            public void run() {
                if (mGoogleApiClient == null && mParent != null) {
                    mGoogleApiClient = new GoogleApiClient.Builder(mParent)
                            .addApi(Wearable.API)
                            .build();
                }

                if (mGoogleApiClient != null) {
                    mGoogleApiClient.blockingConnect();

                    List<String> nodes = getNodes();
                    for (String node : nodes) {
                        Wearable.MessageApi
                                .sendMessage(mGoogleApiClient,
                                        node,
                                        route,
                                        map.toByteArray());
                    }
                }
            }
        };
    }

    private List<String> getNodes() {
        ArrayList<String> results = new ArrayList<>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }
        return results;
    }
}
