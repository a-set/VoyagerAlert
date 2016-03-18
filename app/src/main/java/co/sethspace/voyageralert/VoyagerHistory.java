package co.sethspace.voyageralert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anirudh on 3/17/2016.
 */
public class VoyagerHistory {

    private int vLevel;
    private String vText;
    private int vType;

    public VoyagerHistory(int level,String text, int type){
        this.vLevel = level;
        this.vText = text;
        this.vType = type;
    }

    public int getvLevel() {
        return vLevel;
    }

    public String getvText() {
        return vText;
    }

    public int getvType() {
        return vType;
    }

    public static List<VoyagerHistory> createDummyList(int numOfDummies){
        List<VoyagerHistory> dummyList = new ArrayList<VoyagerHistory>();
        for(int i=0;i<numOfDummies;i++)
            dummyList.add(new VoyagerHistory(i,"Sample",i));
        return dummyList;
    }
}
