
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/*
    set implements linked list of nodes. 
    variables:
        tagNum: presents unique tag number 
        next: reference to next node in sorted list
*/
class Node{
    int tagNum;
    public volatile AtomicMarkableReference<Node> next;
    public Node(int tagNum){
        this.tagNum = tagNum;
    }
}

class Window{
    public Node pred, curr;

    Window(Node myPred, Node myCurr) {
        pred = myPred; curr = myCurr;
    }

    
    public static Window find(Node head, int tagNum)  {
        Node pred = null, curr = null, succ = null;
        boolean[] marked = {false};
        boolean snip;
        retry: while(true){
            pred = head;
            curr = pred.next.getReference();
            while(true){
                pred = head;
                curr = pred.next.getReference();
                while(true){
                    succ = curr.next.get(marked);
                    while(marked[0]){
                        snip =  pred.next.compareAndSet(curr, succ, false, false);
                        if(!snip) continue retry;
                        curr = succ;
                        succ = curr.next.get(marked);
                    }
                    if(curr.tagNum >= tagNum){
                        return new Window(pred, curr);
                    }
                    pred = curr;
                    curr = succ;
                }
            }
        }
    }
}


public final class birthdayPresentsParty{
    public static void main(String[] args) throws InterruptedException {
        int nServants = 4;

        linkedList list = new linkedList();

        Thread[] servants = new Thread[nServants];
        

        for(int i = 0; i < nServants; i++){
            servants[i] = new Thread(new linkedList(i));
            servants[i].start();
        }
        for(int k = 0; k < nServants; k++){
            servants[k].join();
        }

        System.out.print("All thank you notes written");
        
    }

    
}

//lock free synchronization from 9.8
class linkedList implements Runnable{
    static Node head;
    int tID;
    static int counter;
    
    public linkedList(){
        head = new Node(-1);
        Node tailone = new Node(500001);
        Node tailtwo = new Node(500002);
        head.next = new AtomicMarkableReference<Node>(tailone, false);
        tailone.next = new AtomicMarkableReference<Node>(tailtwo, false);
        counter = 0;
    }

    public linkedList(int tID){
        this.tID = tID;
    }

    @Override
    public void run(){
        int[] unordered = unorderedPresents(500000);
        Random r = new Random();
        while(true){
            for(int i = 0; i < 500000; i++){
                add(unordered[i]);
                //System.out.println(unordered[i] + "added");
                remove();
                int willContains = r.nextInt(1, 4);
                if(willContains == 3){
                    contains(unordered[i]);
                }
                if(counter > 500000)
                    return;
            }
        }

        
    }

    public boolean add( int tagNum){
        while(true){
            Window window = Window.find(head, tagNum);
            Node pred = window.pred, curr = window.curr;
            if(curr.tagNum == tagNum){
                return false;
            } else{
                Node node = new Node(tagNum);
                node.next = new AtomicMarkableReference<>(curr, false);
                if(pred.next.compareAndSet(curr, node, false, false)){
                    return true;
                }
            }
        }

    }


    public boolean remove(){
        Node pred = head;
        Node second = pred.next.getReference();
        Node third = second.next.getReference();

        if (second.next == null || third.next == null) 
        {
            return false;
        }

        int n = second.tagNum;

        counter++;
        //System.out.println("removing " + n);

        head.next = new AtomicMarkableReference(third, false);

        return true;
            
    }  
    

    public boolean contains(int tagNum){
        boolean[] marked = {false};
        Node curr = head;
        while(curr.tagNum < tagNum){
            curr = curr.next.getReference();
        }
        return (curr.tagNum == tagNum && !marked[0]);
    }

    public static int[] unorderedPresents(int nPresents){
        Integer[] unordered = new Integer[nPresents];
        int[] unorderedP = new int[nPresents];
        for(int i = 0; i < nPresents; i++){
            unordered[i] = i + 1;
        }

        List<Integer> intList = Arrays.asList(unordered);


		Collections.shuffle(intList);

        for(int i = 0; i < nPresents; i++){
             unorderedP[i] = unordered[i];
        }
		intList.toArray(unordered);
        
        return unorderedP;
    }


}