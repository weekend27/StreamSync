package dataClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2016/1/27.
 */
@SuppressWarnings("serial")
public class IndexData implements Serializable{
	public int lon;                                  //经度
    public int lat;                                  //纬度
    public List<Float> data;

    public IndexData() {
        data = new ArrayList<Float>();
    }
}
