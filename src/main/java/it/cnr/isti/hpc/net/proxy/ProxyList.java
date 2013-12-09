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

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A proxy list, useful to run queries against websites and avoid
 * to be blacklisted. 
 * 
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 08/giu/2012
 */
public class ProxyList {
	//TODO add a blacklist and a resource file with all the 
	// proxies
	
	private static Type proxyType = Type.HTTP;
	private List<Proxy> proxies = new ArrayList<Proxy>();
	private int currentProxy = 0;
	
	public static ProxyList instance;
	
	
	private ProxyList() {
		// only one proxy FIXME
		addProxy("122.141.242.199",80);
	}

	/**
	 * Returns the next proxy.
	 * @return the next proxy.
	 */
	public Proxy getProxy() {
		Proxy p = proxies.get(currentProxy);
		currentProxy = (currentProxy + 1) % proxies.size();
		return p;
	}

	/**
	 * Add a proxy.
	 * @param address the proxy address.
	 * @param port the proxy port.
	 */
	public void addProxy(String address, int port) {
		Proxy p = new Proxy(proxyType, new InetSocketAddress(address, port));
		proxies.add(p);
	}

	/**
	 * Add a proxy.
	 * @param address the proxy address.
	 * @param port the proxy port.
	 * @param proxyType the type of the proxy. 
	 */
	public void addProxy(String address, int port, Type proxyType) {
		Proxy p = new Proxy(proxyType, new InetSocketAddress(address, port));
		proxies.add(p);
	}
	
	
	/**
	 * Returns an instance of proxylist. 
	 * @return an instance of proxylist.
	 */
	public static ProxyList getInstance(){
		if (instance == null) instance = new ProxyList();
		return instance;
	}

}
