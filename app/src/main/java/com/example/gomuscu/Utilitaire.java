package com.example.gomuscu;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class Utilitaire {

    private static final String CHANNEL_ID = "MyChannelID";

    public static void performVibration(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Vérifiez si le dispositif prend en charge la vibration
        if (vibrator.hasVibrator()) {
            // Vibre pendant 100 millisecondes (ajustez selon vos besoins)
            vibrator.vibrate(100);
        } else {
            // Si le dispositif ne prend pas en charge la vibration, affichez un message ou prenez une autre mesure.
            Toast.makeText(context, "Votre appareil ne prend pas en charge la vibration", Toast.LENGTH_SHORT).show();
        }
    }

    private static void createNotificationChannel(Context context) {
        // Vérifier si le canal de notification doit être créé (à partir d'Android Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Nom du canal";
            String description = "Description du canal";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Obtenez le gestionnaire de notifications
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Créer le canal
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(Context context, String title, String content) {
        createNotificationChannel(context);

        // Créer le gestionnaire de notifications
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Construire la notification avec le constructeur NotificationCompat
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logoapplication)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Afficher la notification
        notificationManager.notify(1, builder.build());
    }
}
