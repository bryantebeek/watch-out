package nl.droidcon.WatchOut;

import android.app.Application;

public class WatchOutApplication extends Application{

    public static final String CHANGE_THRESHOLD = "changeThreshold",
                                UPDATE_TIME = "updateTime",
                                SERVICE_RUNNING = "serviceRunning",
                                STOP_SERVICE = "stopService";

    private boolean alertServiceRunning;
    private double btcChangeThreshold;
    private int updateFrequency;
    private boolean stopService;

    public WatchOutApplication(){
        alertServiceRunning = false;
        btcChangeThreshold = 0;
        updateFrequency = 0;
        stopService = false;
    }

    public void setAlertServiceRunning(boolean alertServiceRunning){
        this.alertServiceRunning = alertServiceRunning;
    }

    public boolean isAlertServiceRunning(){
        return alertServiceRunning;
    }

    public void setBtcChangeThreshold(double btcChangeThreshold){
         this.btcChangeThreshold = btcChangeThreshold;
    }

    public double getBtcChangeThreshold(){
        return btcChangeThreshold;
    }

    public void setUpdateTime(int updateFrequency){
        this.updateFrequency = updateFrequency;
    }

    public int getUpdateTime(){
        return updateFrequency;
    }

    public void setStopService(boolean stopService){
        this.stopService = stopService;
    }

    public boolean isStopService(){
        return stopService;
    }
}
