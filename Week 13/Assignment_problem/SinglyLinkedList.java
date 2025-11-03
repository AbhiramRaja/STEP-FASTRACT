class SinglyLinkedList {
    class Node {
        int data;
        Node next;
        Node(int data) { this.data = data; }
    }

    Node head;

    public void insertAtPosition(int data, int pos) {
        Node newNode = new Node(data);

        if (pos < 1) {
            System.out.println("Invalid position!");
            return;
        }

        if (pos == 1) {
            newNode.next = head;
            head = newNode;
            return;
        }

        Node temp = head;
        for (int i = 1; temp != null && i < pos - 1; i++)
            temp = temp.next;

        if (temp == null) {
            System.out.println("Position out of range!");
            return;
        }

        newNode.next = temp.next;
        temp.next = newNode;
    }

    public void display() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data);
            if (temp.next != null) System.out.print(" → ");
            temp = temp.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        SinglyLinkedList list = new SinglyLinkedList();
        list.head = list.new Node(10);
        list.head.next = list.new Node(20);
        list.head.next.next = list.new Node(30);
        list.head.next.next.next = list.new Node(40);

        System.out.print("Original List: ");
        list.display();

        list.insertAtPosition(50, 3);

        System.out.print("After Insertion: ");
        list.display();
    }
}
