package com.mendix.merger.merge;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class MergeApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testMergerSuccess(){
		MergeFiles mergeFiles = new MergeFiles("src/test/resources/input", "src/test/resources/output", 5);
		mergeFiles.merge();
		try {
			Assertions.assertTrue(FileUtils.contentEquals(new File("src/test/resources/output/sorted_original.txt"), new File("src/test/resources/output/result.dat")), "Files matched successfully");
		}
		catch (IOException ioe){
			System.out.println("Exception occurred: " + ioe.getMessage());
		}
	}

}
