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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.regex.Pattern;

public class DatabaseObj extends AsyncTask {

    private Context context;
    private String params = "";
    private String function;
    private Consumer<ArrayList<Object>> onCompleteFunction;
    private Function<String[], Object> objConstructor;
    private boolean get;

    public DatabaseObj(Context context) {
        this.context = context;
    }

    private void setMembers(String whereClause, Consumer<ArrayList<Object>> f){
        if(whereClause != "") {
            this.params = whereClause;
            this.params = this.params.replace(" ", "%20");
            this.params = this.params.replace("(", "%28");
            this.params = this.params.replace(")", "%29");
        }
        onCompleteFunction = f;
    }

    static private Object dbReturnID(String[] result){
        return Integer.parseInt(result[0]);
    }

    public void getApplicablePromotions(int userID, Consumer<ArrayList<Object>> f){
        setMembers("userid=" + userID, f);
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
        function = "nearbyBusinesses";
        get = true;
        this.execute();
    }

    public void getPreferredBusinesses(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = Business::new;
        function = "getPreferredBusinesses";
        get = true;
        this.execute();
    }

    public void getPromotions(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = Promotions::new;
        function = "getPromo";
        get = true;
        this.execute();
    }

    public void getTiers(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = PointTiers::new;
        function = "getTiers";
        get = true;
        this.execute();
    }

    public void getPoints(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = Points::new;
        function = "getPoints";
        get = true;
        this.execute();
    }

    public void getSettings(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = Setting::new;
        function = "getSettings";
        get = true;
        this.execute();
    }

    public void getBusinessSettings(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = BusinessSetting::new;
        function = "getBusinessSettings";
        get = true;
        this.execute();
    }

    public void getUserSettings(String whereClause, Consumer<ArrayList<Object>> f){
        setMembers(whereClause, f);
        objConstructor = UserSetting::new;
        function = "getUserSettings";
        get = true;
        this.execute();
    }

    public void calcAverageDuration(int businessID, Consumer<ArrayList<Object>> f)
    {
        get = true;
        function = "calcAverageDuration";
        params = "";
        params += "businessID=" + businessID;
        objConstructor = DatabaseObj::dbReturnID;
        setMembers(params, f);
        this.execute();
    }

    public void calcAverageVisits(int businessID, Consumer<ArrayList<Object>> f)
    {
        get = true;
        function = "calcAverageVisits";
        params = "";
        params += "businessID=" + businessID;
        objConstructor = DatabaseObj::dbReturnID;
        setMembers(params, f);
        this.execute();
    }

    public void calcMonthlyVisits(int businessID, Consumer<ArrayList<Object>> f)
    {
        function = "calcMonthlyVisits";
        params = "";
        params += "businessID=" + businessID;
        objConstructor = DataPoint::new;
        setMembers(params, f);
        this.execute();
    }

    public void calcNewOldUsers(int businessID, Consumer<ArrayList<Object>> f)
    {
        function = "calcNewOldUsers";
        params = "";
        params += "businessID=" + businessID;
        objConstructor = DataPoint::new;
        setMembers(params, f);
        this.execute();
    }

    public void calcVisitorsPerTier(int businessID, Consumer<ArrayList<Object>> f)
    {
        function = "calcVisitorsPerTier";
        params = "";
        params += "businessID=" + businessID;
        objConstructor = DataPoint::new;
        setMembers(params, f);
        this.execute();
    }

    public void setUser(User o){
        setUser(o, null);
    }

    public void setUser(User o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setUser";
        objConstructor = DatabaseObj::dbReturnID;
        params = "";
        params += "USER_ID=" + o.getUserID() + "&";
        params += "BUSINESS_ID=" + o.getBusinessID() + "&";
        params += "USERNAME=" + o.getUsername() + "&";
        params += "PASSWORD=" + o.getPassword() + "&";
        //params += "SETTING=" + o.getSetting();
        setMembers(params, f);
        this.execute();
    }

    public void updatePassword(int userID, String newPW){
        updatePassword(userID, newPW, null);
    }

    public void updatePassword(int userID, String newPW, Consumer<ArrayList<Object>> f){
        get = false;
        function = "updatePassword";
        params = "";
        params += "USER_ID=" + userID + "&";
        params += "NEW_PASSWORD=" + newPW;
        setMembers(params, f);
        this.execute();
    }

    public void setBusiness(Business o){
        setBusiness(o, null);
    }

    public void setBusiness(Business o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setBusiness";
        objConstructor = DatabaseObj::dbReturnID;
        params = "";
        params += "BUSINESS_ID=" + o.getBusinessID() + "&";
        params += "BUSINESS_NAME=" + o.getBusinessName() + "&";
        params += "LATITUDE=" + o.getLatitude() + "&";
        params += "LONGITUDE=" + o.getLongitude() + "&";
        setMembers(params, f);
        this.execute();
    }

    public void setPreferredBusiness(PreferredBusiness o)
    {
        setPreferredBusiness(o, null);
    }

    public void setPreferredBusiness(PreferredBusiness o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setPreferredBusiness";
        objConstructor = DatabaseObj::dbReturnID;
        params = "";
        params += "USER_ID=" + o.getUserID() + "&";
        params += "BUSINESS_ID=" + o.getBusinessID();
        setMembers(params, f);
        this.execute();
    }

