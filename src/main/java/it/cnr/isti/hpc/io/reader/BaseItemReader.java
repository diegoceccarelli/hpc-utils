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

import it.cnr.isti.hpc.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <strong> DEPRECATED </strong> see RecordReader class
 * @see  RecordReader
 * 
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 25/gen/2012
 */
@Deprecated
public class BaseItemReader<T extends Item> implements ItemReader<T> {
	private static final Logger logger = LoggerFactory
			.getLogger(BaseItemReader.class);

	private BufferedReader br;
	private StringBuilder sb;
	private String input;
	private T typeInstance;
	List<Filter<T>> filters;

	public BaseItemReader(String inputFile, T typeInstance) {
		input = inputFile;
		this.typeInstance = typeInstance;
		filters = new ArrayList<Filter<T>>();
	}

	@Override
	public ItemReader<T> filter(Filter<T> ... filters) {
		for (Filter<T> f : filters)
			this.filters.add(f);
		return this;
	}

	public Iterator<T> iterator() {
		return new BaseItemIterator<T>();
	}

	private class BaseItemIterator<T extends Item> implements Iterator<T> {

		private BufferedReader br;
		T next = null;
		private boolean found = false;
		private boolean eof = false;

		public BaseItemIterator() {
			br = IOUtils.getPlainOrCompressedUTF8Reader(input.toString());
			while ((!found) && (!eof)) {
				next = parseNextItem();
			}

		}

		private T parseNextItem() {
			String nextLine = "";
			T t = null;
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
						t = (T) (typeInstance.parse(nextLine));
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
		private boolean isFilter(T t) {
			
			for (Filter f : filters){
				if (f.isFilter(t)) return true;
			}
			return false;
		}

		public boolean hasNext() {
			return ((!eof) && (found));
		}

		public T next() {
			T toReturn = next;
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
