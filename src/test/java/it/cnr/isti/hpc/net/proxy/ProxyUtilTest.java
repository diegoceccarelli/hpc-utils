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

import it.cnr.isti.hpc.net.FakeBrowser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.junit.Test;

/**
 * ProxyUtilTest.java
 *
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it
 * created on 08/giu/2012
 */
public class ProxyUtilTest {

	@Test
	public void test() throws IOException {
		ProxyUtil util = new ProxyUtil();
		Reader r = util.fetch(new URL("http://www.di.unipi.it/~ceccarel"));
		BufferedReader br = new BufferedReader(r);
		System.out.println(br.readLine());
	}
	
	@Test
	public void FakeUserAgentTest() throws IOException {
		ProxyUtil util = new ProxyUtil();
		FakeBrowser userAgent = new FakeBrowser();
		Reader r = util.fetch(new URL("http://www.di.unipi.it/~ceccarel"));
		BufferedReader br = new BufferedReader(r);
		System.out.println(br.readLine());
	}

}
