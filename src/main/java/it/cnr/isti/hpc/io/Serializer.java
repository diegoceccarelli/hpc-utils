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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facilities for serializing and deserializing objects on/from files. 
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Sep 10, 2012
 */
public class Serializer {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Serializer.class);
	
	
	/***
	 * serializes an object (serializable)  on a file 
	 * 
	 * <br/>
	 * It terminates the execution in case of any type of error.
	 * 
	 * @param obj the object to serialize
	 * @param file the file where to write the serialization
	 */
	public void dump(Object obj, String file) {
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out;
			out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			logger.error("serializing {} object ({})", obj.getClass(),
					e.toString());
			System.exit(-1);
		}
	}

	/**
	 * deserialize an object from a file
	 * 
	 * @param file the file containing the serialized version of the object
	 * @return the deserialized version of the object
	 */
	public Object load(String file) {
		Object obj = null;
		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			try {
				obj = in.readObject();
			} catch (ClassNotFoundException e) {
				logger.error("deserializing object ({})", e.toString());
				System.exit(-1);
			}
			in.close();
			fileIn.close();
		} catch (IOException e) {
			logger.error("deserializing object ({})", e.toString());
			System.exit(-1);
		}

		return obj;

	}
}
