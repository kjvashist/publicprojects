package com.truomni.cryptocurrency;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.truomni.blockchain.Blockchain;

import lombok.Getter;

public class Wallet {
	
	//users of the network used for signature
	@Getter private PrivateKey privateKey;
	
	
	//for verification =>address RIPMD public Key (160 bits)
	//RIPEMD-160 is a 160-bit cryptographic hash function. 
	@Getter private PublicKey publicKey;
	
	public Wallet() {
		KeyPair keypair = CryptographyHelper.ellipticalCurveCrypto();
		this.privateKey = keypair.getPrivate();
		this.publicKey = keypair.getPublic();
	}
	
	// there is balance stored for each user
	// UTXOs and consider all the transactions in the past.
	public double calculateBalance() {
		double balance=0;
		for(Map.Entry<String, TransactionOutput> item: Blockchain.UTXOs.entrySet()) {
			TransactionOutput transactionOutput = item.getValue();
			if(transactionOutput.isMine(publicKey)) {
				balance+=transactionOutput.getAmount();
			}
		}
		return balance;
	}
	
	//purpose of crypto is to be able to transfer money
	// miners of the blockchain will put this transaction into the blockchain. ETHerium has about 2000 transactions in a block
	public Transaction transferMoney(PublicKey receiver, double amount){
		double balance=calculateBalance();
		if(balance<amount){
			System.out.println("Invalid transaction because of not enough money...");
			return null;
		}
		//
		List<TransactionInput> inputs = new ArrayList<TransactionInput>();
		for(Map.Entry<String, TransactionOutput> item: Blockchain.UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			if(UTXO.isMine(this.publicKey)) {
				inputs.add(new TransactionInput(UTXO.getId()));
			}
		}
		//create the new transaction for this request.
		Transaction newTransaction = new Transaction(publicKey, receiver, amount, inputs);
		newTransaction.generateSignature(privateKey);
		return newTransaction;
	}
	

}
