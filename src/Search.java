import java.util.ArrayList;
import java.util.LinkedList;

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

    private static class State {

        // Attributes
        Cube            cube;
        int             ply;
        byte            lastFace;
        ArrayList<Move> moveList;

        // Root state
        private State(Cube cube) {
            this.cube     = cube;
            this.ply      = 0;
            this.lastFace = -1;
            this.moveList = new ArrayList<>();
        }

        // Cloning state
        private State(State state, byte faceIndex) {
            this.cube     = new Cube(state.cube);
            this.ply      = state.ply + 1;
            this.lastFace = faceIndex;
            this.moveList = new ArrayList<>(state.moveList);
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
        int maxPly = 4;
        breadthFirst(cube, maxPly);
    }

    // Breadth first style search
    private static void breadthFirst(Cube initialCube, int maxPly) {

        // Start fresh with the scrambled cube
        LinkedList<State> queue = new LinkedList<>();
        queue.add(new State(initialCube));

        while (!queue.isEmpty()) {

            // Isolate the current state
            State state = queue.removeFirst();

            // Check if the cube is solved
            if (state.cube.isSolved()) {
                System.out.println("Cube solved after " + state.ply + " moves.");
                System.out.println(state.moveList);
                return;
            }

            // Terminate the search if max depth is reached
            if (state.ply == maxPly)
                return;

            // Rotate all faces
            for (byte faceIndex = 0; faceIndex < 6; faceIndex++) {

                // Truncate search space based off last rotation
                if (state.ply != 0) {

                    // Avoid rotating same face twice
                    if (faceIndex == state.lastFace)
                        continue;

                    // When rotating opposite faces, set one allowed ordering
                    if ((faceIndex % 3 == state.lastFace % 3) && faceIndex >= 3)
                        continue;
                }

                // Perform all types of rotations
                for (byte rotationType = 1; rotationType <= 3; rotationType++) {

                    // Rotate
                    state.cube.rotate(faceIndex, rotationType);
                    state.moveList.add(new Move(faceIndex, rotationType));

                    // Search new state
                    queue.addLast(new State(state, faceIndex));
                }
            }
        }
    }
}
