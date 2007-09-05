/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.ocm.annotation.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.ocm.RepositoryLifecycleTestSetup;
import org.apache.jackrabbit.ocm.TestBase;
import org.apache.jackrabbit.ocm.annotation.model.unstructured.A;
import org.apache.jackrabbit.ocm.annotation.model.unstructured.Atomic;
import org.apache.jackrabbit.ocm.annotation.model.unstructured.B;
import org.apache.jackrabbit.ocm.annotation.model.unstructured.C;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;

/**
 * Test JcrSession
 *
 * @author <a href="mailto:christophe.lombart@sword-technologies.com">Christophe Lombart</a>
 */
public class ObjectContentManagerTest extends TestBase {
	private final static Log log = LogFactory.getLog(ObjectContentManagerTest.class);

	
	public void tearDown() throws Exception {

		cleanUpRepisotory();
		super.tearDown();

	}

    public static Test suite()
    {
        // All methods starting with "test" will be executed in the test suite.
        return new RepositoryLifecycleTestSetup(new TestSuite(ObjectContentManagerTest.class));
    }
    
	public ObjectContentManagerTest(String testName) {
		super(testName);
		
	}


	public void testClassA() {
		try {

			List<String> classNames = new ArrayList<String>();
			
			classNames.add(B.class.getName());
			classNames.add(C.class.getName());
			classNames.add(A.class.getName());

			ObjectContentManager ocm = this.initObjectContentManager(classNames);


			// --------------------------------------------------------------------------------
			// Create and store an object graph in the repository
			// --------------------------------------------------------------------------------
			A a = new A();
			a.setPath("/test");
			a.setA1("a1");
			a.setA2("a2");
			B b = new B();
			b.setB1("b1");
			b.setB2("b2");
			a.setB(b);

			C c1 = new C();
			c1.setId("first");
			c1.setName("First Element");
			C c2 = new C();
			c2.setId("second");
			c2.setName("Second Element");

			C c3 = new C();
			c3.setId("third");
			c3.setName("Third Element");

			Collection collection = new ArrayList();
			collection.add(c1);
			collection.add(c2);
			collection.add(c3);

			a.setCollection(collection);

			ocm.insert(a);
			ocm.save();

			// --------------------------------------------------------------------------------
			// Get the object
			// --------------------------------------------------------------------------------           
			a = (A) ocm.getObject("/test");
			assertNotNull("a is null", a);
			assertTrue("Incorrect a1", a.getA1().equals("a1"));
			assertNotNull("a.b is null", a.getB());
			assertTrue("Incorrect a.b.b1", a.getB().getB1().equals("b1"));
			assertNotNull("a.collection is null", a.getCollection());
			assertTrue("Incorrect a.collection", ((C) a.getCollection().iterator().next()).getId().equals("first"));

			// --------------------------------------------------------------------------------
			// Update the object
			// --------------------------------------------------------------------------------
			a.setA1("new value");
			B newB = new B();
			newB.setB1("new B1");
			newB.setB2("new B2");
			a.setB(newB);

			ocm.update(a);
			ocm.save();

			// --------------------------------------------------------------------------------
			// Get the object
			// --------------------------------------------------------------------------------           
			a = (A) ocm.getObject("/test");
			assertNotNull("a is null", a);
			assertTrue("Incorrect a1", a.getA1().equals("new value"));
			assertNotNull("a.b is null", a.getB());
			assertTrue("Incorrect a.b.b1", a.getB().getB1().equals("new B1"));

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occurs during the unit test : " + e);
		}

	}

	public void testIsPersistent() {
		try {
			List<String> classNames = new ArrayList<String>();

			classNames.add(Atomic.class.getName());

			ObjectContentManager ocm = this.initObjectContentManager(classNames);


			assertTrue("Class A is not persistent ", ocm.isPersistent(Atomic.class));
			assertFalse("Class String is  persistent - hum ? ", ocm.isPersistent(String.class));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occurs during the unit test : " + e);
		}
	}

}