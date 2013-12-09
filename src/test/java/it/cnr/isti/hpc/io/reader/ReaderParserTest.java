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

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 * Created on Dec 19, 2012
 */
public class ReaderParserTest {

	@Test
	public void test() {
		JsonRecordParser<Person> parser = new JsonRecordParser<Person>(Person.class);
		Person diego = new Person("diego",28);
		Person salvo = new Person("salvo",29);
		Person cris = new Person("cris",27);
		System.out.println(parser.encode(diego));
		System.out.println(parser.encode(salvo));
		System.out.println(parser.encode(cris));
		RecordReader<Person> reader = new RecordReader<ReaderParserTest.Person>("./src/test/resources/reader/persons", parser);
		Iterator<Person> it = reader.iterator();
		assertTrue(it.next().getName().equals("diego"));
		assertTrue(it.next().getName().equals("salvo"));
		assertTrue(it.next().getName().equals("cris"));
	}
	
	class Person {
		String name;
		int age;
		
		
		public Person(String name, int age) {
			super();
			this.name = name;
			this.age = age;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the age
		 */
		public int getAge() {
			return age;
		}
		/**
		 * @param age the age to set
		 */
		public void setAge(int age) {
			this.age = age;
		}
		
		
	}
	

}
