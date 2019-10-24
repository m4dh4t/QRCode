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
		int[] infoInputBytes = new int[size+2];

		for(int i = 0; i <= size+1; i++){
			if(i == 0){
				infoInputBytes[i] = (prefix << 4) | (size >> 4);
			} else if(i == 1){
				infoInputBytes[i] = (size&0xF) << 4 | inputBytes[0] >> 4;
			} else if(i == (size+1)){
				infoInputBytes[i] = (inputBytes[i-2]&0xF) << 4;
			} else {
				infoInputBytes[i] = (inputBytes[i-2]&0xF) << 4 | inputBytes[i-1] >> 4;
			}
		}

		return infoInputBytes;
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
        if(encodedData.length >= finalLength){
            return encodedData;
        } else {
            int[] filledEncodedData = new int[finalLength];
            int numbersAdded = 0;

            for(int i = 0; i < finalLength; i++){
                if(i < encodedData.length){
                    filledEncodedData[i] = encodedData[i];
                } else {
                    if(numbersAdded % 2 == 0){
                        filledEncodedData[i] = 236;
                    } else {
                        filledEncodedData[i] = 17;
                    }
                    ++numbersAdded;
                }
            }
            return filledEncodedData;
        }
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
        int[] eccEncodedData = new int[encodedData.length + eccLength];
        int[] eccArray = ErrorCorrectionEncoding.encode(encodedData, eccLength);
        int eccDataAdded = 0;

        for(int i = 0; i < eccEncodedData.length; i++){
            if(i < encodedData.length){
                eccEncodedData[i] = encodedData[i];
            } else {
                eccEncodedData[i] = eccArray[eccDataAdded];
                ++eccDataAdded;
            }
        }

		return eccEncodedData;
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
        boolean[] binaryArray = new boolean[data.length*8];

        for(int i = 0; i < data.length; i++){
            int bitsLeft = 7;
            while(data[i] > 0){
                binaryArray[bitsLeft + (i*8)] = (data[i] % 2 != 0);
                data[i] /= 2;

                --bitsLeft;
            }
        }
		return binaryArray;
	}

}
