package com.github.bkhezry.demomapdrawingtools.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.github.bkhezry.demomapdrawingtools.R;
import com.github.bkhezry.demomapdrawingtools.cal.ui.SampleActivity;
import com.github.bkhezry.demomapdrawingtools.cal.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import au.com.bytecode.opencsv.CSVReader;

public class TimeService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
    public static NotificationManager mManager;
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private ArrayList<String> timeList = new ArrayList<String>();
    private ArrayList<String> activityList = new ArrayList<String>();
    private ArrayList<String> incomeList = new ArrayList<String>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed

        List<String[]> list = new ArrayList<String[]>();
        String next[] = {};
        try {
            File file = new File("/storage/emulated/0/farmer.csv");
            try {
                if (!file.exists() && file.createNewFile()) {
                    InputStream inputStream = getApplicationContext().getAssets().open("farmer.csv");
                    FileUtils.copy(inputStream, file);
                }
            } catch (IOException e) {
                e.printStackTrace();
                file = null;
            }
            InputStream targetStream = new FileInputStream(file);
            InputStreamReader csvStreamReader = new InputStreamReader(targetStream);
            CSVReader reader = new CSVReader(csvStreamReader);
            for (; ; ) {
                next = reader.readNext();
                if (next != null) {
                    list.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        timeList = new ArrayList<String>();
        activityList = new ArrayList<String>();
        incomeList = new ArrayList<String>();
        for (int i = 1; i < list.size(); i++) {
            timeList.add(list.get(i)[0]);
            activityList.add(list.get(i)[2]);
            incomeList.add(list.get(i)[3]);
        }
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (timeList != null) {
                        if (timeList.contains(getDateTime())) {
                            int index = timeList.indexOf(getDateTime());
                            NotificationCompat.Builder builder =
                                    new NotificationCompat.Builder(getApplicationContext())
                                            .setSmallIcon(R.drawable.ic_launcher)
                                            .setContentTitle("Activity " + activityList.get(index))
                                            .setContentText("Income " + incomeList.get(index));
                            Intent notificationIntent = new Intent(getApplicationContext(), SampleActivity.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                            builder.setContentIntent(contentIntent);
                            NotificationManager manager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                            manager.notify(0, builder.build());
                            timeList.remove(index);
                            activityList.remove(index);
                            incomeList.remove(index);
                        }
                    }
                }

            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(new Date());
        }

        private boolean checktimings(String time, String endtime) {

            String pattern = "HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            try {
                Date date1 = sdf.parse(time);
                Date date2 = sdf.parse(endtime);
                if (date1.equals(date2)) {
                    return true;
                } else {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}