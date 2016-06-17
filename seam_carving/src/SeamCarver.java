import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver {

    private int height;
    private int width;
    private  Color[][] color;
    private double[][] energy;  // default non-transposed
    private boolean transpose;  // indicate if energy is transposed
                                // energy and color must be coherent

    private static class Topological {  // direction top --> bot

        private boolean[][] visited;
        private int H;
        private int W;
        private Stack<Integer> revPost = new Stack<>();

        public Topological(int H, int W) {

            if (H <= 0 || W <= 0) {
                throw new IllegalArgumentException("H or W can't <= 0");
            }

            this.H = H;
            this.W = W;
            visited = new boolean[this.H][this.W];

            for (int col = 0; col < W; ++col) {
                dfs(0, col);
            }
        }

        private void dfs(int y, int x) {
            // post order: push after all descendants are visited

            if (visited[y][x]) return;

            visited[y][x] = true;

            if (y == H-1) {
                revPost.push(y*W + x);
                return;
            }

            if (x > 0) {
                dfs(y+1, x-1);
            }

            dfs(y+1, x);

            if (x < W-1) {
                dfs(y+1, x+1);
            }

            revPost.push(y*W + x);
        }

        public int[] calcCoord(int index) {return new int[] {index/W, index%W};}

        public Iterable<Integer> order() {return revPost;}
    }

    private static class SPT_DAG { // direction: top --> bot

        private double[][] costOn;
        private double[][] costTo;
        private    int[][] edgeTo;
        private    int H;
        private    int W;
        private Topological topological;

        public SPT_DAG(double[][] energy) {

            if (energy == null) {
                throw new IllegalArgumentException("null matrix in SPT_DAG");
            }

            H = energy.length;
            W = energy[0].length;

            costOn = new double[H][W];
            costTo = new double[H][W];
            edgeTo = new    int[H][W];

            for (int y = 0; y < H; ++y) {
                for (int x = 0; x < W; ++x) {
                    costOn[y][x] = energy[y][x];

                    if (y == 0) {   // init first row
                        costTo[y][x] = energy[y][x];
                    }
                    else {
                        costTo[y][x] = Double.POSITIVE_INFINITY;
                    }

                    edgeTo[y][x] = -1;
                }
            }

            topological = new Topological(H, W);

            for (Integer i : topological.order()) {

                int[] coord = topological.calcCoord(i);
                relaxVertex(coord[0], coord[1]);
            }

        }

        private void relaxVertex(int y, int x) {
            // y: height, x: width

            if (y == H-1) {return;}

            if (x > 0) {relaxEdge(y, x, y+1, x-1);}

            relaxEdge(y, x, y+1, x);

            if (x < W-1) {relaxEdge(y, x, y+1, x+1);}
        }


        private void relaxEdge(int y1, int x1, int y2, int x2) {

            if (costTo[y2][x2] > costTo[y1][x1] + costOn[y2][x2]) {
                costTo[y2][x2] = costTo[y1][x1] + costOn[y2][x2];
                edgeTo[y2][x2] = calc_index(y1, x1);
            }
        }

        private int calc_index(int y, int x) {return y*W + x;}

        private int[] calc_coord(int index) {return new int[] {index/W, index%W};}

        public double costTo(int y, int x) {

            if (y < 0 || y > H-1 || x < 0 || x > W-1) {
                throw new IndexOutOfBoundsException("idx out bound - SPT_DAG");
            }

            return costTo[y][x];
        }

        public Iterable<Integer> shortestPath() {
            // return the col number on each row only.

            double min_cost = Double.POSITIVE_INFINITY;
            int col_num = -1;

            for (int col = 0; col < W; ++col) {

                if (costTo(H-1, col) < min_cost) {
                    min_cost = costTo(H-1, col);
                    col_num = col;
                }
            }

            Stack<Integer> ret = new Stack<>();

            int dad = edgeTo[H-1][col_num];

            while(dad != -1) {  // edgeTo of the top row is -1

                ret.push(col_num);
                int[] coord = calc_coord(dad);
                dad = edgeTo[coord[0]][coord[1]];
                col_num = coord[1];
            }

            ret.push(col_num);

            return ret;
        }
    }

    public SeamCarver(Picture picture) {              // create a seam carver object based on the given picture

        if (picture == null) {
            throw new NullPointerException("input pic is null");
        }

        height = picture.height();
        width  = picture.width();
        transpose = false;

        color  = new Color[height][width];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                color[y][x] = picture.get(x, y);
            }
        }

        energy = new double[height][width];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                energy[y][x] = calcEnergy(y, x);
            }
        }
    }

    public Picture picture() {                        // current picture

        if (transpose) {transpose();}

        Picture picture = new Picture(width, height);

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                picture.set(x, y, color[y][x]);
            }
        }

        return picture;
    }

    private double calcEnergy(int y, int x) {

        if (x == 0 || x == width-1 || y == 0 || y == height-1) {
            return 1000.;
        }

        Color lft = color[y][x-1], rht = color[y][x+1];
        Color top = color[y-1][x], bot = color[y+1][x];

        int Rx = rht.getRed()   -  lft.getRed()  ,  Ry = bot.getRed()   -  top.getRed()  ,
            Gx = rht.getGreen() -  lft.getGreen(),  Gy = bot.getGreen() -  top.getGreen(),
            Bx = rht.getBlue()  -  lft.getBlue() ,  By = bot.getBlue()  -  top.getBlue() ;

        return Math.sqrt((double)Rx*Rx + Gx*Gx + Bx*Bx + Ry*Ry + Gy*Gy + By*By);
    }

    public     int width() {                          // width of current picture
        return width;
    }

    public     int height() {                         // height of current picture
        return height;
    }

    public  double energy(int x, int y) {             // energy of pixel at column x and row y

        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException("pixel out of bound");
        }

        if (!transpose) return energy[y][x];
        else            return energy[x][y];
    }

    public   int[] findHorizontalSeam() {             // sequence of indices for horizontal seam

        if (!transpose) {transpose();}

        return findSeam_vertical();
    }

    public   int[] findVerticalSeam() {               // sequence of indices for vertical seam

        if (transpose) {transpose();}

        return findSeam_vertical();
    }

    private  int[] findSeam_vertical() {
        // always convert to find a vertical seam

        int[]   ret = new int[height];
        SPT_DAG spt = new SPT_DAG(energy);

        int k = 0;
        for (Integer i : spt.shortestPath()) {
            ret[k++] = i;
        }

        return ret;
    }

    private   void transpose() {

        Color[][]   color_t = new Color[width][height];
        double[][] energy_t = new double[width][height];

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {

                color_t[i][j] = color[j][i];
                energy_t[i][j] = energy[j][i];
            }
        }

        energy    = energy_t;
        color     = color_t;

        transpose = !transpose;

        int temp  = height;
        height    = width;
        width     = temp;
    }

    private   void checkException(int[] seam) {

        if (width <= 1) {
            throw new IllegalArgumentException("pic too short");
        }

        if (seam == null) {
            throw new NullPointerException("seam is null");
        }

        if (seam.length != height()) {
            throw new IllegalArgumentException("seam length not match");
        }

        for (int i = 0; i < seam.length; ++i) {

            if (seam[i] < 0 || seam[i] > width-1) {
                throw new IllegalArgumentException("seam out of bound");
            }

            if (i > 0 && Math.abs(seam[i] - seam[i-1]) > 1) {
                throw new IllegalArgumentException("seam not continuous");
            }
        }
    }

    public    void removeHorizontalSeam(int[] seam) { // remove horizontal seam from current picture

        if (!transpose) {transpose();}

        checkException(seam);
        removeSeam_vertical(seam);
    }

    public    void removeVerticalSeam(int[] seam) {   // remove vertical seam from current picture

        if (transpose) {transpose();}

        checkException(seam);
        removeSeam_vertical(seam);
    }

    private    void removeSeam_vertical(int[] seam) {

        Color[][]   color_ = new Color[height][width-1];
        double[][] energy_ = new double[height][width-1];

        for (int y = 0; y < height; ++y) {

            System.arraycopy(color[y], 0, color_[y], 0, seam[y]);   // copy left half
            System.arraycopy(energy[y], 0, energy_[y], 0, seam[y]);
            System.arraycopy(color[y], seam[y]+1, color_[y], seam[y], width-1-seam[y]);
            System.arraycopy(energy[y], seam[y]+1, energy_[y], seam[y], width-1-seam[y]);
        }

        color = color_;
        energy = energy_;
        --width;

        // update energy on side of the seam
        for (int i = 0; i < seam.length; ++i) {

            if (seam[i] > 0) {
                energy[i][seam[i]-1] = calcEnergy(i, seam[i]-1);
            }

            if (seam[i] < width-1) {
                energy[i][seam[i]+1] = calcEnergy(i, seam[i]+1);
            }
        }
    }


    // remember to comment this section
//    public static void main(String[] args) {
//
//        SeamCarver sc = new SeamCarver(new Picture(args[0]));
//        SPT_DAG spt = new SPT_DAG(sc.energy);
//
//        int[] arr = sc.findHorizontalSeam();
//        for (int i = 0; i < arr.length; ++i) {
//            StdOut.print(arr[i] + " ");
//        }
//    }

}
