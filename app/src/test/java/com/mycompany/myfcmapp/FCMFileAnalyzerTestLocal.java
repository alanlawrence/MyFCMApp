package com.mycompany.myfcmapp;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

// The vogella tutorial has these in the example but Android Studio is telling me they are
// unused.
//import org.junit.runner.RunWith;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;

public class FCMFileAnalyzerTestLocal {

    @Before
    public void setUp() throws Exception
    {
        // Nothing to do yet.
    }

    @After
    public void tearDown() throws Exception
    {
        // Nothing to do yet.
    }

    @Test
    public void testAnalyze1() throws Exception
    {
        // Create a mock ReadBuffer object.
        BufferedReader mockBufReader = Mockito.mock(BufferedReader.class);

        // TEST 1: Simple Publisher line.
        // Set how it should behave.
        Mockito.when(mockBufReader.readLine()).thenReturn
        (
                "P 10.30.75.11:55749\n",
                (String) null
        );

        // Automatic due to function prototype returning void, so no need to write this ...
        //Mockito.when(mockBufReader.close()).thenReturn(void);

        // Construct an FCMFileAnalyser with the mock ReadBuffer object.
        FCMFileAnalyzer analyzer = new FCMFileAnalyzer(mockBufReader);
        analyzer.Analyze();

        // Call StatsString to see if the output is correct.
        // First build expected response.
        String statsString = "Start stats ...\n";
        statsString += "#Lines = 1\n";
        statsString += "#Pub lines = 1\n#Sub lines = 0\n";
        statsString += "#Other lines = 0\n";
        statsString += "#Pub ip addresses = 1\n";
        statsString += "#Sub ip addresses = 0\n";

        statsString += "\nPub ip addresses with occurrence count:\n";
        statsString += "{10.30.75.11=1}";
        statsString += "\nSub ip addresses with occurrence count:\n";
        statsString += "{}";
        statsString += "\n... end of stats.\n";

        Assert.assertEquals(statsString, analyzer.StatsString());
    }

    @Test
    public void testAnalyze2() throws Exception
    {
        // Create a mock ReadBuffer object.
        BufferedReader mockBufReader = Mockito.mock(BufferedReader.class);

        // TEST 2: Standard header but omitting broadcast publisher and listener lines.
        //         Hit MAX_LINES by not returning null. last entry will be returned
        //         for every readLine() call after that one.
        Mockito.when(mockBufReader.readLine()).thenReturn
                (
                        "Listening for events\n",
                        "Connected to service [ROUTER_IN_ROOT_A].\n",
                        "!!!BROADCAST LISTENER\n",
                        "!!!End BROADCAST LISTENER\n",
                        "!!!BROADCAST LISTENER\n",
                        "P 10.30.75.11:55749\n"  /* This return value will be repeated. */
                );

        // Construct an FCMFileAnalyser with the mock ReadBuffer object.
        FCMFileAnalyzer analyzer = new FCMFileAnalyzer(mockBufReader);
        analyzer.Analyze();

        // Call StatsString to see if the output is correct.
        // First build expected response.
        String statsString = "Start stats ...\n";
        statsString += String.format("#Lines = %d (= MAX LINES)\n", analyzer.MaxLines());
        statsString += String.format("#Pub lines = %d\n#Sub lines = 0\n", analyzer.MaxLines() - 5);
        statsString += String.format("#Other lines = %d\n", 5);
        statsString += String.format("#Pub ip addresses = %d\n", 1);
        statsString += "#Sub ip addresses = 0\n";

        statsString += "\nPub ip addresses with occurrence count:\n";
        statsString += String.format("{10.30.75.11=%d}", analyzer.MaxLines() - 5);
        statsString += "\nSub ip addresses with occurrence count:\n";
        statsString += "{}";
        statsString += "\n... end of stats.\n";

        Assert.assertEquals(statsString, analyzer.StatsString());
    }

