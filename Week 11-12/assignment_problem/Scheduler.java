import java.util.*;

class Job {
    int id, priority;
    long arrival, duration, remaining, deadline, lastUpdate;

    Job(int id, long arrival, long duration, int priority, long deadline) {
        this.id = id;
        this.arrival = arrival;
        this.duration = duration;
        this.remaining = duration;
        this.priority = priority;
        this.deadline = deadline;
        this.lastUpdate = arrival;
    }
}

class Slice {
    int id;
    long start, end;
    Slice(int id, long start, long end) { this.id = id; this.start = start; this.end = end; }
}

public class Scheduler {
    public static void main(String[] args) {
        int[] id = {1, 2, 3};
        long[] arrival = {0, 2, 4};
        long[] duration = {5, 4, 2};
        int[] priority = {1, 1, 1};
        long[] deadline = {10, 12, 11};

        schedule(id, arrival, duration, priority, deadline, 2, 3, 3);
    }

    static void schedule(int[] id, long[] arrival, long[] duration, int[] basePri,
                         long[] deadline, long q, long A, int K) {

        int n = id.length;
        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < n; i++)
            jobs.add(new Job(id[i], arrival[i], duration[i], basePri[i], deadline[i]));

        Queue<Job>[] queues = new Queue[K + 1];
        for (int i = 1; i <= K; i++) queues[i] = new ArrayDeque<>();

        List<Slice> slices = new ArrayList<>();
        long time = 0, done = 0;
        long[] completion = new long[n];

        while (done < n) {
            for (Job j : jobs)
                if (j.arrival <= time && j.remaining > 0 && !inQueues(queues, j))
                    queues[j.priority].add(j);

            if (allEmpty(queues)) {
                time = nextArrival(jobs, time);
                continue;
            }

            for (Job j : jobs)
                if (j.remaining > 0 && j.arrival < time)
                    j.priority = (int) Math.min(K, j.priority + (time - j.lastUpdate) / A);

            Job cur = pickJob(queues);
            long run = Math.min(q, cur.remaining);

            slices.add(new Slice(cur.id, time, time + run));
            time += run;
            cur.remaining -= run;
            cur.lastUpdate = time;

            if (cur.remaining == 0) {
                completion[index(jobs, cur.id)] = time;
                done++;
            } else queues[cur.priority].add(cur);
        }

        printResults(jobs, completion, slices);
    }

    static void printResults(List<Job> jobs, long[] ct, List<Slice> slices) {
        System.out.println("Execution Order:");
        for (Slice s : slices)
            System.out.println("Job " + s.id + " ran from " + s.start + " to " + s.end);

        long totalWT = 0, maxWT = Long.MIN_VALUE, minWT = Long.MAX_VALUE, maxLate = 0;
        for (int i = 0; i < jobs.size(); i++) {
            Job j = jobs.get(i);
            long TAT = ct[i] - j.arrival;
            long WT = TAT - j.duration;
            long L = Math.max(0, ct[i] - j.deadline);
            totalWT += WT;
            maxWT = Math.max(maxWT, WT);
            minWT = Math.min(minWT, WT);
            maxLate = Math.max(maxLate, L);
            System.out.println("Job " + j.id + ": CT=" + ct[i] + " TAT=" + TAT + " WT=" + WT + " L=" + L);
        }
        System.out.println("Max Lateness=" + maxLate + ", Avg WT=" + (totalWT / jobs.size()) + ", Fairness=" + (maxWT - minWT));
    }

    static boolean allEmpty(Queue<Job>[] q) {
        for (int i = q.length - 1; i >= 1; i--) if (!q[i].isEmpty()) return false;
        return true;
    }

    static Job pickJob(Queue<Job>[] q) {
        for (int i = q.length - 1; i >= 1; i--) if (!q[i].isEmpty()) return q[i].poll();
        return null;
    }

    static boolean inQueues(Queue<Job>[] q, Job j) {
        for (int i = 1; i < q.length; i++) if (q[i].contains(j)) return true;
        return false;
    }

    static int index(List<Job> jobs, int id) {
        for (int i = 0; i < jobs.size(); i++) if (jobs.get(i).id == id) return i;
        return -1;
    }

    static long nextArrival(List<Job> jobs, long time) {
        long next = Long.MAX_VALUE;
        for (Job j : jobs) if (j.remaining > 0 && j.arrival > time) next = Math.min(next, j.arrival);
        return next == Long.MAX_VALUE ? time + 1 : next;
    }
}
