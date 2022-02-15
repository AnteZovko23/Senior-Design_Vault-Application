package com.example.webviewtest;

public class allAbtBytes
{
    private static allAbtBytes instance = new allAbtBytes();

    private allAbtBytes(){}

    public static byte[] createByteArray(String text)
    {
        // if we need to do anything special depending on
        //  type of input string or otherwise, put here
        return text.getBytes();
    }

    public static allAbtBytes getInstance()
    {
        return instance;
    }
}
