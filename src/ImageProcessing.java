import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

class ImageProcessing {

    // Default filenames
    private static final String DEFAULT_OUTPUT = "assets/cube_output.png";
    private static final String DEFAULT_FORMAT = "png";

    // Preset pixel colors
    private static final int[]  WHITE_PIXEL = {255, 255, 255};
    private static final int[]    RED_PIXEL = {255,   0,   0};
    private static final int[]  GREEN_PIXEL = {  0, 255,   0};
    private static final int[] YELLOW_PIXEL = {255, 255,   0};
    private static final int[]   BLUE_PIXEL = {  0,   0, 255};
    private static final int[] ORANGE_PIXEL = {255, 165,   0};
    private static final int[]    BGD_PIXEL = {100, 100, 100};

    // Pick a pixel color from the tile color
    private static int[] pixelSelector(byte color) {

        if      (color == Cube.WHITE)  return  WHITE_PIXEL;
        else if (color == Cube.RED)    return    RED_PIXEL;
        else if (color == Cube.GREEN)  return  GREEN_PIXEL;
        else if (color == Cube.YELLOW) return YELLOW_PIXEL;
        else if (color == Cube.BLUE)   return   BLUE_PIXEL;
        else if (color == Cube.ORANGE) return ORANGE_PIXEL;
        else throw new IllegalArgumentException("Pixel specified has no registered color.");
    }

    // Save cube face
    private static void paintCubeFace(WritableRaster raster, byte[] face, byte rotationType, int iStart, int jStart) {

        // Create a copy of the face
        face = face.clone();

        // Rotate the face
        Cube.fourCycle(face, 0, 2, 7, 5, rotationType);
        Cube.fourCycle(face, 1, 4, 6, 3, rotationType);

        // Save the pixels to the image
        raster.setPixel(jStart    , iStart    , pixelSelector(face[0]));
        raster.setPixel(jStart + 1, iStart    , pixelSelector(face[1]));
        raster.setPixel(jStart + 2, iStart    , pixelSelector(face[2]));
        raster.setPixel(jStart    , iStart + 1, pixelSelector(face[3]));
        raster.setPixel(jStart + 2, iStart + 1, pixelSelector(face[4]));
        raster.setPixel(jStart    , iStart + 2, pixelSelector(face[5]));
        raster.setPixel(jStart + 1, iStart + 2, pixelSelector(face[6]));
        raster.setPixel(jStart + 2, iStart + 2, pixelSelector(face[7]));
    }

    // Save cube to a rendered image
    static void saveCubeImage(Cube cube) {

        // Image dimensions
        int width  = 3 * 4;
        int height = 3 * 3;

        // Create new image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();

        // Set the background
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                raster.setPixel(j, i, BGD_PIXEL);

        // Save each face
        paintCubeFace(raster, cube. whiteFace, Cube.CW,   3, 3);
        paintCubeFace(raster, cube.   redFace, Cube.HALF, 3, 6);
        paintCubeFace(raster, cube. greenFace, Cube.ID,   6, 3);
        paintCubeFace(raster, cube.yellowFace, Cube.ID,   3, 9);
        paintCubeFace(raster, cube.  blueFace, Cube.CCW,  0, 3);
        paintCubeFace(raster, cube.orangeFace, Cube.CCW,  3, 0);

        // Color the centers
        raster.setPixel( 4, 4,  WHITE_PIXEL);
        raster.setPixel( 7, 4,    RED_PIXEL);
        raster.setPixel( 4, 7,  GREEN_PIXEL);
        raster.setPixel(10, 4, YELLOW_PIXEL);
        raster.setPixel( 4, 1,   BLUE_PIXEL);
        raster.setPixel( 1, 4, ORANGE_PIXEL);

        // Write to file
        try {
            ImageIO.write(image, DEFAULT_FORMAT, new File(DEFAULT_OUTPUT));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Test the image saver
    public static void main(String[] args) {

        // Make new cube
        Cube cube = new Cube();

        // Perform validation scramble
        cube.U(Cube.CCW);
        cube.D(Cube.CW);
        cube.U(Cube.CCW);
        cube.F(Cube.CW);
        cube.R(Cube.CCW);
        cube.U(Cube.CCW);
        cube.B(Cube.CCW);
        cube.R(Cube.CCW);
        cube.F(Cube.CCW);
        cube.B(Cube.HALF);
        cube.R(Cube.CW);
        cube.D(Cube.CW);
        cube.F(Cube.HALF);
        cube.B(Cube.CCW);
        cube.L(Cube.CW);
        cube.F(Cube.HALF);
        cube.D(Cube.HALF);
        cube.R(Cube.CW);

        // Save the cube
        cube.saveImage();
    }
}
