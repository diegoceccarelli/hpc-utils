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
package it.cnr.isti.hpc.io;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities to manage files. 
 *
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it
 * created on 25/gen/2012
 */
public class IOUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);


	/** 
	 * Opens a file. If the file ends with gz, it uses a GZIP reader. 
	 * <br/>
	 * It terminates the execution in case of any type of error.
	 * 
	 * @param file - the path of the file to open. 
	 * @return a reader to the file
	 */
	public static BufferedReader getPlainOrCompressedReader(String file) {
		BufferedReader br = null;
		try {
			if (file.endsWith(".gz")) {
				br = new BufferedReader(new InputStreamReader(
						new GZIPInputStream(new FileInputStream(file))));
			} else {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file)));
			}
		} catch (IOException e) {
			logger.error("opening the file {} ({})", file, e.toString());
			System.exit(-1);
		}
		return br;
	}
	
	/** 
	 * Opens an UTF8 file. If the file ends with gz, it uses a GZIP reader. 
	 * <br/>
	 * It terminates the execution in case of any type of error.
	 * 
	 * @param file - the path of the file to open. 
	 * @return a reader to the file
	 */
	public static BufferedReader getPlainOrCompressedUTF8Reader(String file) {
		BufferedReader br = null;
		try {
			if (file.endsWith(".gz")) {
				br = new BufferedReader(new InputStreamReader(
						new GZIPInputStream(new FileInputStream(file)),"UTF8"));
			} else {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file),"UTF8"));
			}
		} catch (IOException e) {
			logger.error("opening the file {} ({})", file, e.toString());
			System.exit(-1);
		}
		return br;
	}
	
	/** 
	 * Opens a file for writing. If the file ends with gz, it uses a GZIP writer. 
	 * <br/>
	 * It terminates the execution in case of any type of error.
	 * 
	 * @param file - the path of the file to open. 
	 * @return a writer to the file
	 */
	public static BufferedWriter getPlainOrCompressedWriter(String file) {
		BufferedWriter bw = null;
		try {
			if (file.endsWith(".gz")) {
				bw = new BufferedWriter(new OutputStreamWriter(
						new GZIPOutputStream(new FileOutputStream(file))));
			} else {
				bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(file)));
			}
		} catch (IOException e) {
			logger.error("opening the file {} ({})", file, e.toString());
			System.exit(-1);
		}
		
		return bw;
	}
	
	/** 
	 * Opens a UTF8 file for writing. If the file ends with gz, it uses a GZIP writer. 
	 * <br/>
	 * It terminates the execution in case of any type of error.
	 * 
	 * @param file - the path of the file to open. 
	 * @return a writer to the file
	 */
	public static BufferedWriter getPlainOrCompressedUTF8Writer(String file) {
		BufferedWriter bw = null;
		try {
			if (file.endsWith(".gz")) {
				bw = new BufferedWriter(new OutputStreamWriter(
						new GZIPOutputStream(new FileOutputStream(file)),"UTF8"));
			} else {
				bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(file),"UTF8"));
			}
		} catch (IOException e) {
			logger.error("opening the file {} ({})", file, e.toString());
			System.exit(-1);
		}
		
		return bw;
	}
	
	
	/** 
	 * Open a file and returns a string containing the content of the file 
	 * <br/>
	 * It terminates the execution in case of any type of error.
	 * 
	 * @param file - the path of the file to open. 
	 * @return a reader to the file
	 */
	public static String getFileAsString(String file){
		StringBuilder sb = new StringBuilder();
		BufferedReader br = getPlainOrCompressedReader(file);
		String line = "";
		try {
			while ( ( line = br.readLine()) != null){
				sb.append(line);
				sb.append("\n");
			}
			if (sb.length() > 0) // remove the last \n
				sb.deleteCharAt(sb.length()-1);
		} catch (IOException e) {
			logger.error("reading the file {} ({})", file, e.toString());
			System.exit(-1);
		}
		return sb.toString();
	}
	
	/**
	 * Returns a list where each element is a line of the file
	 * <br/>
	 * It terminates the execution in case of any type of error.
	 * 
	 * @param file the file to read 
	 * @return a list with the lines of the file
	 */
	public static List<String> getLines(String file){
		
		BufferedReader br = getPlainOrCompressedReader(file);
		List<String> lines = new ArrayList<String>(); 
		String line = "";
		try {
			while ( ( line = br.readLine()) != null){
				lines.add(line);
			}
		} catch (IOException e) {
			logger.error("reading the file {} ({})", file, e.toString());
			System.exit(-1);
		}
		return lines;
	}
	
	
	
	/**
	 * Returns a list where each element is a line of the UTF8 file
	 * <br/>
	 * It terminates the execution in case of any type of error.
	 * 
	 * @param file the path of the file to open
	 * @return a list with the lines of the file
	 */
	public static String getFileAsUTF8String(String file){
		StringBuilder sb = new StringBuilder();
		BufferedReader br = getPlainOrCompressedUTF8Reader(file);
		String line = "";
		try {
			while ( ( line = br.readLine()) != null){
				sb.append(line);
				sb.append("\n");
			}
			if (sb.length() > 0) // remove the last \n
				sb.deleteCharAt(sb.length()-1);
		} catch (IOException e) {
			logger.error("reading the file {} ({})", file, e.toString());
			System.exit(-1);
		}
		return sb.toString();
	}
	
	
	
}
