package cn.edu.scut.priloc.mapper;

import lombok.Data;

import java.io.Serializable;


@Data
public class Entry<V> implements Serializable {
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
}
