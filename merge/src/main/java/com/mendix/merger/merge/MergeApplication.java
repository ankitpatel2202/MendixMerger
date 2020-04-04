package com.mendix.merger.merge;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class MergeApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(MergeApplication.class);

	private String inputFilesLocation;
	private String outputFileLocation;
	private Options options;

	public static void main(String[] args) {
		SpringApplication.run(MergeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.", Arrays.toString(args));
		initCommandLineOptions();
		populateArgs(args);
	}

	/**
	 *  Initialize Command Line options
	 */
	private void initCommandLineOptions(){
		options = new Options();

		Option inputLocation = Option.builder("in-path")
				.hasArg()
				.argName("input files location")
				.numberOfArgs(1)
				.desc("provide path of the directory where all sorted files are present. If not provided then current working directory will be used.")
				.build();

		Option outputLocation = Option.builder("out-path")
				.hasArg()
				.argName("output location")
				.numberOfArgs(1)
				.desc("provide path of the directory where resultant sorted file will be stored. If not provided then current working directory will be used.")
				.build();

		options.addOption(inputLocation);
		options.addOption(outputLocation);
	}

	/**
	 * Populate Command line arguments using command line parser
	 */
	private void populateArgs(String[] args){
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);

			//get input files location
			if (cmd.hasOption("in-path")){
				this.inputFilesLocation = cmd.getOptionValue("in-path");
			}
			else {
				//setting default value as current working directory
				this.inputFilesLocation = ".";
			}

			//get output files location
			if (cmd.hasOption("out-path")){
				this.outputFileLocation = cmd.getOptionValue("out-path");
			}
			else {
				//setting default value as current working directory
				this.outputFileLocation = ".";
			}
		}
		catch (ParseException e) {
			logger.error("Error occurred while parsing command line arguments: {}",e.getMessage());
			e.printStackTrace();
		}
	}
}
