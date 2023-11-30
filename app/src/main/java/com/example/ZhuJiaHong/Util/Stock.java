package com.example.ZhuJiaHong.Util;

public class Stock {
    private String stockNo;
    private String stockGui;
    private String stockSort;
    private String stockName;
    private String isBan;
    // 添加getter和setter方法

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockSort() {
        return stockSort;
    }

    public void setStockSort(String stockSort) {
        this.stockSort = stockSort;
    }

    public String getStockGui() {
        return stockGui;
    }

    public void setStockGui(String stockGui) {
        this.stockGui = stockGui;
    }

    public String getStockNo() {
        return stockNo;
    }

    public void setStockNo(String stockNo) {
        this.stockNo = stockNo;
    }

    public String getIsBan() {
        return isBan;
    }

    public void setIsBan(String isBan) {
        this.isBan = isBan;
    }
}
