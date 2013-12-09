/**
 *  Copyright 2013 Diego Ceccarelli
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
package it.cnr.isti.hpc.io.reader;

import java.util.HashMap;
import java.util.Map;

/**
 * A TsvTuple containing the fields parsed by a line encoded with tab separated
 * values.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Jan 24, 2013
 */
public class TsvTuple {

	Map<String, String> tuple;

	/**
	 * Creates a tuple object.
	 */
	public TsvTuple() {
		tuple = new HashMap<String, String>();
	}

	/**
	 * Gets a value.
	 * @param key the symbolic name.
	 * @return the value associated with the symbolic name.
	 */
	public String get(String key) {
		return tuple.get(key);
	}

	/**
	 * Gets an int value.
	 * @param key the symbolic name.
	 * @return the value associated with the symbolic name.
	 */
	public Integer getInt(String key) {
		return Integer.parseInt(tuple.get(key));
	}
	/**
	 * Set a value
	 * @param key the symbolic name.
	 * @param value associated with the symbolic name.
	 */
	public void put(String key, String value) {
		tuple.put(key, value);
	}

	/**
	 * Gets an double value.
	 * @param key the symbolic name.
	 * @return the value associated with the symbolic name.
	 */
	public Double getDouble(String key) {
		return Double.parseDouble(tuple.get(key));
	}

}
