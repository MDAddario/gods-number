public class Search {

    public static void main(String[] args) {

        // Create a fresh cube
        Cube cube = new Cube();

        // Give it the "sexy move"
        cube.R(Cube.CW);
        cube.U(Cube.CW);
        cube.R(Cube.CCW);
        cube.U(Cube.CCW);

        // Search for the solution
        int maxPly = 4;
        startSearch(cube, maxPly);
    }

    // Wrapper for brute force search
    static void startSearch(Cube cube, int maxPly) {
        bruteForce(cube, 0, maxPly, (byte)-1);
    }

    // Brute force search all possible scrambles
    private static void bruteForce(Cube cube, int ply, int maxPly, byte lastFace) {

        // Check if the cube is solved
        if (cube.isSolved()) {
            System.out.println("Cube solved after " + ply + " moves.\n");
            return;
        }

        // Terminate the search
        if (ply == maxPly)
            return;

        // Rotate all faces
        for (byte faceIndex = 0; faceIndex < 6; faceIndex++) {

            // Truncate search space based off last rotation
            if (ply != 0) {

                // Avoid rotating same face twice
                if (faceIndex == lastFace)
                    continue;

                // When rotating opposite faces, set one allowed ordering
                if ((faceIndex % 3 == lastFace % 3) && faceIndex >= 3)
                    continue;
            }

            // Perform all types of rotations
            for (byte rotationType = 1; rotationType <= 3; rotationType++) {

                // Rotate
                cube.rotate(faceIndex, rotationType);

                // Search new state
                bruteForce(cube, ply + 1, maxPly, faceIndex);

                // Undo rotation
                cube.undoRotate(faceIndex, rotationType);
            }
        }
    }
}
