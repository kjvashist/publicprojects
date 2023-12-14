package com.truomni.cryptocurrency;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

public class CryptographyHelper {
	public static final String SHA_256 = "SHA-256"; //64 hex characters=>32 bytes=>256 bits 
	public static final String UTF_8 = "UTF-8";
	public static String generateHash(String data)
	{
		try
		{
			MessageDigest digest = MessageDigest.getInstance(SHA_256);
			byte[] hash = digest.digest(data.getBytes(UTF_8));
			StringBuilder hexaDecimalString = new StringBuilder();
			for(int i=0; i<hash.length;i++)
			{
				String hexadecimal = Integer.toHexString(0xff & hash[i]);
				//padding
				if(hexadecimal.length()==1) hexaDecimalString.append('0');
				hexaDecimalString.append(hexadecimal);
			}
			return hexaDecimalString.toString();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		//return "";
	}
	
	//generate public key and private key
	// private key:256 bits long random integer
	// public key:point on the elliptical curve
	//(x,y) = both of these values are 256 bit long
	public static KeyPair ellipticalCurveCrypto() {
		try{		
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
			ECGenParameterSpec params = new ECGenParameterSpec("prime256v1");
			keyPairGenerator.initialize(params);
			return  keyPairGenerator.generateKeyPair();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//ECC to sign the given transaction (message)
	// Elliptical Curve Digital Signature Algorithm (ECDSA) using Bouncy Castle
	public static byte[] sign(PrivateKey privateKey, String input){
		
		byte[] output = new byte[0];
		try{		
			//Use Bouncy Castle for Elliptic Curve Cryptography (ECC) with bouncy castle for asymmetric encryption
			Signature ecdsaSignature = Signature.getInstance("ECDSA", "BC");
			ecdsaSignature.initSign(privateKey);
			ecdsaSignature.update(input.getBytes());
			output = ecdsaSignature.sign();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		return output;
	}
	
	// checks whether the given transaction belongs to the sender based on the signature.
	public static boolean verify(PublicKey publicKey, String data, byte[] signature) {
		try{
			Signature ecdsaSignature = Signature.getInstance("ECDSA", "BC");
			ecdsaSignature.initVerify(publicKey);
			ecdsaSignature.update(data.getBytes());
			return ecdsaSignature.verify(signature);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
