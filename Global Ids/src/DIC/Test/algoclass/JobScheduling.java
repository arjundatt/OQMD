package DIC.Test.algoclass;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 6/3/15
 * Time: 11:08 AM
 */
public class JobScheduling {
    public static void main(String[] args) {
        ArrayList<Job> arrayList = new ArrayList<Job>();
        arrayList.add(new Job(1, 2));
        arrayList.add(new Job(2, 1));
        arrayList.add(new Job(1, 2));
        Collections.sort(arrayList);
        for (Job job : arrayList) {
            System.out.println(job.weight + " " + job.length);
        }
    }
}

class Job implements Comparable<Job> {
    int weight;
    int length;

    Job(int weight, int length) {
        this.weight = weight;
        this.length = length;
    }

    @Override
    public int compareTo(Job o) {
        if (((double) length) / weight > ((double) o.length) / o.weight)
            return 1;
        else if (((double) length) / weight == ((double) o.length) / o.weight)
            return 0;
        else
            return -1;
    }
}
