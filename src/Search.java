import java.util.ArrayList;
import java.util.HashMap;

class Search {

    private static class Move {

        private byte faceIndex;
        private byte rotationType;

        private Move(byte faceIndex, byte rotationType) {
            this.faceIndex    = faceIndex;
            this.rotationType = rotationType;
        }

        @Override
        public String toString() {

            StringBuilder output = new StringBuilder();

            // Format face
            if      (this.faceIndex == Cube.WHITE)  output.append("U");
            else if (this.faceIndex == Cube.RED)    output.append("R");
            else if (this.faceIndex == Cube.GREEN)  output.append("F");
            else if (this.faceIndex == Cube.YELLOW) output.append("D");
            else if (this.faceIndex == Cube.ORANGE) output.append("L");
            else if (this.faceIndex == Cube.BLUE)   output.append("B");

            // Format rotation type
            if      (this.rotationType == Cube.HALF) output.append("2");
            else if (this.rotationType == Cube.CCW)  output.append("'");

            return output.toString();
        }
    }

    public static void main(String[] args) {

        // Create a fresh cube
        Cube cube = new Cube();

        // Give it the "sexy move"
        cube.R(Cube.CW);
        cube.U(Cube.CW);
        cube.R(Cube.CCW);
        cube.U(Cube.CCW);

        // Search for the solution
        int maxPly = 8;
        Search search = new Search(maxPly);

        // Time the runtime
        long startTime = System.currentTimeMillis();
        search.bruteForce(cube);
        long endTime = System.currentTimeMillis();

        System.out.println("Runtime: " + (int)((endTime - startTime) / 1000) + " seconds.");
    }

    // Search engine attributes
    private int                    maxPly;
    private HashMap<Cube, Boolean> hash;

    // Constructor
    Search(int maxPly) {
        this.maxPly = maxPly;
        this.hash   = new HashMap<>();
    }

    // Begin the brute force search
    private void bruteForce(Cube cube) {
        bruteForce(cube, 0, (byte)-1, new ArrayList<>(this.maxPly));
    }

    // Brute force search all possible scrambles
    private void bruteForce(Cube cube, int ply, byte lastFace, ArrayList<Move> moveList) {

        // Check if the cube is solved
        if (cube.isSolved()) {
            System.out.println("Cube solved after " + ply + " moves.");
            System.out.println(moveList);
            return;
        }

        // Terminate the search
        if (ply == this.maxPly)
            return;

        // Ignore the node if already visited the cube
        if (this.hash.put(cube, true) != null)
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
                moveList.add(new Move(faceIndex, rotationType));

                // Search new state
                bruteForce(cube, ply + 1, faceIndex, moveList);

                // Undo rotation
                cube.undoRotate(faceIndex, rotationType);
                moveList.remove(moveList.size() - 1);
            }
        }
    }
}
