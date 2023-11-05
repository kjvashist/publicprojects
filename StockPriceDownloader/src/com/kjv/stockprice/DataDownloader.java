package com.kjv.stockprice;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class DataDownloader {
	public List<String> DownloadStockPrices(String configFileName) {
		String stockTickers = LocalUtils.ReadConfigProperty(configFileName, "StockTickers");
		String[] symbolsArray = stockTickers.split("\\s*,\\s*");
		return DownloadStockPrices(configFileName, symbolsArray);
	}
	
	public List<String> DownloadStockPrices(String configFileName, String[] symbols) {
		long startTime = System.currentTimeMillis();
		List<String>  listFilesDownload = new ArrayList<>();
		int filesDownloaded=0;
		int totalTimeTakenOnOnFileDownload = 0;
		try {
			LocalDate endDate = LocalDate.now();
			LocalDate startDate = endDate.minusMonths(12);

			long secondsEndDate = LocalUtils.GetSeconds(endDate);
			long secondsStartDate = LocalUtils.GetSeconds(startDate);
			
//			endDate = endDate.minusMonths(6);
//			secondsEndDate = LocalUtils.GetSeconds(endDate);
//			secondsStartDate = LocalUtils.GetSecondsFromDateString("2022-01-04");
//			
			if(secondsStartDate==0)
				return listFilesDownload;
			String dataFileFolder = LocalUtils.ReadConfigProperty(configFileName, "DataFilesFolder");
			int minSecondsToSleep = LocalUtils.ReadConfigPropertyInt(configFileName, "MinSecondsToSleep");
			int maxSecondsToSleep = LocalUtils.ReadConfigPropertyInt(configFileName, "MaxSecondsToSleep");
			// String urlTemplate =
			// "https://query1.finance.yahoo.com/v7/finance/download/[TICKER]?period1=[START_SECONDS]&period2=[END_SECONDS]&interval=1d&events=history&includeAdjustedClose=true";
			String urlTemplate = LocalUtils.ReadConfigProperty(configFileName, "URLTemplate");

			// Get the current working directory
			String currentWorkingDirectory = System.getProperty("user.dir");
			// Concatenate the folder name to the current working directory
			String fullPath = currentWorkingDirectory + File.separator + dataFileFolder;
			// Append a separator to the end of the path if it does not end with a separator
			if (!fullPath.endsWith(File.separator)) {
				fullPath += File.separator;
			}

			int fileCount = symbols.length;
			String ticker = "";
			for (int fileCtr = 0; fileCtr < fileCount; fileCtr++) {
				try {
					// String ticker = symbol.replace(".","-");
					int randomSeconds = LocalUtils.GetRandom(minSecondsToSleep, maxSecondsToSleep);
					ticker = symbols[fileCtr];
					Thread.sleep(randomSeconds * 1000);
					LocalUtils.WriteMessageToLogFile(
							String.format("Starting download for Ticker [%s] after %d seconds %d#%d", ticker,
									randomSeconds, fileCtr + 1, fileCount));

					long startFileDownloadTime = System.currentTimeMillis();

					secondsEndDate = LocalUtils.GetSeconds(endDate);
					secondsStartDate = LocalUtils.GetSeconds(startDate);

					String tickerUrl = urlTemplate.replace("[TICKER]", ticker)
							.replace("[START_SECONDS]", String.valueOf(secondsStartDate))
							.replace("[END_SECONDS]", String.valueOf(secondsEndDate));
					String fileNameWithFullPath = fullPath + String.format("%s.csv", ticker);

					URI uri = new URI(tickerUrl);
					URL urlObj = uri.toURL();
					HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
					// connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0;
					// Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0
					// Safari/537.36");
					connection.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/105.0.0.0");
					// Get the input stream of the connection
					BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
					// Create an output stream to save the file to disk
					FileOutputStream outputStream = new FileOutputStream(fileNameWithFullPath);
					// Read bytes from the input stream and write them to the output stream
					int bytesRead = -1;
					byte[] buffer = new byte[1024];
					while ((bytesRead = inputStream.read(buffer)) > -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
					// Close the streams and the connection
					inputStream.close();
					outputStream.close();
					connection.disconnect();
					long endFileDownloadTime = System.currentTimeMillis();
					totalTimeTakenOnOnFileDownload += (endFileDownloadTime - startFileDownloadTime);
					LocalUtils.WriteMessageToLogFile(
							String.format("File [%s] downloaded successfully! Time taken =%.2f seconds.",
									fileNameWithFullPath, (endFileDownloadTime - startFileDownloadTime) / 1000.0));
					if(LocalUtils.FileExists(fileNameWithFullPath))
					{
						filesDownloaded++;
						listFilesDownload.add(fileNameWithFullPath);
					}
				} catch (Exception ex) {
					LocalUtils.WriteMessageToLogFile(
							String.format("Error in Stock Price Downloader for Symbol {%s} {%s} {%s} {%s} {%s} {%s}",
									ticker, ex.getMessage(), System.lineSeparator(), ex.getStackTrace().toString()));
				}

			}
		} catch (Exception ex) {
			LocalUtils.WriteMessageToLogFile("Error in Stock Price Downloader " + ex.getMessage()
					+ System.lineSeparator() + ex.getStackTrace().toString());
		}
		long elapsedTimeMilliSecs = System.currentTimeMillis() - startTime;
		LocalUtils.WriteMessageToLogFile(String.format(
				"The Total Time %.2f seconds, FileDownload only=%.2f seconds to complete download %d files.",
				elapsedTimeMilliSecs / 1000.0, totalTimeTakenOnOnFileDownload / 1000.0, symbols.length));
		return listFilesDownload;
	}
}
