package com.kjvashist.stockweb.service;

import com.kjvashist.stockweb.model.StockPrice;
import com.kjvashist.stockweb.repository.StockPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class StockPriceService {

    private final StockPriceRepository stockPriceRepository;

    public StockPriceService() {
        this.stockPriceRepository = new StockPriceRepository();
    }

    public Iterable<StockPrice> getPriceHistForTicker(String ticker) {
        return stockPriceRepository.getPriceHistForTicker(ticker);
    }

    public  Iterable<String>  getTickers(){
        return stockPriceRepository.getTickers();
    }
    public StockPrice get(String ticker, String priceDate) {
        //return stockPriceRepository.findById(ticker,priceDate).orElse(null);
        return null;
    }

    public void remove(String ticker, String priceDate) {
        //stockPriceRepository.deleteById(ticker, priceDate);
    }

    public StockPrice save(String ticker, Date priceDate,
                           double open, double high, double low,
                           double close, double adjClose, double volume)
        {
            StockPrice stockPrice = new StockPrice();
            stockPrice.setTicker(ticker);
            stockPrice.setPriceDate(priceDate);
            stockPrice.setOpen(open);
            stockPrice.setHigh(high);
            stockPrice.setLow(low);
            stockPrice.setClose(close);
            stockPrice.setAdjClose(adjClose);
            stockPrice.setVolume(volume);
            //stockPriceRepository.save(stockPrice);
            return stockPrice;
    }
}
