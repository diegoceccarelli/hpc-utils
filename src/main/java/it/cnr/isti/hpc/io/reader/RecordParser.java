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
package it.cnr.isti.hpc.io.reader;

/**
 * 
 * A RecordParser serializes and deserialize an object E to a string 
 * (without newlines). It is used in {@link RecordReader} to serialize 
 * and deserialize a collection of objects on a file (one line per object).  
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 * Created on Dec 19, 2012
 */
public interface RecordParser<E> {
	/**
	 * Deserializes the object from a string
	 * @param record the string containing the serialized object
	 * @return the object deserialized
	 */
	public E decode(String record);
	
	/**
	 * Serializes the object in a string (the string must not contain newlines!)
	 * @param obj the object to serialize
	 * @return the object serialized
	 */
	public String encode(E obj);
}
