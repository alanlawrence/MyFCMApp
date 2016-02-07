package com.mycompany.myfcmapp;

import junit.framework.Assert;
import junit.framework.TestCase;

// Work out how to rename this file and class to FCMIPAddressesTest.
public class FCMIPAddressTest extends TestCase
{

    public void testAddIPAddress() throws Exception
    {
        FCMIPAddresses ipAddresses = new FCMIPAddresses();
        String ip = "10.11.12.13";
        ipAddresses.AddIPAddress(ip);
        Assert.assertEquals(ipAddresses.Size(), 1);
        Assert.assertEquals(ipAddresses.CountIPAddress(ip), 1);

        ip = "10.14.15.16";
        ipAddresses.AddIPAddress(ip);
        Assert.assertEquals(ipAddresses.Size(), 2);
        Assert.assertEquals(ipAddresses.CountIPAddress(ip), 1);

        // Adding the same IP address. Should not increase the count
        // of addresses.
        ipAddresses.AddIPAddress(ip);
        Assert.assertEquals(ipAddresses.Size(), 2);
        // But the count should now be 2 ...
        Assert.assertEquals(ipAddresses.CountIPAddress(ip), 2);
    }

    public void testCountIPAddress() throws Exception
    {
        // Now test behaviour for an IP that is not present in the list

        FCMIPAddresses ipAddresses = new FCMIPAddresses();
        ipAddresses.AddIPAddress("10.20.21.22");
        String ip = "10.17.18.19";
        Assert.assertEquals(ipAddresses.CountIPAddress(ip), 0);
    }
}