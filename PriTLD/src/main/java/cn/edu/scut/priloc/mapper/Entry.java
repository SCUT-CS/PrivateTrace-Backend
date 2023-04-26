package cn.edu.scut.priloc.mapper;

import java.io.Serializable;


public class Entry<V> implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long key;
    private V value;

    public Entry(Long key, V value) {
        this.key = key;
        this.value = value;
    }

    public V getValue(){
        return this.value;
    }
    @Override
    public String toString() {
        return
                "key=" + key +
                ", value=" + value +"; ";
    }
    public Long compareTo(Long o) {
        return this.key - o;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }


}
