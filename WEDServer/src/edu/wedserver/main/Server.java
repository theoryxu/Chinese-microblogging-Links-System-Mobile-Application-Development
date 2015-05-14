package edu.wedserver.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import edu.wed.main.WED2015Handler;
import edu.wed.model.BaikeItem;

public class Server {

	public static void main(String[] args) {
		
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 监听服务器端口
					ServerSocket serverSocket = new ServerSocket(10000, 100);
					System.out.println("Ready");
								
					while (true) {
						// 建立与客户端通信的Socket连接
						Socket s = serverSocket.accept();
						System.out.println("Socket OK");

						BufferedReader is = new BufferedReader(
								new InputStreamReader(s.getInputStream()));
						BufferedWriter os = new BufferedWriter(
								new OutputStreamWriter(s.getOutputStream()));

						String weibo = is.readLine();
						String word = is.readLine();
						String send = "";

						// 调用实体链接系统
						List<BaikeItem> baikeitem_list = WED2015Handler
								.handlerDisam(word, weibo);
						
						for (BaikeItem bi : baikeitem_list) {
							send = send + bi.getTitle() + "," + bi.getUrl()
									+ ";";
						}
						
						os.write(send + "\n");
						os.flush();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		thread.start();

	}

}
