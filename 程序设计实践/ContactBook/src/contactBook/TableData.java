package contactBook;

import java.util.Vector;

public class TableData {
    int total;
    Vector<Vector<Object>> dataList;

    public TableData(int total, Vector<Vector<Object>> dataList) {
        this.total = total;
        this.dataList = dataList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Vector<Vector<Object>> getDataList() {
        return dataList;
    }

}
