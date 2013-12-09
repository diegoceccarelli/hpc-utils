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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 * Created on Nov 19, 2012
 */
public class ThreadPoolTest {

	@Test
	public void testDifferentFiles() {
		ThreadPool tp = new ThreadPool(10, "./src/test/resources/parallel/input-file.txt",new Lowercase());
		tp.setThreadSleepTime(200);
		tp.run();
		
	}
	
	@Test
	public void test() {
		ThreadPool tp = new ThreadPool(10, "./src/test/resources/parallel/input-file.txt",new Lowercase());
		tp.useOnlyOneOutputFile();
		tp.setThreadSleepTime(200);
		tp.run();
		
	}
	
	
    public class Lowercase implements FunctionToExec {

		/* (non-Javadoc)
		 * @see it.cnr.isti.hpc.parallel.FunctionToExec#run(java.lang.String)
		 */
		@Override
		public StringBuilder run(String input) {
			return new StringBuilder(input.toLowerCase());
		}
		
	}

}
