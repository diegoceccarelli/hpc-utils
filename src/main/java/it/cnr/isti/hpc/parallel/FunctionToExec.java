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


/**
 * The function that a thread pool has to exec. 
 * 
 * @see ThreadPool
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 * Created on Nov 19, 2012
 */
public interface FunctionToExec {
	
	/**
	 * The function that a thread in the thread pool has to exec. 
	 * Given a string in input the output must be returned a StringBuilder.
	 * @param input the data to process 
	 * @return the output string.
	 */
	public StringBuilder run(String input);

}
