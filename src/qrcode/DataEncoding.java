package qrcode;

import java.nio.charset.StandardCharsets;

import reedsolomon.ErrorCorrectionEncoding;

public final class DataEncoding {

	/**
	 * @param input
	 * @param version
	 * @return
	 */
	public static boolean[] byteModeEncoding(String input, int version) {
		// TODO Implementer
		return null;
	}

	/**
	 * @param input
	 *            The string to convert to ISO-8859-1
	 * @param maxLength
	 *          The maximal number of bytes to encode (will depend on the version of the QR code) 
	 * @return A array that represents the input in ISO-8859-1. The output is
	 *         truncated to fit the version capacity
	 */
	public static int[] encodeString(String input, int maxLength) {
		byte[] arrayBytes = input.getBytes(StandardCharsets.ISO_8859_1);
		int[] arrayInt = new int[maxLength];

		for(int i = 0; i < arrayBytes.length && i < maxLength; i++){
			arrayInt[i] = arrayBytes[i] & 0xFF;
		}

		return arrayInt;
	}

	/**
	 * Add the 12 bits information data and concatenate the bytes to it
	 * 
	 * @param inputBytes
	 *            the data byte sequence
	 * @return The input bytes with an header giving the type and size of the data
	 */
	public static int[] addInformations(int[] inputBytes) {
		int prefix = 0b0100;
		int size = inputBytes.length;
		int[] newInputBytes = new int[size+2];

		for(int i = 0; i <= size+1; i++){
			if(i == 0){
				newInputBytes[i] = (prefix << 4) | (size >> 4);
			} else if(i == 1){
				newInputBytes[i] = (size&0xF) << 4 | inputBytes[0] >> 4;
			} else if(i == (size+1)){
				newInputBytes[i] = (inputBytes[i-2]&0xF) << 4;
			} else {
				newInputBytes[i] = (inputBytes[i-2]&0xF) << 4 | inputBytes[i-1] >> 4;
			}
		}

		return newInputBytes;
	}

	/**
	 * Add padding bytes to the data until the size of the given array matches the
	 * finalLength
	 * 
	 * @param encodedData
	 *            the initial sequence of bytes
	 * @param finalLength
	 *            the minimum length of the returned array
	 * @return an array of length max(finalLength,encodedData.length) padded with
	 *         bytes 236,17
	 */
	public static int[] fillSequence(int[] encodedData, int finalLength) {
		// TODO Implementer
		return null;
	}

	/**
	 * Add the error correction to the encodedData
	 * 
	 * @param encodedData
	 *            The byte array representing the data encoded
	 * @param eccLength
	 *            the version of the QR code
	 * @return the original data concatenated with the error correction
	 */
	public static int[] addErrorCorrection(int[] encodedData, int eccLength) {
		// TODO Implementer
		return null;
	}

	/**
	 * Encode the byte array into a binary array represented with boolean using the
	 * most significant bit first.
	 * 
	 * @param data
	 *            an array of bytes
	 * @return a boolean array representing the data in binary
	 */
	public static boolean[] bytesToBinaryArray(int[] data) {
		// TODO Implementer
		return null;
	}

}
