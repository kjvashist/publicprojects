package com.truomni;

import java.security.KeyPair;
import java.security.Security;
import java.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.truomni.blockchain.Block;
import com.truomni.blockchain.Blockchain;
import com.truomni.constants.Constants;
import com.truomni.cryptocurrency.CryptographyHelper;
import com.truomni.cryptocurrency.Miner;
import com.truomni.cryptocurrency.Transaction;
import com.truomni.cryptocurrency.TransactionOutput;
import com.truomni.cryptocurrency.Wallet;

public class App {

	public static void main(String[] args) {
		//we use Bouncy Castle as Cryptography Related provider
		Security.addProvider(new BouncyCastleProvider());
		
		//Create Wallets + Blockchain + the single miner in the network
		Wallet userA= new Wallet();
		Wallet userB= new Wallet();
		Wallet lender= new Wallet();
		Blockchain blockchain = new Blockchain();
		Miner miner = new Miner(); 
		
		// create genesis transaction that sends 500 coins to userA
		Transaction genesisTransaction = new Transaction(lender.getPublicKey() , userA.getPublicKey(), 500, null);
		genesisTransaction.generateSignature(lender.getPrivateKey());
		genesisTransaction.setTransactionId("0");
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.getReceiver(),
				genesisTransaction.getAmount(),genesisTransaction.getTransactionId()));
		Blockchain.UTXOs.put(genesisTransaction.outputs.get(0).getId(), genesisTransaction.outputs.get(0));
		
		System.out.println("Constructing the genesis Block");
		Block genesisBlock = new Block(Constants.GENESIS_PREV_HASH); 
		genesisBlock.addTransactions(genesisTransaction);
		miner.mine(genesisBlock, blockchain);
		
		Block  block1 = new Block(genesisBlock.getHash());
		System.out.println("\nUserA's balance is :" + userA.calculateBalance());
		System.out.println("\nUserA's tries to send money (120 coins) to the UserB...");
		block1.addTransactions(userA.transferMoney(userB.getPublicKey(), 120));
		miner.mine(block1, blockchain);
		System.out.println("\nUserA's balance is :" + userA.calculateBalance());
		System.out.println("\nUserB's balance is :" + userB.calculateBalance());

		Block  block2 = new Block(block1.getHash());
		System.out.println("\nUserA's tries to send money (600 coins) to the UserB...");
		block2.addTransactions(userA.transferMoney(userB.getPublicKey(), 600));
		miner.mine(block2, blockchain);
		System.out.println("\nUserA's balance is :" + userA.calculateBalance());
		System.out.println("\nUserB's balance is :" + userB.calculateBalance());

		Block  block3 = new Block(block2.getHash());
		System.out.println("\nUserB's tries to send money (110 coins) to the UserA...");
		block3.addTransactions(userB.transferMoney(userA.getPublicKey(), 110));
		
		System.out.println("\nUserA's balance before mine is :" + userA.calculateBalance());
		System.out.println("\nUserB's balance before mine is :" + userB.calculateBalance());
		
		miner.mine(block3, blockchain);
		
		System.out.println("\nUserA's balance before mine is :" + userA.calculateBalance());
		System.out.println("\nUserB's balance before mine is :" + userB.calculateBalance());

		System.out.println("\nMiner's reward :" + miner.getReward());
		
		
		
//		KeyPair keys = CryptographyHelper.ellipticalCurveCrypto();
//		System.out.println(keys.getPrivate().toString());
//		System.out.println(keys.getPublic().toString());
//		
//		System.out.println("PrivateKey = "+Base64.getEncoder().encodeToString(keys.getPrivate().getEncoded()));
//		System.out.println("PublicKey = "+Base64.getEncoder().encodeToString(keys.getPublic().getEncoded()));
	}

}
