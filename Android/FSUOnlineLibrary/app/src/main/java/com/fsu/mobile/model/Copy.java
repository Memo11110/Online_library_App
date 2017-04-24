package com.fsu.mobile.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class Copy implements Serializable{

    private int copyId;
    private String accessionNumber;

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

}
