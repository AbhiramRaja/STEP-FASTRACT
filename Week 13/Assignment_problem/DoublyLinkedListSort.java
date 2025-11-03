class DoublyLinkedListSort {
    class Node {
        int data;
        Node prev, next;
        Node(int data) { this.data = data; }
    }

    Node head;

    void bubbleSort() {
        boolean swapped;
        do {
            swapped = false;
            Node curr = head;
            while (curr != null && curr.next != null) {
                if (curr.data > curr.next.data) {
                    int temp = curr.data;
                    curr.data = curr.next.data;
                    curr.next.data = temp;
                    swapped = true;
                }
                curr = curr.next;
            }
        } while (swapped);
    }

    void display() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data);
            if (temp.next != null) System.out.print(" ⇄ ");
            temp = temp.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        DoublyLinkedListSort list = new DoublyLinkedListSort();
        list.head = list.new Node(40);
        Node n2 = list.new Node(10);
        Node n3 = list.new Node(30);
        Node n4 = list.new Node(20);
        list.head.next = n2; n2.prev = list.head;
        n2.next = n3; n3.prev = n2;
        n3.next = n4; n4.prev = n3;

        System.out.print("Before Sorting: ");
        list.display();

        list.bubbleSort();

        System.out.print("After Sorting: ");
        list.display();
    }
}
