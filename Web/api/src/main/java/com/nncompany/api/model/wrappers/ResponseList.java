package com.nncompany.api.model.wrappers;

import java.util.List;

public class ResponseList<T> {

    private List<T> list;
    private Integer total;

    public ResponseList(){}

    public ResponseList(List list, Integer total){
        this.list = list;
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
