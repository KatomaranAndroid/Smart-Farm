package com.github.bkhezry.demomapdrawingtools.utils;

/**
 * Created by LENOVO on 22-07-2017.
 */

public class FarmNew {
    String farmeid;
    String proid;
    String qty;
    String cropname;
    String aqty;
    String aprice;
    String bqty;
    String bprice;
    String cqty;
    String cprice;
    String acost;
    String bcost;
    String ccost;
    String shorttrees;
    String mixedtrees;
    String img;
    String date;


    public FarmNew(String farmeid,String proid, String qty, String cropname, String aqty, String aprice, String bqty, String bprice, String cqty, String cprice, String acost, String bcost, String ccost, String shorttrees, String mixedtrees, String img, String date) {
        this.farmeid = farmeid;
        this.proid=proid;
        this.qty=qty;
        this.cropname = cropname;
        this.aqty = aqty;
        this.aprice = aprice;
        this.bqty = bqty;
        this.bprice = bprice;
        this.cqty = cqty;
        this.cprice = cprice;
        this.acost = acost;
        this.bcost = bcost;
        this.ccost = ccost;
        this.shorttrees = shorttrees;
        this.mixedtrees = mixedtrees;
        this.img = img;
        this.date = date;
    }


    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getFarmeid() {
        return farmeid;
    }

    public void setFarmeid(String farmeid) {
        this.farmeid = farmeid;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getAqty() {
        return aqty;
    }

    public void setAqty(String aqty) {
        this.aqty = aqty;
    }

    public String getAprice() {
        return aprice;
    }

    public void setAprice(String aprice) {
        this.aprice = aprice;
    }

    public String getBqty() {
        return bqty;
    }

    public void setBqty(String bqty) {
        this.bqty = bqty;
    }

    public String getBprice() {
        return bprice;
    }

    public void setBprice(String bprice) {
        this.bprice = bprice;
    }

    public String getCqty() {
        return cqty;
    }

    public void setCqty(String cqty) {
        this.cqty = cqty;
    }

    public String getCprice() {
        return cprice;
    }

    public void setCprice(String cprice) {
        this.cprice = cprice;
    }

    public String getAcost() {
        return acost;
    }

    public void setAcost(String acost) {
        this.acost = acost;
    }

    public String getBcost() {
        return bcost;
    }

    public void setBcost(String bcost) {
        this.bcost = bcost;
    }

    public String getCcost() {
        return ccost;
    }

    public void setCcost(String ccost) {
        this.ccost = ccost;
    }

    public String getShorttrees() {
        return shorttrees;
    }

    public void setShorttrees(String shorttrees) {
        this.shorttrees = shorttrees;
    }

    public String getMixedtrees() {
        return mixedtrees;
    }

    public void setMixedtrees(String mixedtrees) {
        this.mixedtrees = mixedtrees;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }
}
