package para;
/*
 * author:1515829
 * 李墨然
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;


import para.graphic.parser.MainParser;
import para.graphic.shape.*;
import para.graphic.target.*;

/**
 * カメラを起動し、サーバに送るデモ
 */
public class Main08 {
	/**
	 * メインメソッド
	 * 
	 * @param args
	 *            args[0]は相手先のホスト
	 */
	public static void main(String[] args) {
		//受信を表示するための画面とスレッド
		final int MAXCONNECTION = 3;
		JavaFXTarget FXTarget = new JavaFXTarget("Client", 320 * MAXCONNECTION, 240);
		ShapeManager smServer = new ShapeManager();
		FXTarget.init();
		FXTarget.clear();
		FXTarget.flush();
		new Thread(() ->{
			while (true) {
//				FXTarget.draw(smServer);
				FXTarget.flush();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		
		final int PORTNO = 30000;
		ShapeManager sm = new ShapeManager();
		sm.put(new Camera(0, 0, 0));
		try (Socket s = new Socket("localhost", PORTNO)) {
			//javafxのshapemanegerに今の情報を伝えるためのスレッド
			BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			MainParser parser = new MainParser(FXTarget, smServer);
			new Thread(() -> {
				parser.parse(new Scanner(r));
			}).start();
			
			
			//Mainスレッドを送信するためのスレッド
			PrintStream ps;
			ps = new PrintStream(s.getOutputStream());
			Target target = new TextTarget(ps);
			// Target target = new TextTarget(System.out);
			target.init();
			target.clear();
			while (true) {
				target.draw(sm);
				target.flush();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (ps.checkError()) {
					break;
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
