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
package it.cnr.isti.hpc.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A progress logger, print a configurable message when a the method 
 * <pre>up()</pre> is called a number k of times, so when:
 * <br/> <br/>
 * <code>
 * 	status % k == 0
 * </code>
 * 
 *
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it
 * created on 19/lug/2012
 */
public class ProgressLogger {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProgressLogger.class);
	
	int logFrequency = -1;
	int status = 0;
	
	private String logDesc = "{}";
	
	/**
	 * Creates a progress logger, the print frequency is initialized using the system property <code>logat</code>. If logat
	 * is not set no progress will be print.
	 */
	public ProgressLogger(){
		String property = System.getProperty("logat");
		if (property == null ) {
			logger.warn("no log frequency for the ProgressLogger, it will not show updates \n(use -Dlogat=1000 to print an update every 1000 lines)");
			return;
		}
		try {
			logFrequency = Integer.parseInt(property);
		} catch (NumberFormatException e){
			logger.warn("error reading the log frequency for the ProgressLogger, it will not show updates \n(use -Dlogat=1000 to print an update every 1000 lines)");
		}
	}
	
	/**
	 * Creates a progress logger.
	 * @param logFrequency the print frequency. 
	 */
	public ProgressLogger(int logFrequency){
		this.logFrequency = logFrequency;
	}
	
	
	/**
	 * Creates a progress logger.
	 * @param logDesc the message to print, the string <code>{}</code> will be 
	 * replaced with the number of times that <code>up()</code> has been called.
	 * @param logFrequency the print frequency. 
	 */
	public ProgressLogger(String logDesc, int logFrequency){
		this(logFrequency);
		this.logDesc = logDesc;
	}
	
	/**Creates a progress logger. the print frequency is initialized using the system property <code>logat</code>. If logat
	 * is not set no progress will be print.
	 * @param logDesc the message to print, the string <code>{}</code> will be 
	 * replaced with the number of times that <code>up()</code> has been called.
	 */
	public ProgressLogger(String logDesc){
		this();
		this.logDesc = logDesc;
	}
	
	/**
	 * Updates the progress status and print a line base on logFrequency;
	 * @return the number of times up() has been called;
	 */
	public int up(){
		if (logFrequency <= 0) return -1; 
		if (++status % logFrequency == 0) {
			logger.info(logDesc, status);
		}
		return status;
	}
	
	
	
	

}
