package cn.edu.scut.priloc.pojo;

import java.util.List;

public class Page<T> {
    private int total;

    private List<T> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "Page{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}
