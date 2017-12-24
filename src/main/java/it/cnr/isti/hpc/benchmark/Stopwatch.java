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
package it.cnr.isti.hpc.benchmark;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A pool of stopwatches, useful for benchmarking code. 
 * Once you create an instance, you can start a stopwatch using 
 * <pre>
 * 
 * instance.start("stopwatch-name")
 * 
 * </pre>
 * where ''stopwatch-name'' is the symbolic name of a stopwatch. 
 * you can stop it using:
 * <pre>
 * 
 * instance.stop("stopwatch-name")
 * 
 * </pre>
 * and get the benchmarks using the method stat.
 * Please note that you can use start and stop multiple times,
 * stat will provide you the average time and the total time. 
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Sep 7, 2012
 */
public class Stopwatch {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Stopwatch.class);

	public long totalStopwatchesTime = 1;

	List<SingleStopwatch> stopwatches;

	public Stopwatch() {
		stopwatches = new ArrayList<SingleStopwatch>();
	}

	/**
	 * Starts a stopwatch with symbolic name 'name' 
	 * @param name the symbolic name of the stopwatch
	 */
	public void start(String name) {
		SingleStopwatch ss = new SingleStopwatch(name);
		int i = stopwatches.indexOf(ss);
		if (i == -1) {
			stopwatches.add(ss);
			ss.start();
		} else {
			stopwatches.get(i).start();
		}
	}

	/**
	 * Stops a stopwatch with symbolic name 'name' 
	 * @param name the symbolic name of the stopwatch
	 */
	public long stop(String name) {
		SingleStopwatch ss = new SingleStopwatch(name);
		int i = stopwatches.indexOf(ss);
		if (i == -1) {
			logger.warn("no stopwatch with name {}", name);
			return 0;
		}
		return stopwatches.get(i).stop();
	}

	
	/**
	 * Returns a string containing the benchmark for the stopwatch 'name'
	 * @param name the symbolic name of the stopwatch
	 * @return a string with the benchmark for the stopwatch 'name'
	 */
	public String stat(String name) {
		SingleStopwatch ss = new SingleStopwatch(name);
		int i = stopwatches.indexOf(ss);
		if (i == -1) {
			logger.warn("no stopwatch with name {}", name);
			return "";
		}
		return stopwatches.get(i).stat();
	}

	
	/**
	 * Returns a string with all the benchmarks for all the stopwatches started
	 * @return a string with the benchmarks for all the stopwatches
	 */
	public String stat() {
		StringBuilder sb = new StringBuilder();
		for (SingleStopwatch ss : stopwatches) {
			sb.append(ss.stat()).append('\n');
		}
		return sb.toString();
	}

	
	
	/**
	 * A single stopwatch.
	 * 
	 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
	 *
	 */
	public class SingleStopwatch {
		String name;
		private long total = 0;
		private long start = 0;
		private long end = 0;
		boolean running = false;
		int steps = 0;

		public SingleStopwatch(String name) {
			this.name = name;
		}

		/** 
		 * Starts the stopwatch
		 */
		public void start() {
			if (running) {
				logger.warn("there is a {}-stopwatch running! ", name);
				return;
			}
			start = System.currentTimeMillis();
			running = true;
		}

		
		/** 
		 * Stops the stopwatch
		 */
		public long stop() {
			if (! running) {
				logger.warn("{}-stopwatch is not running! ", name);
				return 0;
			}
			end = System.currentTimeMillis();
			long time = end - start;
			total += time;
			totalStopwatchesTime += time;
			steps++;
			running = false;
			return time;
		}

		/**
		 * Returns a string containing the stats for this stopwatch
		 * (name, total time, average time per call)
		 * @return a string containing the stats for this stopwatch
		 */
		public String stat() {
			StringBuilder sb = new StringBuilder();
			sb.append(name).append(" :");
			sb.append("time: ").append(total/1000).append(" sec.");
            
			sb.append(" (").append(total * 100 / (totalStopwatchesTime)).append("%")
					.append(")");
            if (steps > 0){
                sb.append("\t per-step: ").append(total / steps).append(" millis");
            }
			return sb.toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SingleStopwatch other = (SingleStopwatch) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private Stopwatch getOuterType() {
			return Stopwatch.this;
		}

	}
	
	public static void main(String args[] ) throws InterruptedException{
		Stopwatch s = new Stopwatch();
		s.start("test");
		s.start("test1");
		s.start("test2");
		Thread.sleep(1000);
		s.stop("test2");
		s.stop("test1");
		s.stop("test");
		s.start("test1");
		Thread.sleep(1000);
		
		s.stop("test1");
		
		System.out.println(s.stat());
	}
}
