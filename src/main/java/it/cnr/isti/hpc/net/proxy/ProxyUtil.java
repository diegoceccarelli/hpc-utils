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
package it.cnr.isti.hpc.net.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * Fetches webpages using a list of proxies.
 * 
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 08/giu/2012
 */
public class ProxyUtil {
	

	private ProxyList proxies = ProxyList.getInstance();

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
		Proxy proxy = proxies.getProxy();
		BufferedReader br = null;

		URLConnection uc = url.openConnection(proxy);
		uc.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.1.11) Gecko/20071127 Firefox/2.0.0.11");
		// uc.connect();
		br = new BufferedReader(new InputStreamReader(uc.getInputStream()));

		return br;
	}
}
