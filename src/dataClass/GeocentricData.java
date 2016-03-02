package dataClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2016/1/27.
 */
@SuppressWarnings("serial")
public class GeocentricData implements Serializable{
    public List<Float> data;
    public GeocentricData() {
        data = new ArrayList<Float>();
    }
}
