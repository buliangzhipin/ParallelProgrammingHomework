package para;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import para.graphic.target.*;
import para.graphic.shape.*;
import para.graphic.parser.MainParser;

/**
 * クライアントからの通信を受けて描画するサーバプログラム。 監視ポートは30000番
 */
public class Main09 {
	final public int PORTNO = 30000;
	final int MAXCONNECTION = 3;
	final Target target;
	final ShapeManager[] sms;
	final ServerSocket ss;

	final Executor exe;

	/**
	 * 受け付け用ソケットを開くこと、受信データの格納場所を用意すること を行う
	 */
	public Main09() {
		target = new JavaFXTarget("Server", 320 * MAXCONNECTION, 240);
		// target = new TextTarget(System.out);
		ServerSocket tmp = null;
		try {
			tmp = new ServerSocket(PORTNO);
		} catch (IOException ex) {
			System.err.println(ex);
			System.exit(1);
		}
		ss = tmp;
		sms = new ShapeManager[MAXCONNECTION];
		for (int i = 0; i < MAXCONNECTION; i++) {
			sms[i] = new OrderedShapeManager();
		}

		this.exe = Executors.newFixedThreadPool(3);
	}

	/**
	 * 受け付けたデータを表示するウィンドウの初期化とそこに受信データを表示するスレッドの開始
	 */
	public void init() {
		target.init();
		target.clear();
		target.flush();
		new Thread(() -> {
			while (true) {
				for (ShapeManager sm : sms) {
					synchronized (sm) {
						target.draw(sm);
					}
				}
				target.flush();
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
				}
			}
		}).start();
	}

	/**
	 * 受信の処理をする
	 */
	public void start() {
		Flag flag = new Flag();

		while (true) {
			try {
				Socket s = ss.accept();
				int i = flag.notBusy();
				Runnable runner = new Runnable() {
					@Override
					public void run() {
						comminucat(s, flag, i);
					}
				};
				comminucatClient(s, flag, i);

				exe.execute(runner);
			} catch (IOException ex) {
				System.err.print(ex);
			}

		}
	}
	
	
	/*
	 * クライアントに送るためのメソッド
	 * author:15B15829李墨然
	 */
	public void comminucatClient(Socket s, Flag flag, int i) {
		new Thread(() -> {
			Target outTarget;
			try {
				outTarget = new TextTarget(s.getOutputStream());
				while (flag.getflag(i)) {

					for (ShapeManager sm : sms) {
						outTarget.draw(sm);
					}
					Thread.sleep(100);

				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}).start();
	}
	
	/*
	 * サーバ側が受けるためのメソッド
	 * author:15B15829李墨然
	 */
	public void comminucat(Socket s, Flag flag, int i) {
		try (s) {

			BufferedReader r;
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			ShapeManager dummy = new ShapeManager();
			MainParser parser = new MainParser(
					new TranslateTarget(sms[i], new TranslationRule(10000 * i, new Vec2(320 * i, 0))), dummy);
			parser.parse(new Scanner(r));

			flag.setflag(i);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			flag.setflag(i);
			sms[i].clear();
			target.clear();
		}
	}

	public static void main(String[] args) {
		Main09 m = new Main09();
		m.init();
		m.start();
	}
	
	
	/*
	 * 今空いている画面を管理するためのフラグクラス
	 * author:15B15829李墨然
	 */
	class Flag {
		private boolean flag[] = { false, false, false };

		public Flag() {
		}

		synchronized int notBusy() {
			for (int i = 0; i < MAXCONNECTION; i++) {
				if (!flag[i]) {
					flag[i] = true;
					return i;
				}
			}
			return 0;
		}

		synchronized void setflag(int i) {
			flag[i] = false;
		}

		synchronized boolean getflag(int i) {
			return flag[i];
		}
	}
}
