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
package it.cnr.isti.hpc.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProjectProperties wraps project properties to open the file and return
 * properties. It first look in System property for "conf" variable, then it
 * looks for FILE_PROPERTY in the current directory. You can also open them
 * giving a Reader or a file name.
 * 
 * 
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 02/lug/2012
 */
public class ProjectProperties {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ProjectProperties.class);
	private static Properties properties = new Properties();
	private static boolean loaded = false;

	/**
	 * The default property file
	 * 
	 **/
	public final static String DEFAULT_NAME = "project.properties";
	/**
	 * System property
	 */
	public final static String SYSTEM_PROPERTY = "conf";

	/**
	 * Loads the properties
	 * 
	 * @param file
	 *            the property file
	 */
	public ProjectProperties(String file) {
		this(new File(file));
	}

	/**
	 * Loads the properties from the resources.
	 * 
	 * @param clazz
	 *            the current class
	 */
	public ProjectProperties(Class clazz) {

		this(clazz.getResourceAsStream("/" + DEFAULT_NAME));
	}

	/**
	 * Loads the properties
	 * 
	 * @param file
	 *            the property file
	 */
	public ProjectProperties(File file) {
		load(file);
	}

	/**
	 * Gets a property
	 * 
	 * @param key
	 *            the property key.
	 */
	public String get(String key) {
		String value = properties.getProperty(key);
		logger.debug("load property {} = {}", key, value);
		return value;
	}

	/**
	 * Gets a int property
	 * 
	 * @param key
	 *            the property key.
	 */
	public int getInt(String key) {
		String value = properties.getProperty(key);
		logger.debug("load property {} = {}", key, value);
		return Integer.parseInt(value);
	}

	/**
	 * Gets a int property
	 * 
	 * @param key
	 *            the property key.
	 */
	public double getDouble(String key) {
		String value = properties.getProperty(key);
		logger.debug("load property {} = {}", key, value);
		return Double.parseDouble(value);
	}

	/**
	 * Gets a boolean property
	 * 
	 * @param key
	 *            the property key.
	 */
	public boolean is(String key) {
		String value = properties.getProperty(key).toLowerCase();
		if (value.contains("true"))
			return true;
		return false;
	}

	/**
	 * Checks if the a property is defined
	 * 
	 * @param key
	 *            the property to check
	 * @return true if the property is defined, false otherwise
	 */
	public boolean has(String key) {
		return properties.containsKey(key);
	}

	/**
	 * Sets a property
	 * 
	 * @param key
	 *            the property name
	 * @param value
	 *            the property value
	 */
	public void set(String key, String value) {
		properties.setProperty(key, value);
	}

	/**
	 * Loads the properties
	 * 
	 * @param is
	 *            the property file
	 */
	public ProjectProperties(InputStream is) {
		if (loaded) {
			try {
				is.close();
			} catch (IOException e) {
				logger.error("opening properties {}", e.toString());
				System.exit(-1);
			}
			return;
		}
		String file = System.getProperty(SYSTEM_PROPERTY);
		if (file == null) {
			file = "./" + DEFAULT_NAME;
		}
		File f = new File(file);

		if (f.exists()) {
			logger.info("using property file in {}", f.getPath());
			load(f);
			return;
		} else {
			logger.warn("cannot find file {}", f.getAbsoluteFile());
		}
		logger.info("using default property file ");
		load(is);
	}

	/**
	 * Loads the properties
	 * 
	 * @param reader
	 *            the property file
	 */
	public ProjectProperties(Reader reader) {
		load(reader);
	}

	/**
	 * Loads the properties
	 * 
	 * @param file
	 *            the property file
	 */
	private void load(File file) {
		if (!file.exists()) {
			logger.error("cannot find property file {}", file.getAbsolutePath());
			System.exit(1);
		}
		try {
			logger.debug("load property file {}", file);
			load(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e1) {
			logger.warn("cannot load properties ({})", e1.toString());
			System.exit(1);
		}

	}

	/**
	 * Loads the properties
	 * 
	 * @param is
	 *            the property file
	 */
	private void load(InputStream is) {
		load(new InputStreamReader(is));
	}

	/**
	 * Loads the properties
	 * 
	 * @param reader
	 *            the property file
	 */
	private void load(Reader reader) {

		loaded = true;
		try {
			properties.load(reader);
		} catch (IOException e) {
			logger.warn("cannot load properties ({})", e.toString());
			System.exit(1);
		}
	}

}
