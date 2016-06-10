
import edu.princeton.cs.algs4.*;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {


    private TreeSet<Point2D> point_set;


    public PointSET()
    {
        point_set = new TreeSet<>();
    }


    public boolean isEmpty()                        // is the set empty?
    {
        return point_set.isEmpty();
    }


    public int size()                               // number of points in the set
    {
        return point_set.size();
    }


    public void insert(Point2D p)                   // add the point to the set
    {                                               // (if it is not already in the set)
        if (p == null) {
            throw new NullPointerException("null point2d");
        }

        point_set.add(p);
    }


    public boolean contains(Point2D p)              // does the set contain point p?
    {
        if (p == null) {
            throw new NullPointerException("null point2d");
        }

        return point_set.contains(p);
    }


    public void draw()                              // draw all points to standard draw
    {
        for (Point2D p : point_set) {
            p.draw();
        }
    }


    public Iterable<Point2D> range(RectHV rect)     // all points that are inside the rectangle
    {
        List<Point2D> ret_list = new LinkedList<>();

        for (Point2D p : point_set) {

            if (rect.contains(p)) {
                ret_list.add(p);
            }
        }

        return ret_list;
    }


    public Point2D nearest(Point2D p)               // a nearest neighbor in the set to point p;
    {                                               // null if the set is empty
        double min_dist = Double.POSITIVE_INFINITY;
        Point2D ret_p = null;

        for (Point2D q : point_set) {

            if (q.distanceTo(p) < min_dist) {
                min_dist = q.distanceTo(p);
                ret_p = q;
            }
        }

        return ret_p;
    }


    public static void main(String[] args)          // unit testing of the methods (optional)
    {
        In file = new In(args[0]);
        double[] coord = file.readAllDoubles();

        PointSET ps = new PointSET();

        for (int i = 0; i < coord.length-1; i += 2) {
            ps.insert(new Point2D(coord[i], coord[i+1]));
        }


    }
}
