package cn.edu.scut.priloc.mapper;

import java.io.Serializable;


public class Result<V> implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long key;
    private Entry<V> entry;
    private int index;
    private boolean isExit;

    public Result(Long key, Entry<V> entry, int index, boolean isExit) {
        this.key = key;
        this.entry = entry;
        this.index = index;
        this.isExit=isExit;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Entry<V> getEntry() {
        return entry;
    }

    public void setEntry(Entry<V> entry) {
        this.entry = entry;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    @Override
    public String toString() {
        return "Result{" +
                "key=" + key +
                ", entry=" + entry +
                ", index=" + index +
                ", isExit=" + isExit +
                '}';
    }
}
