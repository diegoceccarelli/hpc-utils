/**
 *  Copyright 2012 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package it.cnr.isti.hpc.parallel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.isti.hpc.io.IOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ThreadPool allows to run a simple job in parallel using a pool of 
 * threads. The jobs comes as a class implementing the interface 
 * {@link FunctionToExec} and the input is a unique file where each 
 * line is an input to be processed. Each thread pulls a line and 
 * process it, then writes the output on a individual or shared file.
 *  
 * 
 * @see FunctionToExec
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Nov 19, 2012
 */
public class ThreadPool {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ThreadPool.class);

	/**
	 * where to store the output file of each thread
	 */
	private static final String DEFAULT_OUTPUT_FOLDER = "/tmp/hpc-parallel";
	/**
	 * the name of the output file
	 */
	private static final String DEFAULT_OUTPUT_FILE = "output";
	/**
	 * default number of threads
	 */
	private static final int DEFAULT_THREADS = 10;
	
	/**
	 * how much to sleep between two calls of the same thread 
	 * (useful when you perform scraping) 
	 */
	private static final int THREAD_SLEEP_TIME = 1000; // 1 sec
	
	/**
	 * if use a unique output file shared between the threds, 
	 * or create a file for each thread.
	 */
	private boolean oneOutputPerThread = true;
	private File outputFile = new File(DEFAULT_OUTPUT_FILE);
	private int nThreads = DEFAULT_THREADS;
	private File outputFolder = new File(DEFAULT_OUTPUT_FOLDER);
	
	private BufferedReader reader;
	private boolean running = true;
	private boolean flush = false;
	

	private long sleepTime = THREAD_SLEEP_TIME;

	private FunctionToExec function;

	private BufferedWriter writer;
	
	
	/**
	 * Creates the thread pull.
	 * 
	 * @param nThreads number of thread to use.
	 * @param inputFile the input file, each line is an input for a thread.
	 * @param function the action that each thread has to perform. 
	 */
	public ThreadPool(int nThreads, String inputFile, FunctionToExec function) {
		this.function = function;
		reader = IOUtils.getPlainOrCompressedUTF8Reader(inputFile);
		this.nThreads = nThreads;
	}
	
	/**
	 * Performs the flush everytime a thread writes something (for debugging)
	 * @return this threadpool
	 */
	public ThreadPool flush(){
		flush = true;
		return this;
	}
	
	/**
	 * Uses only one output file.
	 * @return this threadpool
	 */
	public ThreadPool useOnlyOneOutputFile(){
		oneOutputPerThread = false;
		return this;
	}
	
	
	
	/**
	 * Sets thread sleep time
	 * @return this threadpool
	 */
	public ThreadPool setThreadSleepTime(long millis){
		sleepTime = millis;
		return this;
	}
	
	/**
	 * Sets the output folder.
	 * @param folder the output folder
	 * @return this threadpool
	 */
	public ThreadPool setOutputFolder(String folder){
		this.outputFolder = new File(folder);
		return this;
	}
	
	
	/**
	 * Sets the output file name.
	 * @param file the output file
	 * @return this thread pool
	 */
	public ThreadPool setOutputFile(String file){
		this.outputFile = new File(file);
		return this;
	}
	
	
	
	/**
	 * Starts the threads. 
	 */
	public void run() {
		
		if (!outputFolder.exists()) {
			logger.info("creating output folder in {} ", outputFolder);
			outputFolder.mkdir();
		}
		
		
		if (!oneOutputPerThread){
			File out = new File(outputFolder, outputFile.getName());
			logger.info("opening {} ", outputFile);
			writer = IOUtils.getPlainOrCompressedUTF8Writer(out.getAbsolutePath());
		}
		
		List<Thread> threads = new ArrayList<Thread>(nThreads);

		for (int i = 0; i < nThreads; i++) {
			if (oneOutputPerThread) {
				threads.add(new ConsumerThread(i));
			} else {
				threads.add(new ConsumerThread(i, writer));
			}
		}
		for (int i = 0; i < nThreads; i++) {
			logger.info("Runnning thread {} ",i);
			threads.get(i).start();
		}

		for (int i = 0; i < nThreads; i++) {
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				logger.info("error {} ", e.toString());
				System.exit(-1);
			}
		}
		if (! oneOutputPerThread){
			try {
				logger.info("closing uniq output file");
				writer.close();
			} catch (IOException e) {
				logger.info("closing output file {} ",e.toString());
			}
		}

	}

	
	class ConsumerThread extends Thread {

		BufferedWriter threadWriter;
		private int id;

		public ConsumerThread(int id) {
			this.id = id;
			File output = new File(outputFolder, outputFile.getName() + "-"
					+ id);
			logger.info("opening {} ", output);
			threadWriter = IOUtils.getPlainOrCompressedUTF8Writer(output
					.getAbsolutePath());

		}

		public ConsumerThread(int id, BufferedWriter writer) {
			this.id = id;
			logger.info("using default shared writer");
			this.threadWriter = writer;
		}

		private String readLine() {
			synchronized (reader) {
				try {
					return reader.readLine();
				} catch (IOException e) {
					logger.info("reading input file {} ", e.toString());
				}
				return null;
			}
		}

		@Override
		public void run() {
			
			while (running) {
				
				String line = readLine();
				if (line == null) {
					running = false;
					continue;
				}

				StringBuilder sb = function.run(line);
				if (oneOutputPerThread) {
					outputOnThreadFile(sb);
				} else {
					outputOnSharedFile(sb);
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					logger.error("sleeping ({})",e.toString());
					running = false;
					continue;
				}
			}
			if (oneOutputPerThread){
				try {
					threadWriter.close();
				} catch (IOException e) {
					logger.error("closing file for thread {} ({})", id, e.toString());
					System.exit(-1);
				}
			}
			

		}

		private void outputOnSharedFile(StringBuilder sb) {
		
			synchronized (threadWriter) {
				
				try {
					threadWriter.write(sb.toString());
					threadWriter.newLine();
				} catch (IOException e) {
					logger.error("writing output file for thread {} ({})", id,
							e.toString());
					System.exit(-1);
				}

			}

		}

		private void outputOnThreadFile(StringBuilder sb) {

			try {
				threadWriter.write(sb.toString());
				threadWriter.newLine();
				if (flush) threadWriter.flush();
			} catch (IOException e) {
				logger.error("writing output file for thread {} ({})", id,
						e.toString());
				System.exit(-1);
			}
		}

	}

}
