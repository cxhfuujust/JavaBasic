package com.chenx.net.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 闪电侠
 */
public class IOServer {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8080);
        AtomicInteger connectCount = new AtomicInteger();
        // (1) 接收新连接线程
        new Thread(() -> {
            while (true) {
                try {
                    // (1) 阻塞方法获取新的连接
                    Socket socket = serverSocket.accept();
                    System.out.println("socket"+socket);
                    System.out.println("一个Client连接，总共有"+ (connectCount.incrementAndGet()) +"连接！");

                    // (2) 每一个新的连接都创建一个线程，负责读取数据
                    new Thread(() -> {
                        try {
                            int len;
                            byte[] data = new byte[1024];
                            InputStream inputStream = socket.getInputStream();
                            // (3) 按字节流方式读取数据
                            len = inputStream.read(data);
                            while (len != -1) {
                                System.out.println("server收到信息：" + new String(data, 0, len));
                                len = inputStream.read(data); //阻塞方法，获取数据
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("一个Client断开连接，总共有"+ (connectCount.decrementAndGet()) +"连接！");
                    }).start();

                } catch (IOException e) {
                }
            }
        }).start();
    }
}