    public void deletePreferredBusiness(PreferredBusiness o)
    {
        deletePreferredBusiness(o, null);
    }

    public void deletePreferredBusiness(PreferredBusiness o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "deletePreferredBusiness";
        params = "";
        params += "USER_ID=" + o.getUserID() + "&";
        params += "BUSINESS_ID=" + o.getBusinessID();
        setMembers(params, f);
        this.execute();
    }

    public void setPromotion(Promotions o){
        setPromotion(o, null);
    }

    public void setPromotion(Promotions o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setPromotion";
        objConstructor = DatabaseObj::dbReturnID;
        params = "";
        params += "PROMOTION_ID=" + o.getPromotionID() + "&";
        params += "BUSINESS_ID=" + o.getBusinessID() + "&";
        params += "BUSINESS_NAME=" + o.getBusinessName() + "&";
        params += "DETAILS=" + o.getDetails() + "&";
        params += "SHORT_DESCRIPTION=" + o.getShortDescription() + "&";
        params += "CLICKS=" + o.getClicks() + "&";
        params += "MIN_TIER=" + o.getMinTier().getTierID();
        setMembers(params, f);
        this.execute();
    }

    public void setVisit(Visit o){
        setVisit(o, null);
    }

    public void setVisit(Visit o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setVisit";
        objConstructor = DatabaseObj::dbReturnID;
        params = "";
        params += "VISIT_ID=" + o.getVisitID() + "&";
        params += "BUSINESS_ID=" + o.getBusinessID() + "&";
        params += "USER_ID=" + o.getUserID() + "&";
        params += "DATE=" + (new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss")).format(o.getDate().getTime()) + "&";
        params += "DURATION=" + o.getDuration();
        setMembers(params, f);
        this.execute();
    }

    public void setPoints(Points o){
        setPoints(o, null);
    }

    public void setPoints(Points o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setUserBusinessTier";
        objConstructor = DatabaseObj::dbReturnID;
        params = "";
        params += "USER_ID=" + o.getUserID() + "&";
        params += "BUSINESS_ID=" + o.getBusinessID() + "&";
        params += "POINTS=" + o.getPoints() + "&";
        setMembers(params, f);
        this.execute();
    }

    public void setBusinessSetting(BusinessSetting o){
        setBusinessSetting(o, null);
    }

    public void setBusinessSetting(BusinessSetting o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setBusinessSetting";
        objConstructor = DatabaseObj::dbReturnID;
        params = "";
        params += "BUSINESS_ID=" + o.getBusinessID() + "&";
        params += "SETTING_ID=" + o.getSetting().getSettingID() + "&";
        params += "VALUE=" + o.getValue() + "&";
        setMembers(params, f);
        this.execute();
    }

    public void setUserSetting(UserSetting o){
        setUserSetting(o, null);
    }

    public void setUserSetting(UserSetting o, Consumer<ArrayList<Object>> f){
        get = false;
        function = "setUserSetting";
        objConstructor = DatabaseObj::dbReturnID;
        params = "";
        params += "USER_ID=" + o.getUserID() + "&";
        params += "SETTING_ID=" + o.getSetting().getSettingID() + "&";
        params += "VALUE=" + o.getValue() + "&";
        setMembers(params, f);
        this.execute();
    }

    public void deletePromotion(int id){
        deletePromotion(id, null);
    }

    public void deletePromotion(int id, Consumer<ArrayList<Object>> f) {
        get = false;
        function = "deletePromotion";
        params = "PROMOTION_ID=" + id;
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
                        + ((!params.isEmpty()) ? "&whereClause=" + params : "");
            } else{
                link = "http://ec2-99-79-49-31.ca-central-1.compute.amazonaws.com/scripts.php?function=" + function
                        + ((!params.isEmpty()) ? "&" + params : "");
            }
            Log.i("LINK::" , link);
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
                Log.i("DBLINE", line);
            }
            Log.i("DBLINE", String.valueOf(line == null));

            in.close();
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Object objs) {
        String strObjects = (String) objs;
        ArrayList<Object> res = new ArrayList<Object>();
        if (!strObjects.trim().isEmpty()) {
            //Toast.makeText(context, "Database queried successfully", Toast.LENGTH_SHORT).show();
            Log.i("DatabaseObj", "onPostExecute:~" + strObjects);
                if (objConstructor != null) {
                    String strArrObjects[] = strObjects.split(Pattern.quote("~n"));
                    for(String strObject : strArrObjects) {
                        Log.i("SOBJ", strObject);
                        strObject = strObject.replace("NULL", "`NULL");
                        strObject = strObject.replace("~s~s", "~sNULL~s");
                        if(strObject.endsWith("~s"))
                            strObject += "NULL";
                        String[] strObjectArr = strObject.split(Pattern.quote("~s"));
                        for(int i = 0; i < strObjectArr.length; ++i)
                            if(strObjectArr[i].equals("NULL"))
                                strObjectArr[i] = null;
                            else
                                strObjectArr[i] = strObjectArr[i].replace("`NULL", "NULL");
                        res.add(objConstructor.apply(strObjectArr));
                    }
                }
        } else {
            //Toast.makeText(context, "Database query failed", Toast.LENGTH_SHORT).show();
        }
        if(onCompleteFunction != null) {
            onCompleteFunction.accept(res);
        }
    }
}
