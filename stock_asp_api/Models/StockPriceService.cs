using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Data.Sqlite;

namespace stock_asp_api.Model
{
    public class StockPriceService
    {
        public static List<string> GetTickers(SqliteConnection sqliteConnection)
        {
            List<string> listTikcers = new List<string>();
            try
            {
                string sql = $"select distinct Ticker from StockPrices order by Ticker";
                using (SqliteCommand command = new SqliteCommand(sql, sqliteConnection))
                {

                    SqliteDataReader reader = command.ExecuteReader();
                    while (reader.Read())
                    {
                        //Todo:Check for nulls here
                        listTikcers.Add(reader["Ticker"].ToString());
                    }

                }
            }
            catch (Exception exp)
            {
                //Todo:Check for nulls here
            }
            return listTikcers;
        }

        public static StockPrice? GetTickerPricesForDate(SqliteConnection sqliteConnection, string ticker, string priceDate)
        {
            List<StockPrice> listStockPrice = GetTickerPrices(sqliteConnection, ticker, priceDate, 0);
            if (listStockPrice.Count > 0)
            {
                return listStockPrice[0];
            }
            return null;
        }

        public static List<StockPrice> GetTickerPrices(SqliteConnection sqliteConnection, string ticker, string priceDate="", int monthsOfData=3)
        {
            // Create a connection to the SQLite database
            List<StockPrice> listTikcerPrices = new List<StockPrice>();
            try
            {
                //Todo:Check for nulls here
                string priceDateFilter = "";
                if (priceDate.Length==0 && monthsOfData>0)
                {
                    priceDateFilter = $" and PriceDate>= date('now','-{monthsOfData} month')";
                }
                else if (priceDate.Length>0)
                {
                    DateTime  dtPrcDate  = DateTime.Parse(priceDate);
                    priceDate = dtPrcDate.ToString("yyyy-MM-dd");
                    priceDateFilter += $" and PriceDate='{priceDate}'";
                }
                string sql = $"select Ticker, PriceDate , ROUND(Open,2) as Open, ROUND(High,2) as High, ROUND(Low,2) as Low, ROUND(Close,2) as Close, ROUND(AdjClose,2) as AdjClose, Volume from StockPrices where Ticker='{ticker}' {priceDateFilter} order by PriceDate desc";
                using (SqliteCommand command = new SqliteCommand(sql, sqliteConnection))
                {

                    SqliteDataReader reader = command.ExecuteReader();
                    while (reader.Read())
                    {
                        StockPrice stockPrice = new StockPrice();
                        stockPrice.Ticker = reader["Ticker"].ToString();
                        stockPrice.PriceDate = DateTime.Parse(reader["PriceDate"].ToString()).ToString("yyyy-MM-dd");
                        stockPrice.Open = double.Parse(reader["Open"].ToString());
                        stockPrice.High = double.Parse(reader["High"].ToString());
                        stockPrice.Low = double.Parse(reader["Low"].ToString());
                        stockPrice.Close = double.Parse(reader["Close"].ToString());
                        stockPrice.AdjClose = double.Parse(reader["AdjClose"].ToString());
                        stockPrice.Volume = double.Parse(reader["Volume"].ToString());
                        listTikcerPrices.Add(stockPrice);
                    }

                }
            }
            catch (Exception exp)
            {


            }
            return listTikcerPrices;
        }

        public static int UpdateTickerPrices(SqliteConnection sqliteConnection, StockPrice stockPrice)
        {
            int retVal = 0;
            try
            {
                DateTime pricedate;
                if (DateTime.TryParse(stockPrice.PriceDate,out pricedate)==false)
                {
                    pricedate = DateTime.Now.AddYears(-100);
                }
                 
                //Todo:Check for nulls here
                string sql = $"Update StockPrices set " +
                            $" Open = {stockPrice.Open}, " +
                            $" High = {stockPrice.High}, " +
                            $" Low = {stockPrice.Low}, " +
                            $" Close = {stockPrice.Close}, " +
                            $" AdjClose = {stockPrice.AdjClose}, " +
                            $" Volume = {stockPrice.Volume} " + 
                            $" where Ticker = '{stockPrice.Ticker}' " + 
                            $" and PriceDate  = '{pricedate.ToString("yyyy-MM-dd")}' ";
                using (SqliteCommand command = new SqliteCommand(sql, sqliteConnection))
                {
                    retVal = command.ExecuteNonQuery();
                }
            }
            catch (Exception exp)
            {


            }
            return retVal;
        }

        public static int DeleteStockPrices(SqliteConnection sqliteConnection, string ticker, string priceDate)
        {
            int retVal = 0;
            try
            {
                //Todo:Check for nulls here
                string sql = $"delete from StockPrices " +
                             $" where Ticker = '{ticker}' " +
                             $" and PriceDate  = '{priceDate}' ";
                using (SqliteCommand command = new SqliteCommand(sql, sqliteConnection))
                {
                    retVal = command.ExecuteNonQuery();
                }
            }
            catch (Exception exp)
            {


            }
            return retVal;
        }
    }

}