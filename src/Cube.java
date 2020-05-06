class Cube {

    // Faces of the cube
    byte[]  whiteFace;
    byte[]    redFace;
    byte[]  greenFace;
    byte[] yellowFace;
    byte[]   blueFace;
    byte[] orangeFace;

    // Types of rotations (number of -90 degree rotations)
    static final byte ID   = 0;  // Identity
    static final byte CW   = 1;  // Clockwise
    static final byte HALF = 2;  // Half-turn
    static final byte CCW  = 3;  // Counter clockwise

    // Rotate two bytes
    private static void twoCycle(byte[] faceOne, int indexOne, byte[] faceTwo, int indexTwo) {

        byte temp         = faceOne[indexOne];
        faceOne[indexOne] = faceTwo[indexTwo];
        faceTwo[indexTwo] = temp;
    }

    // Rotate four bytes
    private static void fourCycle(byte[] faceOne,   int indexOne,   byte[] faceTwo,  int indexTwo,
                                  byte[] faceThree, int indexThree, byte[] faceFour, int indexFour,
                                  byte rotationType) {

        // The four bytes should be listed as when read clockwise!!
        if (rotationType == ID) {
            return;
        }
        else if (rotationType == CW) {
            byte temp             = faceOne  [indexOne];
            faceOne  [indexOne]   = faceFour [indexFour];
            faceFour [indexFour]  = faceThree[indexThree];
            faceThree[indexThree] = faceTwo  [indexTwo];
            faceTwo  [indexTwo]   = temp;
        }
        else if (rotationType == CCW) {
            fourCycle(faceOne, indexOne, faceFour, indexFour, faceThree, indexThree, faceTwo, indexTwo, CW);
        }
        else if (rotationType == HALF) {
            twoCycle(faceOne, indexOne, faceThree, indexThree);
            twoCycle(faceTwo, indexTwo, faceFour,  indexFour);
        }
        else {
            throw new IllegalArgumentException("Rotation type argument is not valid.");
        }
    }

    static void fourCycle(byte[] face, int one, int two, int three, int four, byte rotationType) {
        fourCycle(face, one, face, two, face, three, face, four, rotationType);
    }

    // Rotating a cube face
    private static void rotateFace(byte[] frontFace, byte[] topFace,  byte[] rightFace,
                                   byte[] botFace,   byte[] leftFace, byte   rotationType) {

        // The four bytes should be listed as when read clockwise!!

        // Rotate the front corners
        fourCycle(frontFace, 0, 2, 7, 5, rotationType);

        // Rotate the front edges
        fourCycle(frontFace, 1, 4, 6, 3, rotationType);

        // Rotate outside 'left' corners
        fourCycle(topFace, 7, rightFace, 2, botFace, 5, leftFace, 0, rotationType);

        // Rotate outside edges
        fourCycle(topFace, 4, rightFace, 1, botFace, 6, leftFace, 3, rotationType);

        // Rotate outside 'right' corners
        fourCycle(topFace, 2, rightFace, 0, botFace, 7, leftFace, 5, rotationType);
    }

    // Well defined rotation notation from speed-cubing community
    void U (byte rotationType) {
        rotateFace(whiteFace, redFace, greenFace, orangeFace, blueFace, rotationType);
    }
    void R (byte rotationType) {
        rotateFace(redFace, greenFace, whiteFace, blueFace, yellowFace, rotationType);
    }
    void F (byte rotationType) {
        rotateFace(greenFace, whiteFace, redFace, yellowFace, orangeFace, rotationType);
    }
    void D (byte rotationType) {
        rotateFace(yellowFace, blueFace, orangeFace, greenFace, redFace, rotationType);
    }
    void L (byte rotationType) {
        rotateFace(orangeFace, yellowFace, blueFace, whiteFace, greenFace, rotationType);
    }
    void B (byte rotationType) {
        rotateFace(blueFace, orangeFace, yellowFace, redFace, whiteFace, rotationType);
    }

    // Default constructor
    Cube() {

        // Setup default face layout
        this.setupInitialFaces();
    }

    // Color definitions
    static final byte WHITE  = 0;
    static final byte RED    = 1;
    static final byte GREEN  = 2;
    static final byte YELLOW = 3;
    static final byte BLUE   = 4;
    static final byte ORANGE = 5;

    // Setup initial faces
    private void setupInitialFaces() {

        // Allocate memory
        this. whiteFace = new byte[8];
        this.   redFace = new byte[8];
        this. greenFace = new byte[8];
        this.yellowFace = new byte[8];
        this.  blueFace = new byte[8];
        this.orangeFace = new byte[8];

        // Paint the faces
        for (int i = 0; i < 8; i++) {
            this. whiteFace[i] = WHITE;
            this.   redFace[i] = RED;
            this. greenFace[i] = GREEN;
            this.yellowFace[i] = YELLOW;
            this.  blueFace[i] = BLUE;
            this.orangeFace[i] = ORANGE;
        }
    }

    // See if cube is in the solved state
    boolean isSolved() {

        // Paint the faces
        for (int i = 0; i < 8; i++)
            if (    this. whiteFace[i] != WHITE  || this. redFace[i] != RED  || this. greenFace[i] != GREEN ||
                    this.yellowFace[i] != YELLOW || this.blueFace[i] != BLUE || this.orangeFace[i] != ORANGE)
                return false;
        return true;
    }

    // Method for imageIO
    void saveImage() {
        ImageProcessing.saveCubeImage(this);
    }
}