    // Test with Subscriber lines too.
    @Test
    public void testAnalyze3() throws Exception
    {
        // Create a mock ReadBuffer object.
        BufferedReader mockBufReader = Mockito.mock(BufferedReader.class);

        // TEST 1: Simple Publisher line.
        // Set how it should behave.
        Mockito.when(mockBufReader.readLine()).thenReturn
                (
                        "P 10.30.75.11:55749\n",
                        "S 10.31.76.12:55750\n",
                        null
                );

        // Automatic due to function prototype returning void, so no need to write this ...
        //Mockito.when(mockBufReader.close()).thenReturn(void);

        // Construct an FCMFileAnalyser with the mock ReadBuffer object.
        FCMFileAnalyzer analyzer = new FCMFileAnalyzer(mockBufReader);
        boolean ok = analyzer.Analyze();
        Assert.assertEquals(true, ok);

        // Call StatsString to see if the output is correct.
        // First build expected response.
        String statsString = "Start stats ...\n";
        statsString += "#Lines = 2\n";
        statsString += "#Pub lines = 1\n#Sub lines = 1\n";
        statsString += "#Other lines = 0\n";
        statsString += "#Pub ip addresses = 1\n";
        statsString += "#Sub ip addresses = 1\n";

        statsString += "\nPub ip addresses with occurrence count:\n";
        statsString += "{10.30.75.11=1}";
        statsString += "\nSub ip addresses with occurrence count:\n";
        statsString += "{10.31.76.12=1}";
        statsString += "\n... end of stats.\n";

        Assert.assertEquals(statsString, analyzer.StatsString());
    }

    @Test
    public void testRepeatedPubSingleSubs() throws Exception
    {
        // Create a mock ReadBuffer object.
        BufferedReader mockBufReader = Mockito.mock(BufferedReader.class);

        // TEST: Repeat the same IP address multiple times for Pub lines.
        //       List different IP addresses for each Sub line.
        Mockito.when(mockBufReader.readLine()).thenReturn
                (
                        "P 10.30.75.11:55749\n",
                        "P 10.30.75.11:55750\n",
                        "P 10.30.75.11:55751\n",
                        "P 10.30.75.11:55752\n",
                        "P 10.30.75.11:55753\n",
                        "S 10.31.76.12:55750\n",
                        "S 10.31.76.13:55750\n",
                        "S 10.31.76.14:55750\n",
                        "S 10.31.76.15:55750\n",
                        "S 10.31.76.16:55750\n",
                        "S 10.31.76.17:55750\n",
                        null
                );

        // Automatic due to function prototype returning void, so no need to write this ...
        //Mockito.when(mockBufReader.close()).thenReturn(void);

        // Construct an FCMFileAnalyser with the mock ReadBuffer object.
        FCMFileAnalyzer analyzer = new FCMFileAnalyzer(mockBufReader);
        boolean ok = analyzer.Analyze();
        Assert.assertEquals(true, ok);

        // Call StatsString to see if the output is correct.
        // First build expected response.
        String statsString = "Start stats ...\n";
        statsString += "#Lines = 11\n";
        statsString += "#Pub lines = 5\n#Sub lines = 6\n";
        statsString += "#Other lines = 0\n";
        statsString += "#Pub ip addresses = 1\n";
        statsString += "#Sub ip addresses = 6\n";

        statsString += "\nPub ip addresses with occurrence count:\n";
        statsString += "{10.30.75.11=5}";
        statsString += "\nSub ip addresses with occurrence count:\n";
        statsString += "{10.31.76.12=1, 10.31.76.13=1, 10.31.76.14=1, 10.31.76.15=1, ";
        statsString +=  "10.31.76.16=1, 10.31.76.17=1}";
        statsString += "\n... end of stats.\n";

        Assert.assertEquals(statsString, analyzer.StatsString());
    }


    // Test the output when an exception is thrown on the first readLine call.
    @Test
    public void testForIOException() throws Exception
    {
        // Create a mock ReadBuffer object.
        BufferedReader mockBufReader = Mockito.mock(BufferedReader.class);

        String errorMsg = "Mocked IOException";
        Mockito.doThrow(new IOException(errorMsg)).when(mockBufReader).readLine();

        // Construct an FCMFileAnalyser with the mock ReadBuffer object.
        FCMFileAnalyzer analyzer = new FCMFileAnalyzer(mockBufReader);
        boolean ok = analyzer.Analyze();
        Assert.assertEquals(false, ok);

        // Call StatsString to see if the output is correct.
        // First build expected response.
        String statsString = "Error message: [IOException[" + errorMsg + "]. ]\n";

        statsString += "Start stats ...\n";
        statsString += "#Lines = 0\n";
        statsString += "#Pub lines = 0\n#Sub lines = 0\n";
        statsString += "#Other lines = 0\n";
        statsString += "#Pub ip addresses = 0\n";
        statsString += "#Sub ip addresses = 0\n";

        statsString += "\nPub ip addresses with occurrence count:\n";
        statsString += "{}";
        statsString += "\nSub ip addresses with occurrence count:\n";
        statsString += "{}";
        statsString += "\n... end of stats.\n";

        String analyzerStatsString = analyzer.StatsString();
        Assert.assertEquals(statsString, analyzerStatsString);
    }


    @Test
    public void testStatsString() throws Exception
    {
        // Tested in testAnalyze().
    }
}