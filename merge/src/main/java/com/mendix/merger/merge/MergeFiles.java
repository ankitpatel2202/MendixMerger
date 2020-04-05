package com.mendix.merger.merge;

import com.mendix.merger.merge.minheap.HeapNode;
import com.mendix.merger.merge.minheap.HeapNodeComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.PriorityQueue;

public class MergeFiles {
    private static final Logger logger = LoggerFactory.getLogger(MergeFiles.class);

    private String inputPath;
    private String outputPath;
    private int totalFiles;

    public MergeFiles(String inputPath, String outputPath, int totalFiles) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.totalFiles = totalFiles;
    }

    /**
     *  merge sorted files in to a single output file
     */
    public void merge()
    {
        PriorityQueue<HeapNode> heapNodePriorityQueue = new PriorityQueue<>(new HeapNodeComparator());
        File[] files = getFiles();

        int fileReadErrorCount = 0;
        // Create a min heap such that every heap node has first line of a file

        for(int i = 0; i < files.length; i++)
        {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(files[i]));
                HeapNode node = new HeapNode(inputStreamReader);
                String firstWord = node.getNextWord();
                if(!firstWord.isEmpty()){
                    node.setWord(firstWord);
                    heapNodePriorityQueue.add(node);
                } else {
                    ++fileReadErrorCount;
                    logger.warn("Ignoring the file: {} because of the file is empty.", files[i].getName());
                }

            } catch (IOException e) {
                ++fileReadErrorCount;
                logger.warn("Ignoring the file: {} because of an error occurred while reading file : {}", files[i].getName(), e.getCause());
            }
        }


        BufferedWriter bw = null;
        FileOutputStream fos = null;
        try {
            File fout = new File(outputPath + "\\" + "result.dat");
            fos = new FileOutputStream(fout);
            bw = new BufferedWriter(new OutputStreamWriter(fos));

            int emptyFileCount = 0;

            // Now one by one get the minimum element from min heap and replace it with next element.
            while (emptyFileCount != files.length - fileReadErrorCount)
            {
                // Get the minimum element and store it in output file
                HeapNode root = heapNodePriorityQueue.poll();
                if(root != null){
                    bw.write(root.getWord());
                    bw.newLine();

                    // Find the next element that will replace current root of heap. The next element belongs to same input file as the current min element.
                    String  nextWord = getNextWordFromCurrentFile(root, heapNodePriorityQueue, bw);
                    if(!nextWord.isEmpty()){
                        root.setWord(nextWord);
                        // Replace root with next element of input file
                        heapNodePriorityQueue.add(root);
                    } else {
                        //input file reached EOF.
                        emptyFileCount++;
                    }
                } else {
                    emptyFileCount++;
                }
            }

        }
        catch (IOException e) {
            logger.error("Am error occurred while reading files: {}",e.getCause());
        }
        finally{
            try {
                if (bw != null){
                    bw.close();
                }
            } catch (IOException e) {
                logger.error("Am error occurred while closing buffer reader: {}",e.getCause());
            }
        }
    }

    /**
     *  Parse the current file pointed in the input node and write all words which are less than current root's word into the output file
     */
    private String getNextWordFromCurrentFile(HeapNode node, PriorityQueue<HeapNode> heapNodePriorityQueue, BufferedWriter bw) throws IOException {
        String nextWord = node.getNextWord();
        if (heapNodePriorityQueue.peek() != null){
            String currentMin = heapNodePriorityQueue.peek().getWord();
            while(!nextWord.isEmpty() && nextWord.compareTo(currentMin) <=0){
                bw.write(nextWord);
                bw.newLine();
                nextWord = node.getNextWord();
            }
        }
        return nextWord;
    }

    /**
     *  get array of file object of all sorted files
     */
    private File[] getFiles(){
        File[] files = new File[totalFiles];
        for(int i=0; i<totalFiles; i++){
            files[i] = new File(inputPath + "\\" + i + ".dat");
        }
        return files;
    }
}
