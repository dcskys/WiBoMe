package com.dc.wibome.entity.response;

/**
 * 微博的可见性及指定可见分组信息。
 * 该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；
 * list_id为分组的组号
 */
public class Visible extends BaseEntity {
    private int type;
    private int list_id;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

}
