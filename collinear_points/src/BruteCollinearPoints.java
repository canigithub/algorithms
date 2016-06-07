
import java.util.ArrayList;

public class BruteCollinearPoints {

    private ArrayList<LineSegment> line_list;

    private LineSegment findEndpoint(Point[] points, int[] index) {

        Point[] copy = new Point[index.length];

        for (int i = 0; i < copy.length; ++i) {
            copy[i] = points[index[i]];
        }

        for (int i = 0; i < copy.length; ++i) {
            for (int j = i; j > 0; --j) {

                if (copy[j].compareTo(copy[j-1]) < 0) {
                    Point temp = copy[j];
                    copy[j] = copy[j-1];
                    copy[j-1] = temp;
                } else {
                    break;
                }
            }
        }

        return new LineSegment(copy[0], copy[copy.length-1]);
    }


    private void examine_input(Point[] points) {

        if (points == null) {
            throw new NullPointerException("points is null.");
        }

        for (int i = 0; i < points.length; ++i) {

            if (points[i] == null) {
                throw new NullPointerException("point is null.");
            }

            for (int j = i+1; j < points.length; ++j) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("contains repeated points.");
                }
            }
        }
    }


    public BruteCollinearPoints(Point[] points)     // finds all line segments containing 4 points
    {
        examine_input(points);

        line_list = new ArrayList<>();

        for (int i = 0; i < points.length; ++i) {
            for (int j = i+1; j < points.length; ++j) {
                for (int k = j+1; k < points.length; ++k) {
                    for (int l = k+1; l < points.length; ++l) {
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]) &&
                                points[i].slopeTo(points[j]) == points[i].slopeTo(points[l])) {
                            int[] index = new int[] {i, j, k, l};
                            LineSegment ls = findEndpoint(points, index);
                            line_list.add(ls);
                        }
                    }
                }
            }
        }
    }


    public int numberOfSegments()                   // the number of line segments
    {
        return line_list.size();
    }


    public LineSegment[] segments()                 // the line segments
    {
        LineSegment[] ret = new LineSegment[numberOfSegments()];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = line_list.get(i);
        }
        return ret;
    }
}
