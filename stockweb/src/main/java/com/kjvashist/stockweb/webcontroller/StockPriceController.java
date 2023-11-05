package com.kjvashist.stockweb.webcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;


import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import com.kjvashist.stockweb.model.StockPrice;
import com.kjvashist.stockweb.service.StockPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin("http://localhost:3000/")
public class StockPriceController {
    private final StockPriceService stockPriceService;

    public StockPriceController(StockPriceService  stockPriceService){
        this.stockPriceService=stockPriceService;
    }
    //@GetMapping("/")
    //public String index() {
    //	return "This is your StockPriceService";
    //}
    @GetMapping("/root")
    public String root() {
        return "Hello from StockWeb Root!";
    }

    @GetMapping("/tickers")
    public  Iterable<String>  getTickers(){
        return stockPriceService.getTickers();
    }

    @GetMapping("/tickerprices/{ticker}")
    public Iterable<StockPrice>  getTickerPrices(@PathVariable String ticker) {
        return stockPriceService.getPriceHistForTicker(ticker);
    }

    @GetMapping("/stockprice/{ticker}/{priceDate}")
    public StockPrice get(@PathVariable String ticker,@PathVariable String priceDate) {
        StockPrice stockPrice = stockPriceService.get(ticker,priceDate);
        if(stockPrice == null)  throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return stockPrice;
    }


    @DeleteMapping("/stockprice/{id}")
    public void delete(@PathVariable String ticker,@PathVariable String priceDate) {
        stockPriceService.remove(ticker,priceDate);
    }

    @PostMapping("/stockprice")
    public StockPrice create(@RequestPart("stock_price_data") String ticker, Date priceDate,
                             double open, double high, double low,
                             double close, double adjClose, double volume) throws IOException {
        return  stockPriceService.save(ticker, priceDate,open, high, low, close, adjClose, volume);
    }
}
