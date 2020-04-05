package com.mendix.merger.merge.minheap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;

public class HeapNode {
    private static final Logger logger = LoggerFactory.getLogger(HeapNode.class);

    private String word;
    private InputStreamReader inputStreamReader;

    public HeapNode(InputStreamReader inputStreamReader) {
        this.inputStreamReader = inputStreamReader;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    /**
     *  read the next word from the current inputStream object
     */
    public String getNextWord() throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        int ch = 0;
        try {
            ch = inputStreamReader.read();
            while (ch != '\n' && ch != -1){
                stringBuilder.append((char) ch);
                ch = inputStreamReader.read();
            }
        } catch (IOException e) {
            logger.error("An error occurred while reading file: {}",e.getCause());
            throw e;
        } finally {
            if(ch == -1){
                closeInputStream();
            }
        }
        return stringBuilder.toString();
    }

    private void closeInputStream(){
        try {
            inputStreamReader.close();
        } catch (IOException e) {
            logger.error("An error occurred while closing InputStreamReader: {}",e.getCause());
        }
    }
}
