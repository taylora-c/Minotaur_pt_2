import java.util.*;
import java.util.List;
import java.util.stream.IntStream;
import java.util.OptionalInt;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Iterator;

 


// 1 hr = 3 sec
//1 min = .05 sec
public class marsRover {
    public static List<Thread> roverThreads;
    public static void main(String[] args) throws InterruptedException{

        int nThreads = 8;
        
        Thread thermos[] = new Thread[nThreads];

        //prints readings for 12 'hours' worth of data
        for(int i = 0; i < 4; i++){
            //takes in temperatures at every 'minute'
            for(int j = 0; j < 60; j++){
                //reads 8 temperatures at every 'minute'
                for(int k = 0; k < nThreads; k++){
                    thermos[k] = new Thread(new tempReader(k, j));
                    thermos[k].start();
                }
                for(int k = 0; k < nThreads; k++){
                    thermos[k].join();
                }
            }
            System.out.println("\n\nReport for hour " + (i + 1));
            tempReader.printMax();
            tempReader.printMin();
            tempReader.printTenMins();
            tempReader.clearMap();
        }
    }
}

class tempReader implements Runnable{

    ReentrantLock lock = new ReentrantLock();
    Random r = new Random();
    int tID;
    int minute;
    static int[][] temps = new int[8][60];
    //SortedList<Integer> list = new SortedList<Integer>();
    static TreeSet <Integer> uniqueTemps = new TreeSet<Integer>() ;
    

    static Integer key = 0;
    
    public tempReader(int i, int minute){
        this.tID = i;
        this.minute = minute;
    }
    public tempReader(){

    }

    @Override
    public void run(){
        IntStream  num = r.ints(1, -100, 71);
        OptionalInt slay = num.findFirst();
        int rNum = slay.getAsInt();
        temps[tID][minute] = rNum;
        key++;
    }

    static public void clearMap(){
        uniqueTemps.clear();
    }

    static public void createSet(){
        for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 60; j++)
                {
                    uniqueTemps.add(temps[i][j]);
                } 

            }
    }

    static public void printMax(){  
        createSet();
        Iterator it = uniqueTemps.descendingIterator();
        uniqueTemps.iterator();
        System.out.println("Top 5 temps of hour:");
        for (int i = 0; i < 5; i++)  {
            System.out.println(it.next());
        }
    }

    static public void printMin(){ 
        System.out.println("Lowest 5 temps of hour:");
        Iterator it = uniqueTemps.iterator();
        uniqueTemps.iterator();
        for (int i = 0; i < 5; i++)  {
            System.out.println(it.next());
        }
    }

    static public void printTenMins(){
        int[] highestInMin = new int[60];
        int[] lowestInMin = new int[60];
        int biggestDifference = 0;
        highestInMin[0] = temps[0][0];

        for(int i = 0; i < 60; i++){
            highestInMin[i] = temps[0][i];
            lowestInMin[i] = temps[0][i];
            for(int j = 0; j < 8; j++){
                for(int k = 0; k < 8; k ++){
                    if(temps[k][i] > temps[j][i]){
                        highestInMin[i] = temps[k][i];
                    }
                    if(temps[k][i] < temps[j][i]){
                        lowestInMin[i] = temps[k][i];
                    }
                }
            }
        }

        int lowMin = 0;
        int highMin = 10;
        int diff1 = 0;
        int diff2 = 0;
        for(lowMin = 0,highMin = 10; highMin<60; lowMin++, highMin++){
            diff1 = Math.abs(highestInMin[lowMin] - lowestInMin[highMin]);
            diff2 = Math.abs(highestInMin[highMin] - lowestInMin[lowMin]);
            if((diff1 > diff2) && (diff1 > biggestDifference)){
                biggestDifference = diff1;
            }
            else if((diff2 > diff1) && (diff2 > biggestDifference)){
                biggestDifference = diff2;
            }
        }
        System.out.println("Biggest Difference in 10 Minute Range:");
        System.out.println(biggestDifference);
    }


    

}


