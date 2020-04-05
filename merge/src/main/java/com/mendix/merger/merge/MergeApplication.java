package com.mendix.merger.merge;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Arrays;

@SpringBootApplication
public class MergeApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(MergeApplication.class);

	private String inputFilesLocation;
	private String outputFileLocation;
	private int totalFiles;
	private Options options;

	public static void main(String[] args) {
		SpringApplication.run(MergeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.", Arrays.toString(args));
		initCommandLineOptions();
		try {
			populateArgs(args);
		} catch (ParseException e){
			logger.error("Error occurred while parsing command line arguments: {}",e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "mendix-merger", options );
			return;
		}
		MergeFiles mergeFiles = new MergeFiles(inputFilesLocation, outputFileLocation, totalFiles);
		mergeFiles.merge();
	}

	/**
	 *  Initialize Command Line options
	 */
	private void initCommandLineOptions(){
		options = new Options();

		Option numberOfFiles = Option.builder("numberOfFiles")
				.required(true)
				.hasArg()
				.argName("Number of files")
				.numberOfArgs(1)
				.desc("provide total number of sorted files to be merged. This field is mandatory")
				.build();

		Option inputLocation = Option.builder("inPath")
				.hasArg()
				.argName("input files location")
				.numberOfArgs(1)
				.desc("provide path of the directory where all sorted files are present. If not provided then current working directory will be used.")
				.build();

		Option outputLocation = Option.builder("outPath")
				.hasArg()
				.argName("output location")
				.numberOfArgs(1)
				.desc("provide path of the directory where resultant sorted file will be stored. If not provided then current working directory will be used.")
				.build();


		options.addOption(numberOfFiles);
		options.addOption(inputLocation);
		options.addOption(outputLocation);
	}

	/**
	 * Populate Command line arguments using command line parser
	 */
	private void populateArgs(String[] args) throws ParseException{
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		//get input files location
		if (cmd.hasOption("inPath")) {
			this.inputFilesLocation = cmd.getOptionValue("inPath");
			if(!checkPathValidity(this.inputFilesLocation)){
				System.out.println("invalid argument of inPath option. Please provide valid path.");
				throw new ParseException("invalid argument of inPath option: " + this.inputFilesLocation);
			}
		} else {
			//setting default value as current working directory
			this.inputFilesLocation = ".";
		}

		//get output files location
		if (cmd.hasOption("outPath")) {
			this.outputFileLocation = cmd.getOptionValue("outPath");
			if(!checkPathValidity(this.outputFileLocation)){
				System.out.println("invalid argument of outPath option. Please provide valid path.");
				throw new ParseException("invalid argument of outPath option: " + this.outputFileLocation);
			}
		} else {
			//setting default value as current working directory
			this.outputFileLocation = ".";
		}

		//get Sorted files count
		if (cmd.hasOption("numberOfFiles")) {
			this.totalFiles = Integer.parseInt(cmd.getOptionValue("numberOfFiles"));
			if(totalFiles <= 0){
				System.out.println("invalid argument of numberOfFiles option. Please provide valid number greater than zero.");
				throw new ParseException("invalid argument of numberOfFiles option: " + totalFiles);
			}
		}
	}

	private boolean checkPathValidity(String path){
		File file = new File(path);
		return file.exists();
	}
}
