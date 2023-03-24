package cn.edu.scut.priloc.mapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class Result<V> implements Serializable {

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
}
