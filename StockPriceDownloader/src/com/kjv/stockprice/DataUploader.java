package com.kjv.stockprice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class DataUploader {

	public int LoadCsvsToStockPriceTable(String configFileName, List<String> listCsvFiles) {
		String sqlLitePath = LocalUtils.ReadConfigProperty(configFileName, "SQLITE_DATABASE_PATH");
		String stockPriceTableName = LocalUtils.ReadConfigProperty(configFileName, "StockPriceTableName");
		try
		{
			if(!LocalUtils.FileExists(sqlLitePath)){
				String localPath=LocalUtils.GetCurrentExecutablePath();
				sqlLitePath=LocalUtils.GetFullPath(localPath, sqlLitePath);
			}
			Connection dbConn = DriverManager.getConnection("jdbc:sqlite:" + sqlLitePath);
			return LoadCsvsToDBTable(dbConn, listCsvFiles, stockPriceTableName);
		}
		catch(SQLException sqlEx) {
			LocalUtils.LogException(sqlEx);
		}
		catch(Exception exception) {
			LocalUtils.LogException(exception);
		}			
		return -1;
	}
	
	
	public int LoadCsvsToDBTable(Connection dbConn, List<String> listCsvFiles, String tableName)
	{
		int filesCount=listCsvFiles.size();
		int totalTimeTakenOnOnFileUpload=0;
		int fileCtr=0;
		for(;fileCtr<filesCount;fileCtr++)
		{
			long startFileUploadTime = System.currentTimeMillis();
			
			String csvFileName = listCsvFiles.get(fileCtr);
			
			String msg=String.format("Starting load [%s],",csvFileName);
			LocalUtils.WriteMessageToLogFile(msg);
			try
			{
			UploadCsvFileToDBTable(dbConn, csvFileName, tableName);
			long endFileUploadTime = System.currentTimeMillis();
			totalTimeTakenOnOnFileUpload += (endFileUploadTime - startFileUploadTime);
			LocalUtils.WriteMessageToLogFile(
					String.format("File [%s] Upload success! Time taken =%.2f seconds.",
							csvFileName, (endFileUploadTime - startFileUploadTime) / 1000.0));
			}
			catch(SQLException sqlEx) {
				LocalUtils.LogException(sqlEx);
			}
			catch(FileNotFoundException fileNotFoundException) {
				LocalUtils.LogException(fileNotFoundException);
			}
			catch(IOException ioException) {
				LocalUtils.LogException(ioException);
			}
			catch(Exception exception) {
				LocalUtils.LogException(exception);
			}			
		}
		LocalUtils.WriteMessageToLogFile(String.format("The Total time to Upload=%.2f seconds %d files.",
				totalTimeTakenOnOnFileUpload / 1000.0, filesCount));
		return fileCtr;
	}
	public boolean UploadCsvFileToDBTable(Connection dbConn, String csvFileName, String tableName) throws SQLException, FileNotFoundException, IOException{
		  // Create a PreparedStatement object to insert data into the SQLite table
        // Create a CSVParser object to parse the CSV file
        
		String fileNameOnly=LocalUtils.GetFileNameOnly(csvFileName);
		String ticker = fileNameOnly.substring(0, fileNameOnly.length()-4);

		CSVParser parser = new CSVParser(new FileReader(csvFileName), CSVFormat.DEFAULT);
		List<CSVRecord> records = parser.getRecords();
		
		CSVRecord columnNames = records.get(0);
		List<String> columnNamesList = new ArrayList<>();
		columnNamesList.add(LocalUtils.DoubleQuote("Ticker"));
		for(String colName: columnNames)
		{
			columnNamesList.add(LocalUtils.DoubleQuote(colName));
		}
		
        boolean tableCreated = CreateTable(dbConn, tableName,  columnNamesList);
        String commandSeparatedColumns = String.join(",", columnNamesList);
        String valuesPlaceHolder = "?,".repeat(columnNamesList.size()-1)+"?"; 
        
        String insertStatement = String.format("INSERT INTO %s ( %s ) VALUES ( %s )", tableName, commandSeparatedColumns, valuesPlaceHolder);
        PreparedStatement pstmt = dbConn.prepareStatement(insertStatement);
        if(tableCreated)
        {
	        // Iterate over the CSV records and insert them into the database
	        for (int recCtr=1;recCtr<records.size();recCtr++) {
	        	CSVRecord record = records.get(recCtr);
	        	pstmt = dbConn.prepareStatement(insertStatement);
	        	pstmt.setString(1, ticker);
	        	for(int colCtr=0;colCtr<record.size();colCtr++){
        			String colVal = record.get(colCtr);
        			pstmt.setString(colCtr+2, colVal);
        		}
	            // Execute the PreparedStatement object
	            pstmt.executeUpdate();
	            //LocalUtils.WriteMessageToLogFile(insertStatement);
	        }
	        
	        //String sqlDeleteInsert="delete sp from StockPricesRaw raw left inner join StockPrices sp on raw.Ticker = sp.Ticker and raw.\"Date\"  = sp.PriceDate where sp.PriceDate is not NULL;";
	        
	        String sqlDeleteInsert="DELETE FROM StockPrices WHERE ROWID IN (select StockPrices.ROWID from StockPrices inner join StockPricesRaw on StockPrices.Ticker = StockPricesRaw.Ticker and StockPrices.PriceDate = StockPricesRaw.\"Date\") ;";
	        PreparedStatement preparedStatement = dbConn.prepareStatement(sqlDeleteInsert);
	        preparedStatement.executeUpdate();
	        LocalUtils.WriteMessageToLogFile(sqlDeleteInsert);
	        
	        sqlDeleteInsert="INSERT INTO StockPrices (Ticker,PriceDate,Open,High,Low,Close,AdjClose,Volume) select raw.Ticker, raw.\"Date\", raw.Open,raw.High,raw.Low,raw.Close, raw.\"Adj Close\", raw.Volume from StockPricesRaw raw left outer join StockPrices sp on raw.Ticker = sp.Ticker and raw.\"Date\" = sp.PriceDate where sp.PriceDate is NULL;";
	        preparedStatement = dbConn.prepareStatement(sqlDeleteInsert);
	        preparedStatement.executeUpdate();
	        LocalUtils.WriteMessageToLogFile(sqlDeleteInsert);
	        // Close the PreparedStatement object and the connection
	        pstmt.close();
	        return true;
        }
        return false;
	}
	
	public boolean CreateTable(Connection dbConn, String tableName, List<String> columnNamesList) throws SQLException {
		Statement statement = dbConn.createStatement();
	    statement.executeUpdate(String.format("DROP TABLE IF EXISTS %s;",tableName));
		    
	    // Create a SQL CREATE TABLE statement using the column names
	    String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
	    int columnsCount=columnNamesList.size();
	    for(int i=0;i<columnsCount;i++)
	    {
	    	String columnName=columnNamesList.get(i);
	    	createTableSQL += columnName + " VARCHAR(255)";
	    	
	    	if(i<columnsCount-1)
	    	{
	    		createTableSQL += ",";
	    	}
	    }
	    createTableSQL += ")";
	    // Execute the CREATE TABLE statement
	    LocalUtils.WriteMessageToLogFile(createTableSQL);
	    dbConn.createStatement().executeUpdate(createTableSQL);
	    return true;
	}
}
