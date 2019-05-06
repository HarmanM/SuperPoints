package ca.bcit.smpv2;

import java.util.ArrayList;

public class DataPoint {

    private ArrayList<String> data;

    public DataPoint(ArrayList<String> data)
    {
        this.data = data;
    }

    public DataPoint(String[] result){
        
        data = new ArrayList<>();
        for(String res : result)
            data.add(res);
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String r = "";
        for (String s : data)
            r += s + ",";
        return r.substring(0, r.length() - 1);
    }
}
