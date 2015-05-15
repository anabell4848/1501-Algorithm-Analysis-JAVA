import java.util.Random;

/* 
   Hanyu Xiong
   1501 project 4
   hax12@pitt.edu
*/

public class Substitute implements SymCipher{

	byte[] key = new byte[256];
	byte[] dkey = new byte[256];
	
	/*It will have two constructors, one without any parameters and one 
	 * that takes a byte array. The parameterless constructor will create 
	 * a random 256 byte array which is a permutation of the 256 possible 
	 * byte values and will serve as a map from bytes to their substitution 
	 * values. For example, if location 65 of the key array has the value 
	 * 92, it means that byte value 65 will map into byte value 92. Note 
	 * that you will also need an inverse mapping array for this cipher, 
	 * which can be easily derived from the substitution array (so you only 
	 * need to send the original substitution array to the server). Be 
	 * careful with this class since byte values can be negative, but array 
	 * indices cannot be negative – this issue can be resolved with some 
	 * thought. The other constructor will use the byte array parameter as 
	 * its key. The SecureChatClient will call the parameterless constructor 
	 * and the SecureChatServer calls the version with a parameter.
	 */
	public Substitute() {
		Random rand = new Random(System.currentTimeMillis());
		for (int i=0; i<256; i++){
			int x= rand.nextInt(256);
			for (int j=0; j<=i; j++){
				if (key[j]==x){
					x= rand.nextInt(256);
					j=-1;
				}
			}
			key[i] = (byte)x;
			dkey[x]=(byte)i;
		}
	}
	
	public Substitute(byte[] byteKey) {
		for (int i=0; i<256; i++){
			key[i] = byteKey[i];
		}
	}

	public byte[] getKey() {
		return key;
	}

	/* To implement the encode() method, convert the String parameter to an 
	 * array of bytes, then iterate through all of the bytes, substituting the 
	 * appropriate bytes from the key. Again, be careful with negative byte 
	 * values.
	 */
	public byte[] encode(String S) {
		char[] characters = S.toCharArray();	//array of chars of the string
		byte[] b = new byte[characters.length];
		byte[] encrypted = new byte[characters.length];
		
		System.out.println("corresponding byte[]: ");
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) characters[i];	//convert to char array to byte array
			System.out.print(b[i]);
			encrypted[i] = key[b[i]];			//make encrypted array 
			//System.out.println(encrypted[i]);
		}

		System.out.println("");
		return encrypted;
	}

	/*To decode, simply reverse the substitution (using your decode byte array) 
	 * and convert the resulting bytes back to a String.
	 */
	public String decode(byte[] bytes) {
		char[] c = new char[bytes.length];
		byte[] decrypted = new byte[bytes.length];
		
		System.out.println("decrypted byte[]: ");
		for (int i = 0; i < bytes.length; i++){
			for (int j=0; j<key.length; j++){
				if (key[j]==bytes[i]){
					//System.out.println("key["+j+"]="+key[j]+"    bytes["+i+"]="+bytes[i]);
					decrypted[i] = dkey[j];
				}
			}
			c[i] = (char)decrypted[i];
			System.out.print(decrypted[i]);
		}
		System.out.println("");

		String S2 = new String(c);	//convert char array back to string
		//System.out.println(S2);
		return S2;
	}

}
