package com.example.a010_db_trivial;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class MyService extends Service {

    MediaPlayer musica;

    public MyService() {
    }

    @Override
    public void onCreate() {
            super.onCreate();
        Log.d("miService", "Creando el servicio");
        musica = MediaPlayer.create(this, R.raw.golpe);
        //musica = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        musica.setLooping(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("miService", "Iniciar el servicio");
        musica.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("miService", "Parar el servicio");
        musica.stop();
    }
}