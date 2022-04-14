# Minotaur_pt_2

Problem 1: Minotaurs Birthday Presents 

To Compile: javac birthdayPresentsParty.java

To Run: java birthdayPresentsParty 


Preblem 2: Mars Rover Temperatures

To Compile: javac marsRover.java

To Run: java marsRover

Problem 1: 

My solution uses a lock-free linked-list implementation. The base of my code was based on chapter 9.8 of the text book "The Art of Multiprocessor Programming". It uses the AtomicMarkableReference library on the next component of a node. This allows a node to be marked logically for removal before it is physically removed. I also used a Window class which holds the predecessor and current node. The window find function takes a head and a tag number, and finds the nodes it should be. When this is used in the add function of the linked list, what is returned is the spot where the node should be added to. If the returned current tagNumber is already present, then the list is unchanged. When find is used in the contains function, the current node returned is either the node containing the present being searched for, or its the one that would come right after it, showing that that present does not exist in the list.

At the creation of the linked list I created 3 sentinel nodes. The find function requires 3 nodes to find where a node should be placed. This does not affect the 'presents' being added since the tag numbers for these nodes were out of range of the presents tag numbers.

I changed the remove function from the textbook so that the thread removing an item takes from the head instead of searching for specific present. This makes it less likely that it will have to wait for a present to be added for it to be deleted. 

Problem 2:

For problem 2, I used for loops to represent time intervals. The outer-loop represents the hours that reports are printed on. I chose 12 as an arbitrary value of 'hours' to run for. The middle loop represents the minutes. This is how many times a thermometer would take readings in an hour. The inner-loop represents the thermometers the Rover has that take a reading every minute. 


        for(int i = 0; i < 12; i++){

            for(int j = 0; j < 60; j++){

                for(int k = 0; k < nThreads; k++){

When the inner-loop is reached, the thermometers are called to read in temps. The temps are then stored in a 2d array. The rows represent each thermometer and the columns represent each minute in an hour. When the hour is up. This data is erased and new data is welcomed.

static int[][] temps = new int[8][60];

To find the maximum 5 and minimum 5 temperatures in an hour, I created a TreeSet and added all the values in the temp 2d array. This meant that all values would be sorted and unique. Then I initated an iterator to print the values. 