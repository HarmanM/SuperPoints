package ca.bcit.smpv2;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

public class BeaconRanger implements BeaconConsumer {

    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    private final Context context;

    public BeaconRanger(Context c) {
        context = c;
        beaconManager = BeaconManager.getInstanceForApplication(context);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(
                new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    public void unbindService(ServiceConnection var1){
        context.unbindService(var1);
    }

    @Override
    public boolean bindService(Intent var1, ServiceConnection var2, int var3){
        return context.bindService(var1, var2, var3);
    }

    @Override
    public Context getApplicationContext(){
        return context;
    }

    protected void onDestroy() {
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllMonitorNotifiers();
        beaconManager.removeAllRangeNotifiers();

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                for(Beacon beacon : beacons)
                    if(beacon.getIdentifier(0).toString().compareToIgnoreCase(region.getUniqueId()) == 0) {
                        //beacon.setRunningAverageRssi(beacon.getRssi());
                        Toast.makeText(context, beacon.getIdentifier(1) + "~" + beacon.getIdentifier(2)
                                + "\n" + sdf.format(cal.getTime())
                                + "\n" + beacon.getTxPower()
                                + "\n" + beacon.getRssi()
                                + "\n" + beacon.getDistance(), Toast.LENGTH_LONG).show();
                    }
                Log.i(TAG, "~We connectected~   ");
            }
        });

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
                Toast.makeText(context, "You have connect to [Store]", Toast.LENGTH_LONG).show();
                //((TextView) findViewById(R.id.test)).setText(region.getUniqueId());
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                Toast.makeText(context, "You have left [Store]", Toast.LENGTH_LONG).show();
                //((TextView) findViewById(R.id.test)).setText("Left");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("F901FB7F-D7D2-4812-8A2B-6D0C7DF23C6E", null, null, null));
            beaconManager.startMonitoringBeaconsInRegion(new Region("F901FB7F-D7D2-4812-8A2B-6D0C7DF23C6E", null, null, null));
            //beaconManager.startMonitoringBeaconsInRegion(new Region("f7826da6-4fa2-4e98-8024-bc5b71e0893e", null, null, null));
        } catch (RemoteException e) {    }
    }
}
