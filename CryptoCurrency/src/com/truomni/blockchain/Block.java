package com.truomni.blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.truomni.constants.Constants;
import com.truomni.cryptocurrency.CryptographyHelper;
import com.truomni.cryptocurrency.Transaction;

import lombok.Getter;
import lombok.Setter;

public class Block {
	private int id;
	private int nonce;
	private long timeStamp;
	
	//this is SHA-256 hash intenfies the Transaction
	@Getter @Setter private String hash;
	private String previousHash;
	
	//Etherium has 1500 to 2000 transactions
	private List<Transaction> transactions;
	
	public Block(String previousHash) {
		this.transactions = new ArrayList<>();
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		generateHash();
	}
	
	public void generateHash() {
		String hashData = Integer.toString(id) + previousHash + Long.toString(timeStamp)
									+transactions.toString()+Integer.toString(nonce);
		this.hash=CryptographyHelper.generateHash(hashData);
	}
	
//	public void addBlock(Block block) {
//		// TODO Auto-generated method stub
//	}
//		
	public boolean addTransactions(Transaction transaction) {
		if(transaction==null) {
			return false;
		}
		//if the block is the genesis block we do not process.\
		if((!previousHash.equals(Constants.GENESIS_PREV_HASH))) {
			if((!transaction.verifyTransaction())) {
				System.out.println("Transaction is not valid. Verification failed...");
					return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction is valid and added to the block " + this);
		return true;

	}
		
	
	
//	public String getHash() {
//		return hash;
//	}

	public void incrementNonce() {
		this.nonce++;
	}
	
	//



}
