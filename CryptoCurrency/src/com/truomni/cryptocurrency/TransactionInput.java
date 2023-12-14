package com.truomni.cryptocurrency;

import lombok.Getter;
import lombok.Setter;

public class TransactionInput {
	//every input has an output. This id is the transactionId of the TransactionOutput
	@Getter @Setter private String transationOutputId;
	//this is the unspent transaction output
	@Getter @Setter private TransactionOutput UTXO;
	
	public TransactionInput(String transationOutputId) {
		this.transationOutputId = transationOutputId;
	}
	
//	public String getTransationOutputId() {
//		return 	transationOutputId;
//	}
//	public void setTransationOutputId(String transationOutputId) {
//		this.transationOutputId= transationOutputId;
//	}
//	
//	public TransactionOutput getUTXO() {
//		return 	UTXO;
//	}
//	public void setUTXO(TransactionOutput UTXO) {
//		this.UTXO=UTXO;
//	}
	
}
