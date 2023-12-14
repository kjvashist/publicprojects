package com.truomni.cryptocurrency;
import com.truomni.blockchain.Block;
import com.truomni.blockchain.Blockchain;
import com.truomni.constants.Constants;

public class Miner {
	private double reward;

	public void mine(Block block, Blockchain blockChain) {
		// get the valid hash with difficulty can be time consuming.
		//Proof Of Work PoW. With Ethereum 2.0 Proof Of Stake will be used (select random nodes for verification instead of all.
		long startMillis = System.currentTimeMillis();
		int ctr=0;
		while(!isGoldenHash(block))
		{
			block.incrementNonce();
			block.generateHash();
			ctr++;
		}
		long milliSecsTaken = System.currentTimeMillis()-startMillis;
		String hash = block.getHash(); 
		System.out.println(String.format("ctr=%,d Time Taken milliSecs=%,d for mining with Hash %s Block=%s",ctr,milliSecsTaken, hash, block));
		
		blockChain.addBlock(block);
		
		reward+=Constants.MINER_REWARD;
	}
	public double getReward()
	{
		return this.reward;
	}
	
	private boolean isGoldenHash(Block block) {
		//return block.getHash().substring(0, Constants.DIFFICULTY).equals(Constants.DIFFICULTY_LEADING_ZEROS);
		String hash = block.getHash();
		boolean goldenHash =  hash.substring(0, Constants.DIFFICULTY).equals(Constants.DIFFICULTY_LEADING_ZEROS);
		if(goldenHash == true)
		{
			System.out.println("goldenHash ="+ hash);
		}
		else
		{
			System.out.println("goldenHash false "+ hash);
		}
		return goldenHash;
	}
}
