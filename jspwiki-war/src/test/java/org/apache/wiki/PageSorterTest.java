/* 
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.  
 */

package org.apache.wiki;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.PropertyConfigurator;
import org.apache.wiki.api.exceptions.WikiException;
import org.apache.wiki.util.ClassUtil;
import org.apache.wiki.util.comparators.HumanComparator;
import org.apache.wiki.util.comparators.LocaleComparator;

/**
 * Wrapper class for managing and using the PageNameComparator.
 * <p>
 * <b>Note</b> - this class is deliberately not null safe. Never call any of the
 * methods with a null argument!
 */
public class PageSorterTest extends TestCase
{
    private Properties props = TestEngine.getTestProperties();

    private TestEngine engine;

	public static Test suite()
    {
        return new TestSuite( PageSorterTest.class );
    }
    
    public void setUp() throws Exception
    {
        PropertyConfigurator.configure(props);
        engine = new TestEngine(props);
    }


    public void testPageSorterBadProperty() throws WikiException
    {
        // Initialised with a broken property
        PageSorter sorter = ClassUtil.getInternalModule(PageSorter.class, engine, props);
        Properties props = new Properties();
        props.put( PageSorter.PROP_PAGE_NAME_COMPARATOR, "haha.this.isnt.a.class" );
        sorter.initialize( engine, props );
        assertTrue( sorter.compare( "ab2", "ab10" ) > 0 );
    }

    public void testPageSorterDefaultConstructor() throws WikiException
    {
        // Check uninitialised behaviour
        PageSorter sorter = ClassUtil.getInternalModule(PageSorter.class, engine, props);
        assertTrue( sorter.compare( "ab2", "ab10" ) > 0 );
    }

    public void testPageSorterHumanComparator() throws WikiException
    {
        // Initialised with the human comparator
        PageSorter sorter = ClassUtil.getInternalModule(PageSorter.class, engine, props);
        Properties props = new Properties();
        props.put( PageSorter.PROP_PAGE_NAME_COMPARATOR, HumanComparator.class.getPackage().getName() + ".HumanComparator" );
        sorter.initialize( engine, props );
        assertTrue( sorter.compare( "ab2", "ab10" ) < 0 );
        props.put( PageSorter.PROP_PAGE_NAME_COMPARATOR, "HumanComparator" );
        sorter.initialize( engine, props );
        assertTrue( sorter.compare( "ab2", "ab10" ) < 0 );
    }

    public void testPageSorterLocaleComparator() throws WikiException
    {
        // Initialised with the human comparator
        PageSorter sorter = ClassUtil.getInternalModule(PageSorter.class, engine, props);
        Properties props = new Properties();
        props.put( PageSorter.PROP_PAGE_NAME_COMPARATOR, LocaleComparator.class.getPackage().getName() + ".LocaleComparator" );
        sorter.initialize( engine, props );
        assertTrue( sorter.compare( "ab2", "ab10" ) > 0 );
        props.put( PageSorter.PROP_PAGE_NAME_COMPARATOR, "LocaleComparator" );
        sorter.initialize( engine, props );
        assertTrue( sorter.compare( "ab2", "ab10" ) > 0 );
    }

    public void testPageSorterNoProperty() throws WikiException
    {
        // Initialised without the necessary property
        PageSorter sorter = ClassUtil.getInternalModule(PageSorter.class, engine, props);
        Properties props = new Properties();
        sorter.initialize( engine, props );
        assertTrue( sorter.compare( "ab2", "ab10" ) > 0 );
    }
}
