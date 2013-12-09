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



import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.isti.hpc.io.IOUtils;

/**
 * A RecordReader abstracts a collection of objects on a file. 
 * Each line of a file represents a serialized object of the collection. 
 * {@link RecordReader} implements the policy for deserializing each 
 * line of the file. 
 *  
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 */
public class RecordReader<E> implements Iterable<E> {

	private static final Logger logger = LoggerFactory
			.getLogger(RecordReader.class);

	private String input;
	private RecordParser<E> parser;
	private List<Filter<E>> filters;

	/**
	 * Creates the record reader, it will be possible to iterate on the object
	 * serialized on the file inputFile.
	 * @param inputFile the file containing the serialized object 
	 * (one per line). If the file ends with .gz, a compressed input reader will be used. 
	 * @param parser the object parser, in order to deserialize each line in a object.
	 */
	public RecordReader(String inputFile, RecordParser<E> parser) {
		input = inputFile;
		this.parser = parser;
		filters = new ArrayList<Filter<E>>();
	}
	
	/**
	 * Creates the record reader, it will be possible to iterate on the object
	 * serialized on the file inputFile. It will assume that objects in the file
	 *  are encoded in json. 
	 * 
	 * @param inputFile the file containing the serialized object (one per line). 
	 * If the file ends with .gz, a compressed input reader will be used. 
	 * @param clazz the class of the object to parse.
	 */
	public RecordReader(String inputFile, Class<E> clazz) {
		input = inputFile;
		this.parser = new JsonRecordParser<E>(clazz);
		filters = new ArrayList<Filter<E>>();
	}
	
	/**
	 * Add one {@link Filter} or more to the reader, only objects that do not
	 * match any filter will be returned.
	 * @param filters the list of filter to match against each object.
	 * @return this record reader.
	 */
	public RecordReader<E> filter(Filter<E>... filters) {
		for (Filter<E> f : filters)
			this.filters.add(f);
		return this;
	}

	public Iterator<E> iterator() {
		return new BaseItemIterator();
	}

	private class BaseItemIterator implements Iterator<E> {

		private BufferedReader br;
		E next = null;
		private boolean found = false;
		private boolean eof = false;

		public BaseItemIterator() {
			br = IOUtils.getPlainOrCompressedUTF8Reader(input.toString());
			while ((!found) && (!eof)) {
				next = parseNextItem();
			}

		}

		private E parseNextItem() {
			String nextLine = "";
			E t = null;
			do {
				try {
					nextLine = br.readLine();
					if (nextLine == null) {
						eof = true;
						return null;
					}
				} catch (IOException e) {
					logger.error("reading from the file {} ({})", input,
							e.toString());
					System.exit(-1);
				}
				if (!eof) {
					try {
						t = parser.decode(nextLine);
						found = true;

					} catch (Exception e) {
						logger.warn("Skipping invalid query result ({})",
								e.toString());
						logger.error(nextLine);
						found = false;
						return null;
					}

				}
			} while (isFilter(t));

			return t;

		}

		/**
		 * @param t
		 * @return
		 */
		private boolean isFilter(E t) {

			for (Filter<E> f : filters) {
				if (f.isFilter(t))
					return true;
			}
			return false;
		}

		public boolean hasNext() {
			return ((!eof) && (found));
		}

		public E next() {
			E toReturn = next;
			found = false;
			while ((!found) && (!eof)) {
				next = parseNextItem();
			}
			return toReturn;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
