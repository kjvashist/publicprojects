package com.kjvashist.stockweb.repository;

import com.kjvashist.stockweb.model.StockPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StockPriceRepository {
    @Autowired
    private Environment env;

    private String dbPath1="";
    private String dbPath2="";

    int monthsOfData=3;


    public StockPriceRepository(){
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        dbPath1 = bundle.getString("SQLITE_DATABASE_PATH1");
        dbPath2 = bundle.getString("SQLITE_DATABASE_PATH2");
        monthsOfData =  Integer.parseInt(bundle.getString("MONTHS_DATA")) ;
    }
    public  Iterable<StockPrice>  getPriceHistForTicker(String ticker)
    {
        try(DbWrapper dbWrapper = new DbWrapper(new String[]{dbPath1,dbPath2} ))
        {
            //String sql = String.format("select Ticker, PriceDate , Open , High , Low , Close , AdjClose,Volume from StockPrices where Ticker='%s' and PriceDate>= date('now','-3 month')  order by PriceDate desc", ticker);
            String priceDateFilter = String.format("and PriceDate>= date('now','-%d month')",monthsOfData);

            String sql = String.format("select Ticker, PriceDate , Open , High , Low , Close , AdjClose,Volume from StockPrices where Ticker='%s' %s order by PriceDate desc", ticker, priceDateFilter) ;
            ResultSet rSet = dbWrapper.select(sql);
            List<StockPrice> listSockPrices  = new ArrayList<>();
            while(rSet.next())
            {
                StockPrice stockPrice = new StockPrice(rSet);
                listSockPrices.add(stockPrice);
            }
            //dbWrapper.close();
            return listSockPrices;
        } catch (SQLException e) {
               throw new RuntimeException(e);
        }
    }

    public  Iterable<String>  getTickers()
    {
        try(DbWrapper dbWrapper = new DbWrapper(new String[]{dbPath1,dbPath2} ))
        {
            String sql = "select distinct Ticker from StockPrices order by Ticker";
            ResultSet rSet = dbWrapper.select(sql);
            List<String> listTickers  = new ArrayList<>();
            while(rSet.next())
            {
                listTickers.add(rSet.getString("Ticker"));
            }
            //dbWrapper.close();
            return listTickers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
