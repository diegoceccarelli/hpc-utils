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
package it.cnr.isti.hpc.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Simulates a Firefox browser in order to send queries to google/wikipedia...
 * 
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 06/giu/2012
 */
public class FakeBrowser {

	private final Map<String, String> properties;

	public FakeBrowser() {
		properties = new HashMap<String, String>();
		properties
				.put("User-Agent",
						"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.1.11) Gecko/20071127 Firefox/2.0.0.11");
	}

	/**
	 * Fetches the url and returns a reader with the content of the page.
	 * 
	 * @param url
	 *            the url to fetch.
	 * @return a reader to the page of the url.
	 * @throws IOException
	 *             if an errors occurs during the fetching.
	 */
	public Reader fetch(URL url) throws IOException {
		URLConnection urlc;
		urlc = url.openConnection();
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			urlc.setRequestProperty(entry.getKey(), entry.getValue());
		}

		InputStream is = urlc.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return br;
	}

	/**
	 * Fetches the url and returns a reader with the content of the page.
	 * 
	 * @param url
	 *            the url to fetch.
	 * @return a reader to the page of the url.
	 * @throws IOException
	 *             if an errors occurs during the fetching.
	 */
	public Reader fetchUTF8(URL url) throws IOException {
		URLConnection urlc;
		urlc = url.openConnection();
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			urlc.setRequestProperty(entry.getKey(), entry.getValue());
		}

		InputStream is = urlc.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));
		return br;
	}

	/**
	 * Adds a property to the request (e.g., a cookie..)
	 * 
	 * @param name
	 *            the name fo the property
	 * @param value
	 *            the value of the property
	 */
	public void addProperty(String name, String value) {
		properties.put(name, value);
	}

	/**
	 * Fetches the url and returns a reader with the content of the page.
	 * 
	 * @param url
	 *            the url to fetch.
	 * @return a reader to the page of the url.
	 * @throws IOException
	 *             if an errors occurs during the fetching.
	 */
	public Reader fetch(String url) throws IOException {
		return fetch(new URL(url));
	}

	/**
	 * Fetches the url and returns a reader with the content of the page.
	 * 
	 * @param url
	 *            the url to fetch.
	 * @return a reader to the page of the url.
	 * @throws IOException
	 *             if an errors occurs during the fetching.
	 */
	public Reader fetchUTF8(String url) throws IOException {
		return fetch(new URL(url));
	}

	/**
	 * Fetches the url and returns a string with the content of the page.
	 * 
	 * @param url
	 *            the url to fetch.
	 * @return string containing the content of the page.
	 * @throws IOException
	 *             if an errors occurs during the fetching.
	 */
	public String fetchAsString(String url) throws IOException {
		return asString(fetch(url));
	}

	/**
	 * Fetches the url and returns a string with the content of the page.
	 * 
	 * @param url
	 *            the url to fetch.
	 * @return string containing the content of the page.
	 * @throws IOException
	 *             if an errors occurs during the fetching.
	 */
	public String fetchAsUTF8String(String url) throws IOException {
		return asString(fetchUTF8(url));
	}

	private String asString(Reader r) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(r);
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line).append(" ");
		}
		return sb.toString();

	}

}
