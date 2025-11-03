class DoublyLinkedListDeleteAll {
    class Node {
        int data;
        Node prev, next;
        Node(int data) { this.data = data; }
    }

    Node head;

    void deleteAll(int value) {
        Node temp = head;
        while (temp != null) {
            if (temp.data == value) {
                if (temp.prev != null)
                    temp.prev.next = temp.next;
                else
                    head = temp.next;

                if (temp.next != null)
                    temp.next.prev = temp.prev;
            }
            temp = temp.next;
        }
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
        DoublyLinkedListDeleteAll list = new DoublyLinkedListDeleteAll();
        list.head = list.new Node(10);
        Node n2 = list.new Node(20);
        Node n3 = list.new Node(30);
        Node n4 = list.new Node(20);
        Node n5 = list.new Node(40);
        list.head.next = n2; n2.prev = list.head;
        n2.next = n3; n3.prev = n2;
        n3.next = n4; n4.prev = n3;
        n4.next = n5; n5.prev = n4;

        System.out.print("Original List: ");
        list.display();

        list.deleteAll(20);

        System.out.print("After Deletion: ");
        list.display();
    }
}
