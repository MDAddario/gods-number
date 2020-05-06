public class Cube {

    // Faces of the cube
    private byte[]  whiteFace;
    private byte[]    redFace;
    private byte[]  greenFace;
    private byte[] yellowFace;
    private byte[]   blueFace;
    private byte[] orangeFace;

    // Rotate four bytes in a clockwise manner
    private static void fourCycleCW(byte[] faceOne,   int indexOne,   byte[] faceTwo,  int indexTwo,
                                    byte[] faceThree, int indexThree, byte[] faceFour, int indexFour) {

        // The four bytes should be listed as when read clockwise!!
        byte temp             = faceOne  [indexOne];
        faceOne  [indexOne]   = faceFour [indexFour];
        faceFour [indexFour]  = faceThree[indexThree];
        faceThree[indexThree] = faceTwo  [indexTwo];
        faceTwo  [indexTwo]   = temp;
    }

    private static void fourCycleCW(byte[] face, int indexOne, int indexTwo, int indexThree, int indexFour) {
        fourCycleCW(face, indexOne, face, indexTwo, face, indexThree, face, indexFour);
    }

    /*
    // Rotate four bytes in a counterclockwise manner
    private static void fourCycleCCW(byte[] faceOne,   int indexOne,   byte[] faceTwo,  int indexTwo,
                                     byte[] faceThree, int indexThree, byte[] faceFour, int indexFour) {

        // Recast as a clockwise rotation
        fourCycleCW(faceOne, indexOne, faceFour, indexFour, faceThree, indexThree, faceTwo, indexTwo);
    }

    private static void fourCycleCCW(byte[] face, int indexOne, int indexTwo, int indexThree, int indexFour) {
        fourCycleCCW(face, indexOne, face, indexTwo, face, indexThree, face, indexFour);
    }
     */

    // Rotating a frontFace clockwise
    private static void CW(byte[] frontFace, byte[] topFace, byte[] rightFace, byte[] botFace, byte[] leftFace) {

        // The four bytes should be listed as when read clockwise!!

        // Rotate the front corners
        fourCycleCW(frontFace, 0, 2, 7, 5);

        // Rotate the front edges
        fourCycleCW(frontFace, 1, 4, 6, 3);

        // Rotate outside 'left' corners
        fourCycleCW(topFace, 7, rightFace, 2, botFace, 5, leftFace, 0);

        // Rotate outside edges
        fourCycleCW(topFace, 4, rightFace, 1, botFace, 6, leftFace, 3);

        // Rotate outside 'right' corners
        fourCycleCW(topFace, 2, rightFace, 0, botFace, 7, leftFace, 5);
    }

    // Well defined rotation notation from speed-cubing community
    void U() {
        CW(whiteFace, redFace, greenFace, orangeFace, blueFace);
    }

    void R() {
        CW(redFace, greenFace, whiteFace, blueFace, yellowFace);
    }

    void F() {
        CW(greenFace, whiteFace, redFace, yellowFace, orangeFace);
    }

    void D() {
        CW(yellowFace, blueFace, orangeFace, greenFace, redFace);
    }

    void L() {
        CW(orangeFace, yellowFace, blueFace, whiteFace, greenFace);
    }

    void B() {
        CW(blueFace, orangeFace, yellowFace, redFace, whiteFace);
    }

    // Default constructor
    Cube() {

        // Setup default face layout
        this.setupInitialFaces();
    }

    // Color definitions
    private static final byte WHITE  = 0;
    private static final byte RED    = 1;
    private static final byte GREEN  = 2;
    private static final byte YELLOW = 3;
    private static final byte BLUE   = 4;
    private static final byte ORANGE = 5;

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
}
