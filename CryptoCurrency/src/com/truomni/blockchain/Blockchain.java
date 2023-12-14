package com.truomni.blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.truomni.cryptocurrency.TransactionOutput;

public class Blockchain {
	//this is the public ledger -- everyone can access.
	// all the blocks (previous transactions) on the blockchain
	public static List<Block> blockChain;
	
	//At any point it has every unspent transaction
	public static Map<String, TransactionOutput> UTXOs;
	
	public Blockchain() {
		Blockchain.UTXOs = new HashMap<String, TransactionOutput>();
		Blockchain.blockChain = new ArrayList<>();
	}
	
	public void addBlock(Block block) {
		Blockchain.blockChain.add(block);
	}
	
	public int size() {
		return Blockchain.blockChain.size();
	}
	
	public String toString(){
		StringBuilder blockChainString = new StringBuilder();
		for(Block block : Blockchain.blockChain)
			blockChainString.append(block.toString()+"\n");
		return blockChainString.toString();
	}
}


