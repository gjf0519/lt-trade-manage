package similar;

import java.util.Arrays;

/**
 * @author gaijf
 * @description
 * @date 2020/4/7
 */
public class DataNode {
    double[] datas;

    public double[] getDatas() {
        return datas;
    }

    public void setDatas(double[] datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "DataNode [datas=" + Arrays.toString(datas) + "]";
    }

    public DataNode() {

    }
    public DataNode(double[] datas) {
        super();
        this.datas = datas;
    }
}
