# MendixMerger
You are given a directory with hundreds of files. Each file contains a sorted list of hundreds of words, one word per line. This project merges the content of all the files into one sorted file.

# Algorithm
In this Project, we used priority queue based solution to merge sorted files. By default Java's Priority Queue is based on min-heap.
So we first create min-heap nodes such that each node contain the first word from input files. So if there are 500 input sorted files then 500 min-heap node will be created. Also each node will have a reference of InputStreamReader to corresponding file.
We used InputStreamReader because it will read file efficiently i.e only when needed.
At the same time these heap-node will be added to our Priority Queue. 

Now we have a Priority Queue which will have minimum element(word in our case) at the root.

Now we will do following steps until all input file reached EOF (End Of File) :

1. poll the element from the Priority Queue. which will return and remove the current minimum element in the heap. 
2. store that element in the output file.
3. peek the element from the Priority Queue. which will return the current minimum element in the heap.
4. while ( current minimum element >= current input file's next word )
   a. store current input file's next word in the output file.
5. Now we get the next word that should be inserted in to the Priority Queue. 
6. insert that word to Priority Queue that will heapify it.

After above steps, we will get merged sorted file in the output directory.

# Time Complexity : O(NLogM)
Suppose we have M number of files and N number of total element in all files.
In every iteration of loop, we are inserting an element in the Priority Queue which takes O(LogM) time. Therefore, the time complexity is O(NLogM).
But In every step, we are also checking if we have some words in the current file which are less than or equal to current root of the Priority Queue. In those cases we don't need to insert these words in the Priority Queue. These words will be directly stored in the output file. This will make our solution more effcient as we are avioiding heapify process of O(logM). 

# Space Complexity : O(M)
At any given time, we have at max M elements in the Priority Queue. So thats make space complexity of O(M), where M is the number of sorted files.

# Scalability
The problem is that some time we can have large amount of sorted files needs to be merged that can't fit in the main memory.
Since we are not loading whole sorted file in the main memory as we are reading one word at a time from the sorted file, That means at any given time we will have at max M(number of sorted files) words in the main memory and we are doing operation on them only. This makes our solution scalable.

# Build
This is a Spring boot console application. In this project we are using gradle build tool. You have to just import the gradle file in any gradle supported IDE like Intellij and build it.

# Running the project
This Command line utility have 3 command line options as: 

usage: mendix-merger
                             
1.  -numberOfFiles <Number of files>  :  provide total number of sorted files
                                         to be merged. This field is mandatory.
  
2.  -inPath <input files location>    :  provide path of the directory where
                                         all sorted files are present. If not
                                         provided then current working
                                         directory will be used.
  
3.  -outPath <output location>        :  provide path of the directory where
                                         resultant sorted file will be stored.
                                         If not provided then current working
                                         directory will be used.

Before running the jar file, you need to create a directory named "config" in the same location where jar file is located and then put two files : application.properties and logback-spring.xml in that directory.
You can specify the logging directory path in the application.properties, where all logs will be generated.

You can run the jar file using below command:

   Java -jar <jar_file_name> -numberOfFiles <number files> -inPath <input files location> -outPath <output location>
  
After running the jar file, merged file as result.dat will be created in the output directory location.

# Environment Requirement
Plateform should have java version 8 or above installed.

