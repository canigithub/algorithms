import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {


    public static void main(String[] args)
    {
        int k = Integer.parseInt(args[0]);

        if (k == 0) {
            return;
        }

        RandomizedQueue<String> rq = new RandomizedQueue<>();


        /*** Use Reservoir Sampling ***/
        int count = 0;
        while (!StdIn.isEmpty()) {

            String s = StdIn.readString();

            if (++count <= k) {
                rq.enqueue(s);
            } else {

                String temp = rq.dequeue();
                int r = StdRandom.uniform(count);

                if (r < k) {
                    rq.enqueue(s);
                } else {
                    rq.enqueue(temp);
                }
            }
        }

        for (String s : rq) {
            StdOut.println(s);
        }
    }
}
