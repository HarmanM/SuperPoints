package ca.bcit.smpv2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.Consumer;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Function;

public class DatabaseObj extends AsyncTask {

    private Context context;
    private String params;
    private String function;
    private Consumer<ArrayList<Object>> onCompleteFunction;
    private Function<String, Object> objConstructor;
    private boolean get;

    public DatabaseObj(Context context) {
        this.context = context;
    }

    private void setMembers(String whereClause, Consumer<ArrayList<Object>> f){
        if(whereClause != "")
            this.params = whereClause;
        onCompleteFunction = f;
    }

    public void getApplicablePromotions(int userID, Consumer<ArrayList<Object>> f){
        setMembers("uid=" + userID, f);
        objConstructor = Promotions::new;
        function = "getApplicablePromos";
        get = true;
        this.execute();
    }

    public void getUsers(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = User::new;
        function = "getUser";
        get = true;
        this.execute();
    }

    public void getVisits(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = Visit::new;
        function = "getVisit";
        get = true;
        this.execute();
    }

    public void getBusinesses(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = Business::new;
        function = "getBusiness";
        get = true;
        this.execute();
    }

    public void getBusinessesNearby(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = Business::new;
        function = "getBusinesses";
        get = true;
        this.execute();
    }

    public void getPromotions(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = Promotions::new;
        function = "getPromotion";
        get = true;
        this.execute();
    }

    public void getTiers(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = Tiers::new;
        function = "getTier";
        get = true;
        this.execute();
    }

    public void getPoints(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = Points::new;
        function = "getUserBusinessTier";
        get = true;
        this.execute();
    }

    public void setUser(User o){
        setUser(o, null);
    }

    public void setUser(User o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setUser";
        params = "";
        params += "USER_ID=" + o.getUserID() + "&";
        params += "BUSINESS_ID=" + o.getBusinessID() + "&";
        params += "USERNAME=" + o.getUsername() + "&";
        params += "PASSWORD=" + o.getPassword() + "&";
        params += "SETTING=" + o.getSetting();
        setMembers(params, f);
        this.execute();
    }

    public void setBusiness(Business o){
        setBusiness(o, null);
    }

    public void setBusiness(Business o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setBusiness";
        params = "";
        params += "BUSINESS_ID=" + o.getBusinessID() + "&";
        params += "BUSINESS_NAME=" + o.getBusinessName() + "&";
        params += "LATITUDE=" + o.getLatitude() + "&";
        params += "LONGITUDE=" + o.getLongitude() + "&";
        setMembers(params, f);
        this.execute();
    }

    public void setPromotion(Promotions o){
        setPromotion(o, null);
    }

    public void setPromotion(Promotions o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setPromotion";
        params = "";
        params += "PROMOTION_ID=" + o.getPromotionID() + "&";
        params += "BUSINESS_ID=" + o.getBusinessID() + "&";
        params += "BUSINESS_NAME=" + o.getBusinessName() + "&";
        params += "DETAILS=" + o.getDetails() + "&";
        params += "CLICKS=" + o.getClicks() + "&";
        params += "TIER_ID=" + o.getTierID() + "&";
        setMembers(params, f);
        this.execute();
    }

    public void setVisit(Visit o){
        setVisit(o, null);
    }

    public void setVisit(Visit o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setVisit";
        params = "";
        params += "VISIT_ID=" + o.getVisitID() + "&";
        params += "BUSINESS_ID=" + o.getBusinessID() + "&";
        params += "USER_ID=" + o.getUserID() + "&";
        params += "DATE=" + o.getDate() + "&";
        params += "DURATION=" + o.getDuration() + "&";
        setMembers(params, f);
        this.execute();
    }

    public void setTier(Tiers o){
        setTier(o, null);
    }

    public void setTier(Tiers o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setTier";
        params = "";
        params += "TIER_ID=" + o.getTierID() + "&";
        params += "MIN_POINTS=" + o.getMinPoints() + "&";
        setMembers(params, f);
        this.execute();
    }
    public void setPoints(Points o){
        setPoints(o, null);
    }

    public void setPoints(Points o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setUserBusinessTier";
        params = "";
        params += "USER_ID=" + o.getUserID() + "&";
        params += "BUSINESS_ID=" + o.getBusinessID() + "&";
        params += "POINTS=" + o.getPoints() + "&";
        setMembers(params, f);
        this.execute();
    }

    @Override
    protected String doInBackground(Object[] arg0) {
        try {
            String link;
            // if its a get from database
            if(get) {
                 link = "http://ec2-99-79-49-31.ca-central-1.compute.amazonaws.com/scripts.php?function=" + function
                        + "&whereClause=" + params;
            } else{
                link = "http://ec2-99-79-49-31.ca-central-1.compute.amazonaws.com/scripts.php?function=" + function
                        + "&" + params;
            }
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }

            in.close();
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Object obj) {
        String strObject = (String) obj;
        if (!strObject.isEmpty()) {
            Toast.makeText(context, "Database queried successfully", Toast.LENGTH_SHORT).show();
            Log.i("DatabaseObj", "onPostExecute:~" + strObject);
            if(onCompleteFunction != null) {
                ArrayList<Object> res = new ArrayList<Object>();
                if (objConstructor != null) {
                    String resArr[] = strObject.split("\n");
                    for(String sObj : resArr) {
                        Log.i("SOBJ", sObj);
                        res.add(objConstructor.apply(sObj));
                    }
                }
                onCompleteFunction.accept(res);
            }
        } else {
            Toast.makeText(context, "Database query failed", Toast.LENGTH_SHORT).show();
        }
    }
}
