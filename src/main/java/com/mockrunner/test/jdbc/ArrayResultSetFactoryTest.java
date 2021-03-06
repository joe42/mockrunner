package com.mockrunner.test.jdbc;

import java.util.Arrays;

import junit.framework.TestCase;

import com.mockrunner.jdbc.ArrayResultSetFactory;
import com.mockrunner.jdbc.StringValuesTable;
import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Exercises the published contract for <code>ArrayResultSetFactory</code> 
 * instances.
 * 
 * @author Erick G. Reid
 */
public class ArrayResultSetFactoryTest extends TestCase 
{
    private ArrayResultSetFactory arrayResultSetFactory;
    private StringValuesTable stringTable;
    private String[] columnNames;
    private String[][] stringMatrix;

    /**
     * Set up the test fixture.
     */
    public void setUp() throws Exception
    {
        super.setUp();
        columnNames = new String[] { "id", "name", "address" };
        stringMatrix = new String[][] { new String[] { "1", "moe", "123 main" }, new String[] { "2", "larry", "123 main" }, new String[] { "3", "curly", "123 main" }, };
        stringTable = new StringValuesTable("table", columnNames, stringMatrix);
        arrayResultSetFactory = new ArrayResultSetFactory(columnNames,  stringMatrix);
    }

    /**
     * Tear down the test fixture.
     */
    public void tearDown() throws Exception
    {
        super.tearDown();
        arrayResultSetFactory = null;
    }

    /**
     * Ensures that the contructors published for
     * <code>ArrayResultSetFactory</code> fulfill their contract.
     * 
     * @throws Exception is an error occurs during testing.
     */
    public void testConstructors() throws Exception
    {
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory((StringValuesTable) null);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory((String[][]) null);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(null, null);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(columnNames, null);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(new String[] { "", "", null }, stringMatrix);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(columnNames, new String[][] { null, new String[] { "", "", "" } });
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(columnNames, new String[][] { new String[] { "", null, "" } });
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(columnNames, new String[][] { new String[] { "", "" }, new String[] { "", "", "" } });
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(columnNames, new String[][] { new String[] { "", "", "" }, new String[] { "", "" } });
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        arrayResultSetFactory = new ArrayResultSetFactory(stringTable);
        arrayResultSetFactory = new ArrayResultSetFactory(stringMatrix);
        arrayResultSetFactory = new ArrayResultSetFactory(columnNames, stringMatrix);
    }

    /**
     * Ensures that <code>MockResultSet create(id)</code> fulfills its published contract.
     *
     * @throws Exception is an error occurs during testing.
     */
    public void testCreate() throws Exception
    {
        MockResultSet mockResultSet = null;
        try
        {
            mockResultSet = arrayResultSetFactory.create(null);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        mockResultSet = arrayResultSetFactory.create("");
        assertEquals(columnNames.length, mockResultSet.getColumnCount());
        for (int ii = 0; ii < columnNames.length; ii++)
        {
            assertNotNull(mockResultSet.getColumn(columnNames[ii]));
        }
        for (int ii = 0; ii < stringMatrix.length; ii++)
        {
            assertEquals(Arrays.asList(stringMatrix[ii]), mockResultSet.getRow(ii + 1));
        }
    }
}

