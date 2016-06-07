import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {


    private ArrayList<LineSegment> line_list;


    private void naturalSort(ArrayList<Point> arr) {

        /** use insertion sort is vulnerable if existing a long line */
//        for (int i = 0; i < arr.size(); ++i) {
//
//            for (int j = i; j > 0; --j) {
//
//                if (arr.get(j).compareTo(arr.get(j-1)) >= 0) {
//                    break;
//                }
//
//                Point temp = arr.get(j);
//                arr.set(j, arr.get(j-1));
//                arr.set(j-1, temp);
//            }
//        }


        /** instead, use system sort */
//        Collections.sort(arr);


        /**
         * best approach - linear time using reference point
         *  ref point must be on the line
         **/
        Point ref = arr.get(0);
        int[] rel_dist = new int[arr.size()];

        for (int i = 0; i < rel_dist.length; ++i) {
            rel_dist[i] = arr.get(i).compareTo(ref);
        }

        int min_pos = -1, max_pos = -1;
        int min_val = Integer.MAX_VALUE, max_val = Integer.MIN_VALUE;

        for (int i = 0; i< rel_dist.length; ++i) {

            if (rel_dist[i] < min_val) {
                min_val = rel_dist[i];
                min_pos = i;
            }

            if (rel_dist[i] > max_val) {
                max_val = rel_dist[i];
                max_pos = i;
            }
        }

        Point min_point = arr.get(min_pos), max_point = arr.get(max_pos);
        arr.set(0, min_point);              // set arr[0] to be one end point
        arr.set(arr.size()-1, max_point);   // and arr[size-1] to be another

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


    public FastCollinearPoints(Point[] points)      // finds all line segments containing 4 or more points
    {
        examine_input(points);

        line_list = new ArrayList<>();

        Point[] Points = new Point[points.length];
        Point[] copy = new Point[points.length];

        for (int k = 0; k < copy.length; ++k) {
            Points[k] = points[k];
            copy[k] = points[k];
        }

        Arrays.sort(Points);

        for (int i = 0; i < Points.length; ++i) {

            Arrays.sort(copy, Points[i].slopeOrder());

            ArrayList<Point> temp = new ArrayList<>();

            for (int p = 1; p < copy.length; ++p) {

                if (Double.compare(copy[p].slopeTo(copy[0]), copy[p-1].slopeTo(copy[0])) != 0 ||
                        p == copy.length-1) {

                    if (Double.compare(copy[p].slopeTo(copy[0]), copy[p-1].slopeTo(copy[0])) == 0) {
                        temp.add(copy[p]);
                    }

//                    if (p == copy.length-1) {
//                        temp.add(copy[p]);
//                    }

                    if (temp.size() >= 3) {

                        temp.add(copy[0]);
                        naturalSort(temp);

                        if (copy[0].compareTo(temp.get(0)) == 0) {

                            LineSegment line = new LineSegment(temp.get(0), temp.get(temp.size()-1));
                            line_list.add(line);

                        }
                    }

                    temp.clear();

                }

                temp.add(copy[p]);
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
