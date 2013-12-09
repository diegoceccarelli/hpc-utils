/**
 *  Copyright 2011 Diego Ceccarelli
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

package it.cnr.isti.hpc.cli;

import it.cnr.isti.hpc.io.IOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbtractCommandLineInterface provides facilities to create command line interface
 * classes (CLI). 
 * A class extending this class, accepts by default an input and an output file, i.e.,
 * can be called using the syntax: 
 * <pre>
 * 
 * java YourClassCLI -input fileinput  -output fileoutput
 * 
 * </pre>
 * where fileinput and fileoutput represent two filepaths. Please note that 
 * fileinput must point to an existing input file while if fileoutput does not exist
 * the file will create (otherwise will be overwritten). 
 * <br/>
 * <br/>
 * It is possible to modify the parameters accepted giving the list in the constructor. 
 * The class will take care to valide them and to fail if a parameter is missing. 
 * You can also define an usage message that will be printed everytime the cli is 
 * called with wrong parameters.
 * <br/>
 * <br/>
 * <strong>Please, add the suffix CLI to the classes that extend this class, e.g., 
 *  MyclassCLI.java.
 * </strong>
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 21/set/2011
 */
public abstract class AbstractCommandLineInterface {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractCommandLineInterface.class);

	/**
	 * Default name for the input file
	 */
	public static final String INPUT = "input";
	/**
	 * Default name for the output file
	 */
	public static final String OUTPUT = "output";
	protected Map<String, String> params = new HashMap<String, String>();

	private String inputFile, outputFile;
	
	/**
	 * Default usage string, overwrite it to change the message
	 */
	protected String usage = "java -cp $jar " + this.getClass()
			+ " -input fileinput -output fileoutput";
	private BufferedReader br;
	private BufferedWriter bw;
	private String[] args; 
	
	/** 
	 * Do not use this costructor
	 */
	public AbstractCommandLineInterface(){

	}
	/**
	 * Default costructor 
	 * @param args the main args
	 */
	public AbstractCommandLineInterface(String[] args) {
		this();
		if (args == null ){
			System.err.println(usage);
			System.exit(-1);
		}
		params.put(INPUT, "");
		params.put(OUTPUT, "");
		this.args = args;
		getParams();
	}
	/**
	 * 
	 * @param args the main args
	 * @param parameters the parameters accepted by this cli, if different by the default.
	 */
	public AbstractCommandLineInterface(String[] args, String[] parameters) {
		this();
		if (args == null ){
			System.err.println(usage);
			System.exit(-1);
		}
		this.args = args;
		for (String p : parameters)
			params.put(p, "");
		getParams();
	}
	
	/**
	 *
	 * @param args the main args
	 * @param parameters the parameters accepted by this cli, if different by the default.
	 * @param usage an usage string if different by the default.
	 */
	public AbstractCommandLineInterface(String[] args, String[] parameters,String usage) {
		this();
		if (args == null ){
			System.err.println(usage);
			System.exit(-1);
		}
		this.args = args;
		this.usage = usage;
		for (String p : parameters)
			params.put(p, "");
		getParams();
	}

	/**
	 * Returns the input filename.
	 * @return the input filename
	 */
	protected String getInput() {
		return inputFile;
	}


	/**
	 * Returns the output filename.
	 * @return the output filename
	 */
	protected String getOutput() {
		return outputFile;
	}
	
	
	/** 
	 * Returns the value of a particular parameter
	 * @param param the parameter 
	 * @return the value associated with the parameter
	 */
	protected String getParam(String param){
		return params.get(param);
	}
	
	/** 
	 * Returns the int value of a particular parameter
	 * @param param the parameter 
	 * @return the value associated with the parameter
	 */
	protected int getIntParam(String param){
		return Integer.parseInt(params.get(param));
	}
	
	/** 
	 * Returns the double value of a particular parameter
	 * @param param the parameter 
	 * @return the value associated with the parameter
	 */
	protected double getDoubleParam(String param){
		return Double.parseDouble(params.get(param));
	}
	
	
	private void getParams(){
		OptionParser parser = new OptionParser();
		for (String p : params.keySet()) {
			parser.accepts(p).withRequiredArg();
		}
		OptionSet option = parser.parse(args);
		for (String p : params.keySet()) {
			if (option.has(p)) {
				params.put(p, (String) option.valueOf(p));
			} else {
				System.err.println(usage);
				System.exit(-1);
			}
		}
		if (params.containsKey(INPUT)) inputFile = params.get(INPUT);
		if (params.containsKey(OUTPUT)) outputFile = params.get(OUTPUT);
 		
	}
	
	/**
	 * Opens the file given in input, if the file ends with .gz, it uses
	 * a compressed reader. 
	 */
	protected void openInput() {
		br = IOUtils.getPlainOrCompressedUTF8Reader(inputFile);

	}

	/**
	 * Open the file given in output, if the file ends with .gz, it uses
	 * a compressed writer. 
	 */
	protected void openOutput() {
		bw = IOUtils.getPlainOrCompressedUTF8Writer(outputFile);
	}

	
	/**
	 * Reads the next line on the inputfile, if the file ends returns null.
	 * Terminates the cli if an error occurs. 
	 */
	protected String readLineFromInput(){
		if (br == null){
			logger.error("reading the input file");
			System.exit(-1);
		}		
		try {
			return br.readLine();
		} catch (IOException e) {
			logger.error("reading the input file ({})",e.toString());
			System.exit(-1);
		}
		return "";
	}

	/**
	 * Writes a line in the output file. 
	 * Terminates the cli if an error occurs.
	 */
	protected void writeLineInOutput(String line) {
		if (bw == null){
			 logger.error("writing the line {} in the outputFile (out is null) ",line);
			 System.exit(-1);
		}
		try {
			bw.write(line);
		
			bw.newLine();
		} catch (IOException e) {
			logger.error("writing the line {} in the outputFile ({}) ",line,e.toString());
			 System.exit(-1);
		}
	}
	/**
	 * Writes text in the output file. 
	 * Terminates the cli if an error occurs.
	 */
	protected void writeInOutput(String str)  {
		if (bw == null)
			logger.error("writing the string " + str
					+ " in the outputFile ");
	
		try {
			bw.write(str);
		} catch (IOException e) {
			logger.error("writing the file");
			System.exit(-1);
		}
	}

	/**
	 * Opens both input and output, terminates the cli if an error
	 * occurs.
	 */
	protected void openInputAndOutput()  {
		getInput();
		getOutput();
		
			openInput();
		
		openOutput();
	}

	/**
	 * Closes the input file,  terminates the cli if an error
	 * occurs.
	 */
	protected void closeInput()  {
		try {
			br.close();
		} catch (IOException e) {
			logger.error("closing the input file ({})", e.toString());
			System.exit(-1);
		}
	}

	/**
	 * Closes the output file,  terminates the cli if an error
	 * occurs.
	 */
	protected void closeOutput()  {
		try {
			bw.close();
		} catch (IOException e) {
			logger.error("closing the output file ({})", e.toString());
			System.exit(-1);
		}
	}

	
	/**
	 * Closes both input and output file,  terminates the cli if an error
	 * occurs.
	 */
	protected void closeInputAndOuput()  {
		closeInput();
		closeOutput();
	}
	

	/**
	 * Returns the usage message.
	 * @return the usage message
	 */
	public String getUsage() {
		return usage;
	}

	/**
	 * Sets the usage message.
	 * @param usage the usage message
	 */
	public void setUsage(String usage) {
		this.usage = usage;
	}
}