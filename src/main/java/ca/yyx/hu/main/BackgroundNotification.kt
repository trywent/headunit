package ca.yyx.hu.main

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import ca.yyx.hu.R
import android.app.PendingIntent
import android.view.KeyEvent
import ca.yyx.hu.App
import ca.yyx.hu.aap.AapProjectionActivity
import ca.yyx.hu.aap.protocol.nano.MediaPlayback
import ca.yyx.hu.utils.LocalIntent


/**
 * @author algavris
 * @date 17/07/2017
 */
class BackgroundNotification(private val context: Context) {

    companion object {
        private const val NOTIFICATION_MEDIA = 1
    }

    fun notify(metadata: MediaPlayback.MediaMetaData) {

        if (!App.provide(context).hasVideoFocus) {
            return
        }

        val playPauseKey = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
        val nextKey = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT)
        val prevKey = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS)

        val playPause = PendingIntent.getBroadcast(context, 1, LocalIntent.createKeyEvent(playPauseKey), PendingIntent.FLAG_UPDATE_CURRENT)
        val next = PendingIntent.getBroadcast(context, 1, LocalIntent.createKeyEvent(nextKey), PendingIntent.FLAG_UPDATE_CURRENT)
        val prev = PendingIntent.getBroadcast(context, 1, LocalIntent.createKeyEvent(prevKey), PendingIntent.FLAG_UPDATE_CURRENT)

        val image = BitmapFactory.decodeByteArray(metadata.albumart, 0, metadata.albumart.size)

        val notification = NotificationCompat.Builder(context)
                .setContentTitle(metadata.song)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentText(metadata.artist)
                .setSubText(String.format("Remaining: %02d:%02d", metadata.duration / 60, metadata.duration % 60))
                .setSmallIcon(R.drawable.ic_stat_aa)
                .setLargeIcon(image)
                .setContentIntent(PendingIntent.getActivity(context, 0, AapProjectionActivity.intent(context), PendingIntent.FLAG_UPDATE_CURRENT))
                .setStyle(NotificationCompat.BigPictureStyle())
                .addAction(R.drawable.ic_skip_previous_black_24dp, "Previous", prev)
                .addAction(R.drawable.ic_play_arrow_black_24dp, "Play/Pause", playPause)
                .addAction(R.drawable.ic_skip_next_black_24dp, "Next", next)
                .build()

        App.provide(context).notificationManager.notify(NOTIFICATION_MEDIA, notification)
    }

}