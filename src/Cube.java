import java.util.Arrays;

class Cube {

    // Faces of the cube
    byte[]  whiteFace;
    byte[]    redFace;
    byte[]  greenFace;
    byte[] yellowFace;
    byte[] orangeFace;
    byte[]   blueFace;

    // Color definitions (opposite faces are equal modulo 3)
    static final byte WHITE  = 0;
    static final byte RED    = 1;
    static final byte GREEN  = 2;
    static final byte YELLOW = 3;
    static final byte ORANGE = 4;
    static final byte BLUE   = 5;

    // Setup initial faces
    private void setupInitialFaces() {

        // Allocate memory
        this. whiteFace = new byte[8];
        this.   redFace = new byte[8];
        this. greenFace = new byte[8];
        this.yellowFace = new byte[8];
        this.orangeFace = new byte[8];
        this.  blueFace = new byte[8];

        // Paint the faces
        for (int i = 0; i < 8; i++) {
            this. whiteFace[i] = WHITE;
            this.   redFace[i] = RED;
            this. greenFace[i] = GREEN;
            this.yellowFace[i] = YELLOW;
            this.orangeFace[i] = ORANGE;
            this.  blueFace[i] = BLUE;
        }
    }

    // See if cube is in the solved state
    boolean isSolved() {

        // Paint the faces
        for (int i = 0; i < 8; i++)
            if (    this. whiteFace[i] != WHITE  || this.   redFace[i] != RED    || this.greenFace[i] != GREEN ||
                    this.yellowFace[i] != YELLOW || this.orangeFace[i] != ORANGE || this. blueFace[i] != BLUE)
                return false;
        return true;
    }

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

    // Ensure rotation type is valid
    private static boolean isValidRotation( byte rotationType ) { return 0 <= rotationType && rotationType <= 3; }

    // Rotate four bytes
    private static void fourCycle(byte[] faceOne,   int indexOne,   byte[] faceTwo,  int indexTwo,
                                  byte[] faceThree, int indexThree, byte[] faceFour, int indexFour,
                                  byte rotationType) {

        if (!isValidRotation(rotationType))
            throw new IllegalArgumentException("Rotation type argument is not valid.");

        // The four bytes should be listed as when read clockwise!!
        if (rotationType == CW) {
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

    // Create a rotation class for each of the faces
    private abstract class Rotation {
        abstract void rotate(byte rotationType);
    }
    private class WhiteRotation extends Rotation{
        void rotate(byte rotationType) {
            rotateFace(whiteFace, redFace, greenFace, orangeFace, blueFace, rotationType);
        }
    }
    private class RedRotation extends Rotation{
        void rotate(byte rotationType) {
            rotateFace(redFace, greenFace, whiteFace, blueFace, yellowFace, rotationType);
        }
    }
    private class GreenRotation extends Rotation{
        void rotate(byte rotationType) {
            rotateFace(greenFace, whiteFace, redFace, yellowFace, orangeFace, rotationType);
        }
    }
    private class YellowRotation extends Rotation{
        void rotate(byte rotationType) {
            rotateFace(yellowFace, blueFace, orangeFace, greenFace, redFace, rotationType);
        }
    }
    private class OrangeRotation extends Rotation{
        void rotate(byte rotationType) {
            rotateFace(orangeFace, yellowFace, blueFace, whiteFace, greenFace, rotationType);
        }
    }
    private class BlueRotation extends Rotation{
        void rotate(byte rotationType) {
            rotateFace(blueFace, orangeFace, yellowFace, redFace, whiteFace, rotationType);
        }
    }

    // Array of rotation instances
    private Rotation[] rotations;

    // Pair up each rotation with the associated face number
    private void setupRotations() {

        // Allocate memory
        this.rotations = new Rotation[6];

        // Place the rotations
        this.rotations[WHITE]  = new  WhiteRotation();
        this.rotations[RED]    = new    RedRotation();
        this.rotations[GREEN]  = new  GreenRotation();
        this.rotations[YELLOW] = new YellowRotation();
        this.rotations[ORANGE] = new OrangeRotation();
        this.rotations[BLUE]   = new   BlueRotation();
    }

    // Check to see if the face index is valid
    private static boolean isValidFace(byte faceIndex) { return 0 <= faceIndex && faceIndex < 6; }

    // Perform any possible rotation
    void rotate(byte faceIndex, byte rotationType) {

        // Ensure face index is valid
        if (!isValidFace(faceIndex))
            throw new IllegalArgumentException("Face index argument is illegal.");

        // Perform the rotation
        this.rotations[faceIndex].rotate(rotationType);
    }

    // Revert any rotation
    void undoRotate(byte faceIndex, byte rotationType) {

        // Ensure face index is valid
        if (!isValidFace(faceIndex))
            throw new IllegalArgumentException("Face index argument is illegal.");

        // Perform the inverse rotation
        if (rotationType == CW)
            this.rotations[faceIndex].rotate(CCW);
        else if (rotationType == HALF)
            this.rotations[faceIndex].rotate(HALF);
        else if (rotationType == CCW)
            this.rotations[faceIndex].rotate(CW);
        else
            throw new IllegalArgumentException("Rotation type argument is not valid.");
    }

    // Well defined rotation names in the speed-cubing community
    void U(byte rotationType) { this.rotate(WHITE,  rotationType); }
    void R(byte rotationType) { this.rotate(RED,    rotationType); }
    void F(byte rotationType) { this.rotate(GREEN,  rotationType); }
    void D(byte rotationType) { this.rotate(YELLOW, rotationType); }
    void L(byte rotationType) { this.rotate(ORANGE, rotationType); }
    void B(byte rotationType) { this.rotate(BLUE,   rotationType); }

    // Default constructor
    Cube() {

        // Setup default face layout
        this.setupInitialFaces();

        // Setup the rotations
        this.setupRotations();
    }

    // Method for imageIO
    void saveImage() {
        ImageProcessing.saveCubeImage(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cube)
            return (Arrays.equals(this. whiteFace, ((Cube) obj).whiteFace)  &&
                    Arrays.equals(this.   redFace, ((Cube) obj).redFace)    &&
                    Arrays.equals(this. greenFace, ((Cube) obj).greenFace)  &&
                    Arrays.equals(this.yellowFace, ((Cube) obj).yellowFace) &&
                    Arrays.equals(this.orangeFace, ((Cube) obj).orangeFace) &&
                    Arrays.equals(this.  blueFace, ((Cube) obj).blueFace));
        return false;
    }

    private int faceSum(byte faceIndex) {

        byte[][] array = {whiteFace, redFace, greenFace, yellowFace, orangeFace, blueFace};

        int sum = 0;
        for (int i = 0; i < 8; i++)
            sum += array[faceIndex][i];
        return sum;
    }

    @Override
    public int hashCode() {

        int sum = 0;
        for (byte i = 0; i < 6; i++)
            sum = 31 * sum + faceSum(i);
        return sum;
    }
}
