
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

public class KdTree {


    private int size;
    private KDT_Node root;


    private static class KDT_Node {

        int      split_feat;
        Point2D  point;
        KDT_Node left = null;
        KDT_Node right = null;

        KDT_Node(Point2D p, int f) {

            point = p;
            split_feat = f;
        }
    }


    public KdTree()
    {
        size = 0;
        root = null;
    }


    public boolean isEmpty()                        // is the set empty?
    {
        return size == 0;
    }


    public int size()                               // number of points in the set
    {
        return size;
    }


    public void insert(Point2D p)                   // add the point to the set
    {                                               // (if it is not already in the set)
        if (p == null) {
            throw new NullPointerException("null point2d");
        }

        KDT_Node new_node = new KDT_Node(p, 0);
        ++size;
        root = insert(new_node, root);
    }


    private KDT_Node insert(KDT_Node nn, KDT_Node rt) {

        if (rt == null) {
            return nn;
        }

        if (nn.point.equals(rt.point)) {        // if insert twice, keep old one.
            --size;
            return rt;
        }

        if (rt.split_feat == 0) {

            nn.split_feat = 1;

            if (nn.point.x() <= rt.point.x()) {
                rt.left = insert(nn, rt.left);
            } else {
                rt.right = insert(nn, rt.right);
            }
        }
        else if (rt.split_feat == 1) {

            nn.split_feat = 0;

            if (nn.point.y() <= rt.point.y()) {
                rt.left = insert(nn, rt.left);
            } else {
                rt.right = insert(nn, rt.right);
            }
        }
        else {
            throw new RuntimeException("goes more than 2 dimensions");
        }

        return rt;
    }


    public boolean contains(Point2D p)              // does the set contain point p?
    {
        if (p == null) {
            throw new NullPointerException("null point2d");
        }

        KDT_Node cur = root;

        while (cur != null) {

            if (cur.point.equals(p)) {
                return true;
            }

            if (cur.split_feat == 0) {
                if (p.x() <= cur.point.x()) {
                    cur = cur.left;
                } else {
                    cur = cur.right;
                }
            }
            else if (cur.split_feat == 1) {
                if (p.y() <= cur.point.y()) {
                    cur = cur.left;
                } else {
                    cur = cur.right;
                }
            }
            else {
                throw new RuntimeException("goes more than 2 dimensions");
            }
        }

        return false;
    }


    public void draw()                              // draw all points to standard draw
    {
        draw(root);
    }


    private void draw(KDT_Node rt) {

        if (rt == null) {
            return;
        }

        rt.point.draw();
        draw(rt.left);
        draw(rt.right);
    }


    public Iterable<Point2D> range(RectHV rect)     // all points that are inside the rectangle
    {
        if (rect == null) {
            throw new NullPointerException("null point2d");
        }

        List<Point2D> ret_list = new LinkedList<>();

        range(rect, root, ret_list);

        return ret_list;
    }


    private void range(RectHV rect, KDT_Node rt, List<Point2D> list) {

        if (rt == null) {
            return;
        }

        if (rect.contains(rt.point)) {
            list.add(rt.point);
        }

        if (rt.split_feat == 0) {

            if (rect.xmax() <= rt.point.x()) {
                range(rect, rt.left, list);
            } else if (rect.xmin() > rt.point.x()) {
                range(rect, rt.right, list);
            } else {
                range(rect, rt.left, list);
                range(rect, rt.right, list);
            }
        }
        else if (rt.split_feat == 1) {

            if (rect.ymax() <= rt.point.y()) {
                range(rect, rt.left, list);
            } else if (rect.ymin() > rt.point.y()) {
                range(rect, rt.right, list);
            } else {
                range(rect, rt.left, list);
                range(rect, rt.right, list);
            }
        }
        else {
            throw new RuntimeException("goes more than 2 dimensions");
        }
    }


    public Point2D nearest(Point2D p)               // a nearest neighbor in the set to point p;
    {                                               // null if the set is empty
        if (p == null) {
            throw new NullPointerException("null point2d");
        }

        Double[] min_dist = { Double.POSITIVE_INFINITY };
        Point2D[] ret_point = { null };
        RectHV  rect = new RectHV(0.0, 0.0, 1.0, 1.0);

        nearest(p, root, rect, min_dist, ret_point);

        return ret_point[0];
    }


    private void nearest(Point2D point, KDT_Node rt, RectHV rect, Double[] min_dist, Point2D[] ret_point) {

        if (rt == null) {
            return;
        }

        if (!rect.contains(point) && rect.distanceTo(point) >= min_dist[0]) {
            return;
        }


        double dist = point.distanceTo(rt.point);

        if (dist < min_dist[0]) {
            min_dist[0] = dist;
            ret_point[0] = rt.point;
        }

        if (rt.split_feat == 0) {

            RectHV left_rect = new RectHV(rect.xmin(), rect.ymin(), rt.point.x(), rect.ymax());
            RectHV right_rect = new RectHV(rt.point.x(), rect.ymin(), rect.xmax(), rect.ymax());

            if (point.x() <= rt.point.x()) {    // search left first

                nearest(point, rt.left, left_rect, min_dist, ret_point);
                nearest(point, rt.right, right_rect, min_dist, ret_point);

            } else {                            // search right first

                nearest(point, rt.right, right_rect, min_dist, ret_point);
                nearest(point, rt.left, left_rect, min_dist, ret_point);
            }
        }
        else if (rt.split_feat == 1) {

            RectHV bot_rect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), rt.point.y());
            RectHV top_rect = new RectHV(rect.xmin(), rt.point.y(), rect.xmax(), rect.ymax());

            if (point.y() <= rt.point.y()) {    // search bot first

                nearest(point, rt.left, bot_rect, min_dist, ret_point);
                nearest(point, rt.right, top_rect, min_dist, ret_point);

            } else {                            // search top first

                nearest(point, rt.right, top_rect, min_dist, ret_point);
                nearest(point, rt.left, bot_rect, min_dist, ret_point);
            }
        }
        else {
            throw new RuntimeException("goes more than 2 dimensions");
        }

    }


    public static void main(String[] args)          // unit testing of the methods (optional)
    {
        KdTree kdt = new KdTree();
        kdt.insert(new Point2D(0.0, 0.0));
        StdOut.println(kdt.size());
        kdt.insert(new Point2D(0.0, 0.9));
        StdOut.println(kdt.size());
        kdt.insert(new Point2D(0.6, 0.4));
        StdOut.println(kdt.size());
        kdt.insert(new Point2D(0.6, 0.4));
        StdOut.println(kdt.size());

    }
}
