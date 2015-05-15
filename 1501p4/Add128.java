/* 
   Hanyu Xiong
   1501 project 4
   hax12@pitt.edu
*/
import java.util.Random;

public class Add128 implements SymCipher{

	
	byte[] key = new byte[128];
	
	/*It will have two constructors, one without any parameters 
	 * and one that takes a byte array. The parameterless constructor 
	 * will create a random 128 byte additive key and store it in an 
	 * array of bytes. The other constructor will use the byte array 
	 * parameter as its key. The SecureChatClient will call the parameterless 
	 * constructor and the SecureChatServer calls the version with a parameter.
	 */
	public Add128() {
		Random rand = new Random(System.currentTimeMillis());
		for (int i=0; i<128; i++){
			int x = rand.nextInt(128);	//random bytes generated
			key[i] = (byte)x;
		}
	}
	
	public Add128(byte[] byteKey) {
		for (int i=0; i<128; i++){
			key[i] = byteKey[i];
		}
	}


	public byte[] getKey() {
		return key;
	}
	
	/*To implement the encode() method, convert the String parameter to 
	 * an array of bytes and simply add the corresponding byte of the key 
	 * to each index in the array of bytes. If the message is shorter than 
	 * the key, simply ignore the remaining bytes in the key. If the message 
	 * is longer than the key, cycle through the key as many times as necessary. 
	 * The encrypted array of bytes should be returned as a result of this 
	 * method call.
	 */
	public byte[] encode(String S) {
		char[] characters = S.toCharArray();	//array of chars of the string
		byte[] b = new byte[characters.length];
		byte[] encrypted = new byte[characters.length];
		
		System.out.println("corresponding byte[]: ");
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) characters[i];				//convert char array to byte array
			System.out.print(b[i]);
			encrypted[i] = (byte)(b[i]+key[i % (key.length)]);	//encrypt by adding byte value
			//System.out.println(encrypted[i]);
		}
		System.out.println("");
		
		return encrypted;
	}

	/*To decrypt the array of bytes, simply subtract the corresponding byte 
	 * of the key from each index of the array of bytes. If the message is 
	 * shorter than the key, simply ignore the remaining bytes in the key. If 
	 * the message is longer than the key, cycle through the key as many times 
	 * as necessary.  Convert the resulting byte array back to a String and return it.
	 */
	public String decode(byte[] bytes) {
		char[] c = new char[bytes.length];
		byte[] decrypted = new byte[bytes.length];
		
		System.out.println("decrypted byte[]: ");
		for (int i = 0; i < bytes.length; i++){
			decrypted[i] = (byte)(bytes[i]-key[i % (key.length)]);	//subtract corresponding byte value to decrypte 
			System.out.print(decrypted[i]);
			c[i] = (char)decrypted[i];		//convert byte array to char array
			//System.out.println(c[i]);
		}
		System.out.println("");
		
		String S2 = new String(c);	//convert char array back to string
		return S2;
	}

}
