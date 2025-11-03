class DetectRemoveLoop {
    static class Node {
        int data;
        Node next;
        Node(int d) { data = d; }
    }

    Node head;

    void detectAndRemoveLoop() {
        Node slow = head, fast = head;
        boolean loopExists = false;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                loopExists = true;
                break;
            }
        }

        if (!loopExists) {
            System.out.println("No loop found");
            return;
        }

        slow = head;
        while (slow.next != fast.next) {
            slow = slow.next;
            fast = fast.next;
        }

        fast.next = null; // remove loop
        System.out.println("Loop detected and removed.");
    }

    void display() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        DetectRemoveLoop list = new DetectRemoveLoop();
        list.head = new Node(10);
        list.head.next = new Node(20);
        list.head.next.next = new Node(30);
        list.head.next.next.next = new Node(40);
        list.head.next.next.next.next = new Node(50);

        // Creating a loop (50 → 30)
        list.head.next.next.next.next.next = list.head.next.next;

        list.detectAndRemoveLoop();
        list.display();
    }
}
