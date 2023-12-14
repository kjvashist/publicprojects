package com.truomni.cryptocurrency;

import java.security.PublicKey;

import lombok.Getter;
import lombok.Setter;

public class TransactionOutput {
	//identifier of the transaction output (SHA-256)
	@Getter private String id;
	@Getter @Setter private String parentTransactionId;
	//the new owner of the coin
	@Getter @Setter private PublicKey receiver;
	
	// amount of the coins
	@Getter @Setter private double amount;
	
	public TransactionOutput(PublicKey receiver, double amount, String parentTransactionId) {
		this.receiver = receiver;
		this.amount = amount;
		this.parentTransactionId = parentTransactionId;
		generateId();
		
	}
	
	private void generateId() {
		this.id = CryptographyHelper.generateHash(receiver.toString()+Double.toString(amount)+parentTransactionId);
	}
	public boolean isMine(PublicKey publicKey) {
		return (publicKey == receiver);
	}
	
}
