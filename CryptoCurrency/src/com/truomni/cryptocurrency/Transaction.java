package com.truomni.cryptocurrency;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.truomni.blockchain.Blockchain;

import lombok.Getter;
import lombok.Setter;

public class Transaction {
	//id of the transaction is hash
	@Getter @Setter private String transactionId;
	
	//We use PublicKeys to reference the sender or receiver
	private PublicKey	sender;
	@Getter private PublicKey	receiver;

	//amount of the coins in  transaction sender sends to receiver
	@Getter private double	amount;
	
	// make sure the transaction is signed to prevent anyone else from spending the coins.
	private byte[] signature;

	//every transaction has inputs and outputs
	public List<TransactionInput> inputs;
	public List<TransactionOutput> outputs;
	
	public Transaction(PublicKey sender, PublicKey receiver, double amount, List<TransactionInput> inputs){
		//this.inputs = new ArrayList<TransactionInput>();
		this.outputs = new ArrayList<TransactionOutput>();
		this.sender = sender;	
		this.receiver = receiver;
		this.amount = amount;
		this.inputs = inputs;
		calculateHash();
		
	}

	public boolean verifyTransaction() {
		if(!verifySignature()) {
			System.out.println("Invalid transaction because of invalid signature...");
			return false;
		}
		//let us gether unspent transactions (we have to consider inputs)
		for(TransactionInput transactionInput:inputs){
			transactionInput.setUTXO(Blockchain.UTXOs.get(transactionInput.getTransationOutputId()));
		}
		
		//transactions have 2 parts: send an amount to the receiver + sent the (balance-amout) back to sender
		// send the value to the recipient.
		outputs.add(new TransactionOutput(this.receiver, amount, transactionId));
		outputs.add(new TransactionOutput(this.sender, getInputSum()-amount, transactionId));
		
		//UPDATE the UPDATE UTXOs
		// the outputs will be inputs for the other transaction (so put them in the blockchain's UTXOs)
		for(TransactionOutput transactionOutput: outputs) {
			Blockchain.UTXOs.put(transactionOutput.getId(), transactionOutput);
		}
		// remove the transaction inputs from the Blockchain's UTXOs list because they have been spent
		
		for(TransactionInput transactioninput: inputs) {
			if(transactioninput.getUTXO() !=null){
				Blockchain.UTXOs.remove(transactioninput.getUTXO().getId());
			}
		}
		return true;
	}
	
	public double getInputSum(){
		double sum = 0;
		for(TransactionInput transactionInput: inputs)
			if(transactionInput.getUTXO()!=null)			{
				sum+=transactionInput.getUTXO().getAmount();
			}
		return sum;
	}
	
	private String getHashData() {
		return sender.toString()+receiver.toString()+Double.toString(amount);
	}
	public void  generateSignature(PrivateKey privateKey){
		String data = getHashData();
		signature = CryptographyHelper.sign(privateKey,data);
	}

	public boolean verifySignature() {
		String data = getHashData();
		return CryptographyHelper.verify(sender, data, signature);
	}
	
	public void  calculateHash(){
		String hashData = getHashData();
		this.transactionId = CryptographyHelper.generateHash(hashData);
	}
}

