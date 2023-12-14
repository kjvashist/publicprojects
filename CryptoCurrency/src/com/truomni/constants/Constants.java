package com.truomni.constants;

public class Constants {
	
	private Constants()
	{
		
	}
	public static final int HASH_LENGTH = 64;
	public static final int DIFFICULTY = 1;
	
	//Reward in underlying currency
	public static final double  MINER_REWARD = 6.25;

	//first block (genesis)Hash //64 characters
	//public static final String GENESIS_PREV_HASH = String.format("%0" + 64 + "d", 0);
	public static final String GENESIS_PREV_HASH = new String(new char[HASH_LENGTH]).replace('\0', '0');
	
	public static final String DIFFICULTY_LEADING_ZEROS = new String(new char[DIFFICULTY]).replace('\0', '0');
}
