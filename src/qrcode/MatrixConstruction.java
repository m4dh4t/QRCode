package qrcode;

public class MatrixConstruction {

	/*
	 * Constants defining the color in ARGB format
	 * 
	 * W = White integer for ARGB
	 * 
	 * B = Black integer for ARGB
	 * 
	 * both needs to have their alpha component to 255
	 */
	public static final int W = 0xFF_FF_FF_FF;
	public static final int B = 0xFF_00_00_00;

	/*
	 * Global variables to prevent hardcoding values
	 */
	public static final int finderPatternSize = 7;
	public static final int alignmentPatternSize = 5;
	public static final int timingPosition = 6;

	public static int matrixSize;

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @param mask
	 *            The mask used on the data. If not valid (e.g: -1), then no mask is
	 *            used.
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data, int mask) {

		/*
		 * PART 2
		 */
		int[][] matrix = constructMatrix(version, mask);
		/*
		 * PART 3
		 */
		addDataInformation(matrix, data, mask);

		return matrix;
	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** PART 2 *********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create a matrix (2D array) ready to accept data for a given version and mask.
	 * 
	 * @param version
	 *          The version number of QR code (has to be between 1 and 4 included)
	 * @param mask
	 *          The mask id to use to mask the data modules. Has to be between 0
	 *          and 7 included to have a valid matrix. If the mask id is not
	 *          valid, the modules would not be not masked later on, hence the
	 *          QR Code would not be valid
	 * @return	The QR Code with the patterns and format information modules
	 *         	initialized. The modules where the data should be remain empty.
	 */
	public static int[][] constructMatrix(int version, int mask) {
		int[][] matrix = initializeMatrix(version);
		addFinderPatterns(matrix);
		addAlignmentPatterns(matrix, version);
		addTimingPatterns(matrix);
		addDarkModule(matrix);
		addFormatInformation(matrix, mask);
		return matrix;
	}

	/**
	 * Create an empty 2d array of integers of the size needed for a QR code of the
	 * given version
	 * 
	 * @param version
	 *            the version number of the qr code (has to be between 1 and 4
	 *            included
	 * @return an empty matrix
	 */
	public static int[][] initializeMatrix(int version) {
		matrixSize = QRCodeInfos.getMatrixSize(version);
		return new int[matrixSize][matrixSize];
	}

	/**
	 * Builds the different so-called "patterns" used inside a QR Code,
	 * i.e. the 3 squares at the corners and the one inside (for the first versions).
	 *
	 * @param size
	 * 			The size of the biggest square of the pattern
	 * @return 	A 2D array representing 3 different squares inside of each other
	 */
	public static int[][] patternBuilder(int size) {
		int[][] pattern = new int[size][size];
		for (int i=0 ; i<size ; i++) {
			for (int j=0 ; j<size ; j++) {
				pattern[i][j] = B;
			}
		}
		for (int i=1 ; i<(size-1); i++) {
			for (int j=1 ; j<(size-1); j++) {
				pattern[i][j] = W;
			}
		}
		for (int i=2 ; i<(size-2); i++) {
			for (int j=2 ; j<(size-2); j++) {
				pattern[i][j] = B;
			}
		}
		return pattern;
	}

	/**
	 * Builds what is going to be used as a separator between
	 * the finder patterns and the rest of the QR Code.
	 *
	 * @return	A 2D array representing a white square which
	 * 			will be placed behind the finder patterns
	 */
    public static int [][] separatorBuilder() {
        int size = finderPatternSize+1;
        int[][] separator = new int[size][size];
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                separator[i][j] = W;
            }
        }
        return separator;
    }

	/**
	 * Places any element represented as an array inside the given matrix.
	 *
	 * @param matrix
	 *         	The 2D array to modify representing a QR Code: where new elements will be added
	 * @param element
	 * 			The 2D array that will be added inside the given matrix
	 * @param x
	 * 			The x coordinate where the given element
	 * 			will be placed inside the given matrix
	 * @param y
	 * 			The y coordinate where the given element
	 * 			will be placed inside the given matrix
	 */
	public static void elementPlacer(int[][] matrix, int[][] element, int x, int y) {
		for (int i=0; i<element.length; i++) {
			for (int j=0; j<element.length; j++) {
				matrix[i+x][j+y]=element[i][j];
			}
		}
	}

	/**
	 * Adds all finder patterns to the given matrix with a border of White modules.
	 *
	 * @param matrix
	 *            	The 2D array to modify representing a QR Code: where to add the patterns
	 */
	public static void addFinderPatterns(int[][] matrix) {
		int[][] finderPattern = patternBuilder(finderPatternSize);
		int[][] separator = separatorBuilder();

        //TOP LEFT
        elementPlacer(matrix, separator, 0, 0);
        elementPlacer(matrix, finderPattern, 0, 0);

        //TOP RIGHT
        elementPlacer(matrix, separator, matrixSize-(finderPatternSize+1), 0);
        elementPlacer(matrix, finderPattern, matrixSize - finderPatternSize, 0);

        //BOTTOM LEFT
        elementPlacer(matrix, separator, 0, matrixSize-(finderPatternSize+1));
        elementPlacer(matrix, finderPattern, 0, matrixSize - finderPatternSize);
	}

	/**
	 * Adds the alignment pattern to the given matrix if needed, does nothing for version 1
	 *
	 * @param matrix
	 *            The 2D array to modify
	 * @param version
	 *            The version number of the QR code needs to be between 1 and 4
	 *            included
	 */
	public static void addAlignmentPatterns(int[][] matrix, int version) {
		if (version>1) {
			int[][] alignmentPattern = patternBuilder(alignmentPatternSize);
			elementPlacer(matrix, alignmentPattern, matrixSize - 9,matrixSize - 9);
		}
	}

	/**
	 * Adds timing patterns the the given matrix.
	 *
	 * @param matrix
	 *            The 2D array to modify
	 */
	public static void addTimingPatterns(int[][] matrix) {
		for (int i = (finderPatternSize+1); i < (matrixSize - finderPatternSize-1); i++) {
			if (i % 2 == 0) {
				matrix[timingPosition][i] = B;
                matrix[i][timingPosition] = B;
            } else {
				matrix[timingPosition][i] = W;
                matrix[i][timingPosition] = W;
            }
		}
	}

	/**
	 * Add the dark module to the given matrix.
	 * 
	 * @param matrix
	 *            The 2-dimensional array representing the QR code
	 */
	public static void addDarkModule(int[][] matrix)
	{
		matrix[8][matrixSize - 8] = B;
	}

	/**
	 * Converts a boolean array to an array containing black and white colors in ARGB value.
	 *
	 * @param tab
	 *          the array containing boolean values
	 * @return	An array containing black and white colors in ARGB value, i.e. as integers
	 */
	public static int [] ConvertBooleanToBW(boolean [] tab) {
		int [] bwArray = new int[tab.length];

		for (int i=0; i<tab.length; i++) {
			bwArray[i] = tab[i] ? B : W;
		}

		return bwArray;
	}

	/**
	 * Add the format information to the matrix
	 *
	 * @param matrix
	 *            The 2-dimensional array representing the QR code to modify
	 * @param mask
	 *            The mask id
	 */
	public static void addFormatInformation(int[][] matrix, int mask) {
		int [] formatInfoArray = ConvertBooleanToBW(QRCodeInfos.getFormatSequence(mask));
		int rowIndex = 5;
		int borderToFormatInfo = finderPatternSize + 1;

		for (int i=0; i<formatInfoArray.length; i++) {
			//TOP LEFT
			if (i <= 5) {
				matrix[i][borderToFormatInfo]=formatInfoArray[i];
			} else if (i==6 || i==7) {
				matrix[i+1][borderToFormatInfo]=formatInfoArray[i];
			} else if (i==8){
				matrix[borderToFormatInfo][7]=formatInfoArray[i];
			} else {
				matrix[borderToFormatInfo][rowIndex]=formatInfoArray[i];
				--rowIndex;
			}

			//BOTTOM
			if(i <= 6){
				matrix[borderToFormatInfo][(matrixSize-1)-i]=formatInfoArray[i];
			//TOP RIGHT
			} else {
				matrix[(matrixSize-borderToFormatInfo-7)+i][borderToFormatInfo]=formatInfoArray[i];
			}
		}
	}


	/*
	 * =======================================================================
	 * ****************************** PART 3 *********************************
	 * =======================================================================
	 */

	/**
	 * Choose the color to use with the given coordinate using the given masking
	 * 
	 * @param col
	 *          x-coordinate
	 * @param row
	 *          y-coordinate
	 * @param dataBit
	 *          : initial color without masking
	 * @param masking
	 * 			The masking for which the data has to be verified
	 * @return the color with the masking
	 */
	public static int maskColor(int col, int row, boolean dataBit, int masking) {
		boolean[] maskList = {
				(col+row) % 2 == 0,
				row % 2 == 0,
				col % 3 == 0,
				(col + row) % 3 == 0,
				((col/2) + (row/3)) % 2 == 0,
				((col*row) % 2) + ((col*row) % 3) == 0,
				(((col*row) % 2) + ((col*row) % 3)) % 2 == 0,
				(((col+row) % 2) + ((col+row) % 3)) % 2 == 0
		};

		if(masking >= 0 && masking <= 7){
			boolean isMasked = maskList[masking];

			if((dataBit && !isMasked) || (!dataBit && isMasked)){
				return B;
			} else {
				return W;
			}
		} else {
			return dataBit ? B : W;
		}
	}

	/**
	 * Add the data bits into the QR code matrix
	 * 
	 * @param matrix
	 *            a 2-dimensional array where the bits needs to be added
	 * @param data
	 *            the data to add
	 */
	public static void addDataInformation(int[][] matrix, boolean[] data, int mask) {
		int runCounter = 1;					// The number of time the data-placing algorithm has
											// already run. This value is incremented each time
											// two bits of data has been placed to fill a row of
											// the "zigzag" column

		int bitIndex = 0;					// The index of the data to place. Incrementing each
											// time a bit has been placed in the matrix
		int rowIndex = matrix.length - 1;	// The initial row index used in the algorithm
		int colIndex = rowIndex;			// The initial column index used in the algorithm

		final int columnWidth = 2;			// The width of a column of data where the "zigzag"
											// algorithm of a QR Code is going to work on

		boolean rowIndexDirection = true; 	// Direction representing the evolution of the row index
											// true: going up | false: going down

		while(colIndex >= 0){
			for(int runCounterPerRow = 0; runCounterPerRow < columnWidth; ++runCounterPerRow){

				int bitColIndex = colIndex-runCounterPerRow;

				if (!bitExists(matrix, bitColIndex, rowIndex)) {
					if(dataLeft(data, bitIndex)){
						matrix[bitColIndex][rowIndex] = maskColor(bitColIndex, rowIndex, data[bitIndex], mask);
					} else {
						matrix[bitColIndex][rowIndex] = maskColor(bitColIndex, rowIndex, false, mask);
					}
					++bitIndex;
				}
			}

			if((rowIndex == 0 || rowIndex == matrix.length-1) && endOfColumn(matrix.length, runCounter)){
				rowIndexDirection = !rowIndexDirection;
				if(colIndex-columnWidth == timingPosition){
					colIndex -= columnWidth+1;
				} else {
					colIndex -= columnWidth;
				}
			}

			if(!endOfColumn(matrix.length, runCounter)){
				if(rowIndexDirection){
					--rowIndex;
				} else {
					++rowIndex;
				}
			}

			++runCounter;
		}
	}

	/**
	 * Checks whether a bit as already been written at a given point in a matrix
	 *
	 * @param matrix
	 * 		The matrix where the verification has to be done
	 * @param col
	 * 		The coordinate "column" to check
	 * @param row
	 * 		The coordinate "row" to check
	 * @return If a bit already exist or not as a boolean
	 */
	public static boolean bitExists(int[][] matrix, int col, int row){
		if(alphaValue(matrix[col][row]) == 0xFF) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gives the alpha value in ARGB for a given bit
	 *
	 * @param bit
	 * 		The bit for which we are looking at the alpha value
	 * @return The alpha value in ARGB format as an integer
	 */
	public static int alphaValue(int bit){
		return ((bit >> 24)&0xFF);
	}

	/**
	 * Checks if the end of column of a given matrix has been reached
	 *
	 * @param matrixSize
	 * 		The size of the matrix we want to verify
	 * @param runCounter
	 * 		The number of time the algorithm filling the matrix
	 * 		has already run.
	 * @return a boolean value representing if we are a the end of the column
	 */
	public static boolean endOfColumn(int matrixSize, int runCounter){
		if(runCounter % matrixSize == 0){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if there is still data to be placed inside the matrix
	 * representing a QR Code
	 *
	 * @param data
	 * 		The boolean array of data to place in the matrix
	 * @param bitIndex
	 * 		The index indicating the amount of data already placed
	 * @return a boolean value representing if there is still data to place
	 */
	public static boolean dataLeft(boolean[] data, int bitIndex){
		if(data.length != 0) {
			if (bitIndex >= data.length) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** BONUS **********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * The mask is computed automatically so that it provides the least penalty
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data) {

		int mask = findBestMasking(version, data);

		return renderQRCodeMatrix(version, data, mask);
	}

	/**
	 * Find the best mask to apply to a QRcode so that the penalty score is
	 * minimized. Compute the penalty score with evaluate
	 * 
	 * @param data
	 * @return the mask number that minimize the penalty
	 */
	public static int findBestMasking(int version, boolean[] data) {
		// TODO BONUS
		return 0;
	}

	/**
	 * Compute the penalty score of a matrix
	 * 
	 * @param matrix:
	 *            the QR code in matrix form
	 * @return the penalty score obtained by the QR code, lower the better
	 */
	public static int evaluate(int[][] matrix) {
		//TODO BONUS
	
		return 0;
	}

}