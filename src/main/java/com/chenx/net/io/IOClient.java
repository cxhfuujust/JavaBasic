package com.chenx.net.io;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * @author 闪电侠
 */
public class IOClient {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                socket.setKeepAlive(true);
                System.out.println(socket.getKeepAlive());
                while (!socket.isClosed()) {
                    try {
                        socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                }
                System.out.println("Channel close!");
                //socket.close();

            } catch (IOException e) {
            }

        }).start();


    }
}