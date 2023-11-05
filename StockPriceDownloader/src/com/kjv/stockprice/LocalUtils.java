package com.kjv.stockprice;

import java.time.Instant;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.Class;
//import java.lang.StackTraceElement;

public class LocalUtils {
	public static String _logFileName = "";

	
    public static long GetSecondsFromDateString(String dateString) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
		try {
			date = formatter.parse(dateString);
			return GetSeconds(date);
		} catch (ParseException exp) {
			LogException(exp);
		}
		return 0;
	}
    
	public static long GetSeconds(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return instant.getEpochSecond();
	}

	public static long GetSeconds(LocalDate localDate) {
		Calendar calendar = Calendar.getInstance();
		// Set the calendar to the LocalDate object
		calendar.set(Calendar.YEAR, localDate.getYear());
		calendar.set(Calendar.MONTH, localDate.getMonthValue() - 1);
		calendar.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
		// Convert the Calendar object to a Date object
		Date newDate = calendar.getTime();
		return GetSeconds(newDate);
	}

	public static String ReadConfigProperty(String configFileName, String propertyName) {
		// Specify the config file path
		// String configFileName = "config.properties";
		try (InputStream intputStream = new FileInputStream(configFileName)) {
			Properties properties = new Properties();
			// Load the Properties file
			properties.load(intputStream);
			String propertyVal = properties.getProperty(propertyName);
			return propertyVal;
		} catch (IOException io) {
			WriteMessageToLogFile(getStackTraceString(io));
		}
		return "";
	}

	public static int ReadConfigPropertyInt(String configFileName, String propertyName) {
		String str = ReadConfigProperty(configFileName, propertyName);
		return Integer.parseInt(str);
	}

	public static int GetRandom(int startNum, int endNum) {
		Random random = new Random();
		return random.nextInt(startNum + 1) + (endNum - startNum);
	}

	public static String GetMainClassName() {
		Class mainClass = null;
		try {
			mainClass = Class
					.forName(Thread.currentThread().getStackTrace()[Thread.currentThread().getStackTrace().length - 1]
							.getClassName());
		} catch (ClassNotFoundException e) {
			WriteMessageToLogFile(getStackTraceString(e));
		}
		if (mainClass != null) {
			return mainClass.getName();
		}
		return "";
	}

	public static String GetCurrentExecutablePath() {
		// Get the current executable file
		File currentWorkingDirectory = new File(".");
		String executableName = currentWorkingDirectory.getAbsolutePath();
		return executableName;
	}	
	public static String GetCurrentLogFolderName() {
		// Get the current executable file
		File currentWorkingDirectory = new File(".");
		String executableName = currentWorkingDirectory.getAbsolutePath();
		String logFolderName = Paths.get(executableName).resolve("Logs").toString();
		return logFolderName;
	}

	public static String GetFullPath(String parentPath, String childPath){
		return Paths.get(parentPath).resolve(childPath).toString();
	}
	
	public static String GetCurrentDatetime() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
		String formattedDatetime = now.format(formatter);
		return formattedDatetime;
	}

	public static String GetCurrentTimeForLog() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd_HH:mm:ss");
		String formattedDatetime = now.format(formatter);
		return formattedDatetime;
	}

	static FileWriter fileWriter = null;

	public synchronized  static boolean WriteMessageToLogFile(String strMessage) {
		
		try {
			if (_logFileName == "") {
				String mainClassName = GetMainClassName();
				String currentLogFolderName = GetCurrentLogFolderName();
				_logFileName = Paths.get(currentLogFolderName)
						.resolve(mainClassName + "_" + GetCurrentDatetime() + ".log").toString();
				fileWriter = new FileWriter(_logFileName, true);
			}
			// Write the message to the log file
			fileWriter.write(GetCurrentTimeForLog() + " " + strMessage + System.lineSeparator());
			// Close the FileWriter object
			// fileWriter.close();
			fileWriter.flush();
			return true;
		} catch (IOException io) {
			WriteMessageToLogFile(getStackTraceString(io));
		}
		return false;
	}

	public synchronized static boolean CloseLogFile() {

		try {
			if (fileWriter != null) {
				_logFileName = "";
				fileWriter.flush();
				fileWriter.close();
				fileWriter = null;
				return true;
			}
		} catch (IOException io) {
			WriteMessageToLogFile(getStackTraceString(io));
		} catch (Exception ex) {
			System.out.println(ex.getMessage() + System.lineSeparator() + getStackTraceString(ex));
		}
		return false;
	}

	public static String getStackTraceString(Throwable aThrowable) {
		final StringWriter result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}
	
	public static boolean FileExists(String fileName) {
		   File file = new File(fileName);
		   return file.exists();
	}

	public static String GetFileNameOnly(String fileNameWithPath) {
		Path path = Paths.get(fileNameWithPath);
	    return  path.getFileName().toString();
	}

	public static String DoubleQuote(String strVar) {
		return "\""+strVar+"\"";
	}
	
	public static String SingleQuote(String strVar) {
		return "'"+strVar+"'";
	}

	
	public static boolean LogException(Exception ex) {
		String msg = ex.getClass().getCanonicalName() + System.lineSeparator() + ex.getMessage() + System.lineSeparator() + getStackTraceString(ex);
		WriteMessageToLogFile(msg);
		return true;
	}

}
