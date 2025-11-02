import java.util.*;

class WindowStats {
    int W;
    Queue<Event> queue;
    Map<String, Integer> userCount;
   
    WindowStats(int WSeconds) {
        this.W = WSeconds;
        this.queue = new LinkedList<>();
        this.userCount = new HashMap<>();
    }
   
    String ingest(long tMs, String userId) {
        long windowStart = tMs - (W * 1000L);
       
        while (!queue.isEmpty() && queue.peek().time <= windowStart) {
            Event old = queue.poll();
            userCount.put(old.user, userCount.get(old.user) - 1);
            if (userCount.get(old.user) == 0) {
                userCount.remove(old.user);
            }
        }
       
        queue.offer(new Event(tMs, userId));
        userCount.put(userId, userCount.getOrDefault(userId, 0) + 1);
       
        int unique = userCount.size();
        int qps = queue.size() / W;
       
        return unique + " " + qps;
    }
   
    static class Event {
        long time;
        String user;
        Event(long time, String user) {
            this.time = time;
            this.user = user;
        }
    }
   
    public static void main(String[] args) {
        WindowStats ws = new WindowStats(5);
       
        System.out.println("Event (1000, A): " + ws.ingest(1000, "A"));
        System.out.println("Event (2000, A): " + ws.ingest(2000, "A"));
        System.out.println("Event (2500, B): " + ws.ingest(2500, "B"));
        System.out.println("Event (7000, B): " + ws.ingest(7000, "B"));
       
        System.out.println("\nFinal Queue State:");
        for (Event e : ws.queue) {
            System.out.println("  (" + e.time + ", " + e.user + ")");
        }
    }
}

