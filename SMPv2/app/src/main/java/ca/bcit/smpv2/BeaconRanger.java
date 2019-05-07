package ca.bcit.smpv2;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class BeaconRanger implements BeaconConsumer {

    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    private final Context context;
    private Visit visit;
    public long q = 0;
    public ArrayList<Boolean> connected = new ArrayList<>();
    public ArrayList<Long> time = new ArrayList<>();
    private boolean inStore = false;
    private Business currentBusinessConnection;
    private int businessIDHolder;

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
                String s = "";
                for(int i = 0; i < connected.size(); ++i)
                    s += connected.get(i) + "~";
                Calendar cal = Calendar.getInstance();
                boolean foundB = false;
                for(Beacon beacon : beacons) {
                    for (Business bus : MapsActivity.businessesNearby)
                        if ((currentBusinessConnection == null || beacon.getIdentifier(0).toString().compareToIgnoreCase(currentBusinessConnection.getRegion()) == 0                      )
                            && beacon.getIdentifier(0).toString().compareToIgnoreCase(bus.getRegion()) == 0) {
                            foundB = true;
                            currentBusinessConnection = bus;
                        }

                }

                if(foundB){
                    connected.add(true);
                    time.add(System.currentTimeMillis());
                }
                else {
                    connected.add(false);
                    time.add(System.currentTimeMillis());
                }

                while(System.currentTimeMillis() - time.get(0) > 5000) {
                    connected.remove(0);
                    time.remove(0);
                }
                boolean wasInStore = inStore;
                inStore = false;
                for(int i = 0; i < connected.size(); ++i) {
                    if (connected.get(i)) {
                        inStore = true;
                        q = connected.size();
                        break;
                    }
                }
                if(!wasInStore && inStore) {
                    Toast.makeText(context, "Entered store " + currentBusinessConnection.getBusinessName(), Toast.LENGTH_LONG).show();
                    visit = new Visit(LoginActivity.user.getUserID(), currentBusinessConnection.getBusinessID(), Calendar.getInstance());
                }
                else if(wasInStore && !inStore) {
                    visit.setDuration((int)((Calendar.getInstance().getTimeInMillis() - visit.getDate().getTimeInMillis()) / 1000 / 60));
                    businessIDHolder = currentBusinessConnection.getBusinessID();
                    // Minimum visit duration is 2 min
                    if(visit.getDuration() >= 2) {
                        Toast.makeText(context, "Exited store" + currentBusinessConnection.getBusinessName(), Toast.LENGTH_LONG).show();
                        new DatabaseObj(context).setVisit(visit, (ArrayList<Object> objects) -> {
                            new DatabaseObj(context).getPoints("userID=" + LoginActivity.user.getUserID()
                                            + " AND businessID=" + businessIDHolder,
                                    (ArrayList<Object> pointsObj) -> {
                                        NotificationHandler.showNotification("Store Visit",
                                                "You have " + ((Points) pointsObj.get(0)).getPoints() + " points, see available promotions!",
                                                PendingIntent.getActivity(context, 0, new Intent(context, MapsActivity.class), 0),
                                                context, 0);
                                    });
                        });
                    }
                    currentBusinessConnection = null;
                }
            }
        });

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
                //Toast.makeText(context, "You have connect to [Store]", Toast.LENGTH_LONG).show();
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                //Toast.makeText(context, "You have left [Store]", Toast.LENGTH_LONG).show();
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("F901FB7F-D7D2-4812-8A2B-6D0C7DF23C6E", null, null, null));
            beaconManager.startRangingBeaconsInRegion(new Region("", null, null, null));
            beaconManager.startMonitoringBeaconsInRegion(new Region("F901FB7F-D7D2-4812-8A2B-6D0C7DF23C6E", null, null, null));
            //beaconManager.startMonitoringBeaconsInRegion(new Region("f7826da6-4fa2-4e98-8024-bc5b71e0893e", null, null, null));
        } catch (RemoteException e) {    }
    }
}
