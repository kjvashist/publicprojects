package com.kjv.stockprice;

//import java.io.*;
//import java.net.URL;
//import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Calendar;
//import java.util.Random;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class StockPriceDownloader {

	final static String CONFIG_FILE_NAME = "config.properties";

	public static void main(String[] args) {
		
		LocalUtils.WriteMessageToLogFile("Initialization StockPriceDownloader");

		
		//String[] symbols = { "AAPL", "MSFT", "GOOGL", "AMZN", "TSLA" };
		//String[] symbols = { "AAPL", "MSFT"};
//		String[] symbols = { "AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "NVDA", "META", "BRK-A", "TSM", "JNJ", "UNH",
//				"GOOG", "V", "MA", "WMT", "COST", "HD", "MCD", "PG", "PEP", "KO", "INTC", "CRM", "NFLX", 
//				"CMCSA", "ABBV", "TXN", "ADBE", "MRK", "QCOM", "AMD", "TMO", "ADP", "PYPL", "LLY", "CVX", "XOM",
//				"MDLZ", "DIS", "IBM", "FISV.VI", "BKNG", "LMT", "NKE", "UNP", "DHR", "DOW", "JPM", "WBA", "MO",
//				"AVGO", "MMM" };
		// String[] symbols = {"BRK-A"};
		// String[] symbols =
		// {"BRK-A","TSM","JNJ","UNH","GOOG","V","MA","WMT","COST","HD","MCD","PG","PEP","KO","INTC","CRM","NFLX","NVDA","CMCSA","ABBV","TXN","ADBE","MRK","QCOM","AMD","TMO","ADP","PYPL","LLY","CVX","XOM","MDLZ","DIS","IBM","FISV.VI","BKNG","LMT","NKE","UNP","DHR","DOW","JPM","WBA","MO","AVGO","MMM"};
		// String[] symbols =
		// {"BRK-A","TSM","JNJ","UNH","GOOG","V","MA","WMT","COST","HD","MCD","PG","PEP","KO","INTC","CRM","NFLX","NVDA","CMCSA","ABBV","TXN","ADBE","MRK","QCOM","AMD","TMO","ADP","PYPL","LLY","CVX","XOM","MDLZ","DIS","IBM","FISV.VI","BKNG","LMT","NKE","UNP","DHR","DOW","JPM","WBA","MO","AVGO","MMM"};
		// String[] symbols = {"FISV.VI"};
		// String[] symbols =
		// {"FISV.VI","BKNG","LMT","NKE","UNP","DHR","DOW","JPM","WBA","MO","AVGO","MMM"};
		DataDownloader dataDownloader = new DataDownloader(); 
		List<String>  listFilesDownload = dataDownloader.DownloadStockPrices(CONFIG_FILE_NAME);
		
//		List<String>  listFilesDownload  = new ArrayList<>();
//		listFilesDownload.add("C:\\Data\\Dev\\Java\\StockPriceDownloader\\Data\\AAPL.csv");
		DataUploader dataUploader = new DataUploader();
		dataUploader.LoadCsvsToStockPriceTable(CONFIG_FILE_NAME, listFilesDownload);
		
		LocalUtils.CloseLogFile();
		
	}
}
