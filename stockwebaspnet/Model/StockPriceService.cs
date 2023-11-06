using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Data.Sqlite;

namespace stockwebaspnet.Model
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


        public static List<StockPrice> GetTickerPrices(SqliteConnection sqliteConnection, string ticker, int monthsOfData)
        {
            // Create a connection to the SQLite database
            List<StockPrice> listTikcerPrices = new List<StockPrice>();
            try
            {
                //Todo:Check for nulls here
                string priceDateFilter = $" and PriceDate>= date('now','-{monthsOfData} month')";
                string sql = $"select Ticker, PriceDate , Open , High , Low , Close , AdjClose,Volume from StockPrices where Ticker='{ticker}' {priceDateFilter} order by PriceDate desc";
                using (SqliteCommand command = new SqliteCommand(sql, sqliteConnection))
                {

                    SqliteDataReader reader = command.ExecuteReader();
                    while (reader.Read())
                    {
                        StockPrice stockPrice = new StockPrice();
                        stockPrice.Ticker = reader["Ticker"].ToString();
                        stockPrice.PriceDate = DateOnly.FromDateTime(DateTime.Parse(reader["PriceDate"].ToString()));
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

    }
}