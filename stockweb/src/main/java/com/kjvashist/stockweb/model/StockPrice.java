package com.kjvashist.stockweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class StockPrice {

    @NotEmpty
    private String ticker;
    //raw Data

    @NotEmpty
    private Date priceDate;
    private double open;
    private double high;
    private double low;

    private double close;
    private double adjClose;
    private double volume;


    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Date getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(double adjClose) {
        this.adjClose = adjClose;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }


    public StockPrice() {
    }

    public static Date parseDate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the date string and get a LocalDate object.
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        // Convert the LocalDate object to a Date object.
        return java.sql.Date.valueOf(localDate);
    }

    public StockPrice(ResultSet rSet) throws SQLException {
        ticker = rSet.getString("Ticker");
        String dateString = rSet.getString("PriceDate");
        priceDate = parseDate(dateString);
        //rSet.getDate("PriceDate");
        open = rSet.getDouble("Open");
        high = rSet.getDouble("High");
        low = rSet.getDouble("Low");
        close = rSet.getDouble("Close");
        adjClose = rSet.getDouble("AdjClose");
        volume= rSet.getDouble("Volume");
    }



}
