package com.mycompany.myfcmapp;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FCMLineParserTest {

    @Test
    public void testReset() throws Exception
    {
        FCMLineParser parser = new FCMLineParser();

        // Use a subscriber line to initialise the parser.
        String testLine = "S 10.174.8.147:53074";
        parser.Parse(testLine);
        Assert.assertEquals('S', parser.GetLineType());
        Assert.assertEquals("10.174.8.147", parser.GetIPAddress());

        // Test the Reset method.
        parser.Reset();
        Assert.assertEquals(' ', parser.GetLineType());
        Assert.assertEquals("0.0.0.0", parser.GetIPAddress());
    }

    @Test
    public void testParse() throws Exception
    {
        FCMLineParser parser = new FCMLineParser();

        // Test subscriber line.
        String testLine = "S 10.174.8.147:53074";
        parser.Parse(testLine);
        Assert.assertEquals('S', parser.GetLineType());
        Assert.assertEquals("10.174.8.147", parser.GetIPAddress());

        // Test publisher line.
        testLine = "P 10.30.66.45:34053";
        parser.Parse(testLine);
        Assert.assertEquals('P', parser.GetLineType());
        Assert.assertEquals("10.30.66.45", parser.GetIPAddress());

        // Test case where this is not a publisher line.
        testLine = "BP <LZPROD> /lz/sentinel_position/realtime/cashflow ...";
        parser.Parse(testLine);
        Assert.assertEquals(' ', parser.GetLineType());
        Assert.assertEquals("0.0.0.0", parser.GetIPAddress());
    }

    @Test
    public void testGetLineType() throws Exception
    {
        // Tests covered in the testParse method.
    }

    @Test
    public void testGetIPAddress() throws Exception
    {
        // Tests covered in the testParse method.
    }
}