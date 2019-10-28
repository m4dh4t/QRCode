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
	// TODO add constant for White pixel
	// TODO add constant for Black pixel
	public static final int W = 0xFF_FF_FF_FF;
	public static final int B = 0xFF_00_00_00;
	public static final int [][] motif = ConstructionMotif();
	public static final int [][] AlignementPattern = ConstructionAlignementPattern();
	public static final int [][] Ligne = ConstructionBande(8,1);
	public static final int [][] Colonne = ConstructionBande(1,8);
	// ...  MYDEBUGCOLOR = ...;
	// feel free to add your own colors for debugging purposes

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
	 * Create a matrix (2D array) ready to accept data for a given version and mask
	 * 
	 * @param version
	 *            the version number of QR code (has to be between 1 and 4 included)
	 * @param mask
	 *            the mask id to use to mask the data modules. Has to be between 0
	 *            and 7 included to have a valid matrix. If the mask id is not
	 *            valid, the modules would not be not masked later on, hence the
	 *            QRcode would not be valid
	 * @return the qrcode with the patterns and format information modules
	 *         initialized. The modules where the data should be remain empty.
	 */
	public static int[][] constructMatrix(int version, int mask) {
		int [][] matrix = initializeMatrix(version);
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
		int taille = QRCodeInfos.getMatrixSize(version);
		int [][] matrix = new int [taille][taille];
		return matrix;
	}

	/**
	 * Add all finder patterns to the given matrix with a border of White modules.
	 * 
	 * @param matrix
	 *            the 2D array to modify: where to add the patterns
	 */
	public static int [][] ConstructionMotif() {
		int [][] motif = new int [8][8];
		for (int i=1 ; i<=7 ; i++) {
			for (int j=1 ; j<=7 ; j++) {
				motif [i][j] = B;
			}
		}
		for (int i=2 ; i<=6; i++) {
			for (int j=2 ; j<=6; j++) {
				motif [i][j] = W;
			}
		}
		for (int i=3 ; i<=5; i++) {
			for (int j=3 ; j<=5; j++) {
				motif [i][j] = B;
			}
		}
		return motif;
	}
	
	public static int [][] ConstructionBande(int a, int b) {
		int [][] Bande = new int [a+1][b+1];
		for (int i=1 ; i<=a; i++) {
			for (int j=1 ; j<=b; j++) {
				Bande [i][j] = W;
			}
		}
		return Bande;
	}
	
	public static int [][] PatternPlacer(int[][] matrix, int [][] pattern, int x, int y, int a, int b) {
		for (int i=1; i< a ; i++) {
			for (int j=1; j< b ; j++) {
			matrix[x+i-1][y+j-1]=pattern[i][j];
			}
		}
		return matrix;
	}
		
	
	public static void addFinderPatterns(int[][] matrix) {
		int taille = matrix.length;
		matrix=PatternPlacer(matrix, motif, 0,0,8,8);
		matrix=PatternPlacer(matrix, motif, 0,(taille - 7),8,8);
		matrix=PatternPlacer(matrix, motif, (taille - 7),0,8,8);
		matrix=PatternPlacer(matrix, Colonne, (taille - 8),0,2,9);
		matrix=PatternPlacer(matrix, Colonne, 7,0,2,9);
		matrix=PatternPlacer(matrix, Colonne, 7,(taille - 8),2,9);
		matrix=PatternPlacer(matrix, Ligne, 0,7,9,2);
		matrix=PatternPlacer(matrix, Ligne, (taille - 8),7,9,2);
		matrix=PatternPlacer(matrix, Ligne, 0,(taille - 8),9,2);
	}

	/**
	 * Add the alignment pattern if needed, does nothing for version 1
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 * @param version
	 *            the version number of the QR code needs to be between 1 and 4
	 *            included
	 */
		
	public static int [][] ConstructionAlignementPattern() {
		int [][] motif = new int [6][6];
		for (int i=1 ; i<=5 ; i++) {
			for (int j=1 ; j<=5 ; j++) {
				motif [i][j] = B;
			}
		}
		for (int i=2 ; i<=4; i++) {
			for (int j=2 ; j<=4; j++) {
				motif [i][j] = W;
			}
		}
				motif [3][3] = B;
		return motif;
	}
	
	public static void addAlignmentPatterns(int[][] matrix, int version) {
		if (version>1) {
			int taille = matrix.length;
			matrix=PatternPlacer(matrix, AlignementPattern, (taille - 9),(taille - 9),6,6);
			}
	}
	

	/**
	 * Add the timings patterns
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 */
	public static void addTimingPatterns(int[][] matrix) {
		for (int i=8; i<= (matrix.length - 9); i++) {
			if (i % 2 == 0) {
				matrix[6][i] = B;
			} 
			else {
				matrix[6][i] = W;
			}
		}
		for (int i=8; i<= (matrix.length - 9); i++) {
			if (i % 2 == 0) {
				matrix[i][6] = B;
				} 
				else {
					matrix[i][6] = W;
				}
		}
	}

	/**
	 * Add the dark module to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code
	 */
	public static void addDarkModule(int[][] matrix) {
		matrix[8][matrix.length - 8] = B;
	}

	/**
	 * Add the format information to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code to modify
	 * @param mask
	 *            the mask id
	 */	
	
	public static int [] ConvertBooleanToBW(boolean [] tab) {
		int [] Sequence = new int [tab.length] ;
		for (int i=0; i<tab.length ; i++) {
			if (tab [i] == true) {
				Sequence [i] =B; 
			} else {
				Sequence [i] =W;
			}
		}
		return Sequence;
	}
	
	public static void addFormatInformation(int[][] matrix, int mask) {
		int [] Sequence = ConvertBooleanToBW(QRCodeInfos.getFormatSequence(mask));
		int j=7;
		for (int i=0; i<16; i++) {
			if (i>=0 && i<=5) {
			matrix[i][8]=Sequence[i];
			}
			if (i>=6 && i<=7) {
				matrix[i+1][8]=Sequence[i];
			}
			if (i>=8 && i<=15) {
				matrix[8][j]=Sequence[i-1];
				j=j-1;
			}
		}
		int l= matrix.length - 1;
		int m= matrix.length - 1;
		for (int k=0; k<16; k++) {
			if (k>=0 && k<7) {
			matrix[8][l-k]=Sequence[k];
			}
			if (k>=7 && k<15) {
				matrix[m-7][8]=Sequence[k];
				m=m+1;
				}
		}
	}
		

	/*
	 * =======================================================================
	 * ****************************** PART 3 *********************************
	 * =======================================================================
	 */

	/**
	 * Choose the color to use with the given coordinate using the masking 0
	 * 
	 * @param col
	 *            x-coordinate
	 * @param row
	 *            y-coordinate
	 * @param color
	 *            : initial color without masking
	 * @return the color with the masking
	 */
	public static int maskColor(int col, int row, boolean dataBit, int masking) {
		// TODO Implementer
		return 0;
	}

	/**
	 * Add the data bits into the QR code matrix
	 * 
	 * @param matrix
	 *            a 2-dimensionnal array where the bits needs to be added
	 * @param data
	 *            the data to add
	 */
	public static void addDataInformation(int[][] matrix, boolean[] data, int mask) {
		// TODO Implementer

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
