package ca.yyx.hu.roadrover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;

import ca.yyx.hu.App;
import ca.yyx.hu.aap.AapTransport;
import ca.yyx.hu.aap.Messages;
import ca.yyx.hu.utils.AppLog;
import ca.yyx.hu.utils.Utils;

/**
 * @author algavris
 * @date 24/09/2016.
 */

public class DeviceListener extends BroadcastReceiver {
    private static final String ACTION_AUDIO = "com.roadrover.frontpane.audio";
    private static final String ACTION_KEYEVENT = "com.roadrover.frontpane.keyevent";
    private static final String ACTION_STARTMUSIC = "com.roadrover.startmusic";

    public static IntentFilter createIntentFilter()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_AUDIO);
        filter.addAction(ACTION_KEYEVENT);
        filter.addAction(ACTION_STARTMUSIC);
        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppLog.i(intent.toString());

        AapTransport transport = App.get(context).transport();

        if (ACTION_KEYEVENT.equals(intent.getAction())) {
            int keyCode = intent.getIntExtra("keyvalue", 0);
            handleKeyEvent(keyCode, transport);
        } else if (ACTION_STARTMUSIC.equals(intent.getAction()))
        {
            transport.sendButton(Messages.BTN_PLAYPAUSE, true);
        }
    }

    private void handleKeyEvent(int keyCode, AapTransport transport) {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_HEADSETHOOK:
                sendButton(Messages.BTN_PLAYPAUSE, transport);
                break;
            case KeyEvent.KEYCODE_MEDIA_STOP:
                sendButton(Messages.BTN_STOP, transport);
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                sendButton(Messages.BTN_NEXT, transport);
                break;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                sendButton(Messages.BTN_PREV, transport);
                break;
            default:
                AppLog.i("Unknown keyCode: "+keyCode);
        }
    }

    private void sendButton(int btnCode, AapTransport transport)
    {
        transport.sendButton(btnCode, true);
        Utils.ms_sleep(100);
        transport.sendButton(btnCode, false);
    }
}
