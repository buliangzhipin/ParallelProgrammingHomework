package para;

import java.util.Scanner;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.Graphics;
import java.util.Random;
import para.graphic.target.*;
import para.graphic.shape.*;
import para.graphic.parser.*;
import para.game.*;

/**
 * モグラたたきゲームの雛形
 */
public class Game01 extends GameFrame {
	final static int WIDTH = 400;
	final static int HEIGHT = 700;
	final int MCOUNT = 9;
	final int XCOUNT = 3;
	Target inputside;
	Target outputside;
	Thread thread;
	ShapeManager osm;
	ShapeManager ism;
	private long prev;
	Random rand;
	private int last;
	private int[] slot;
	Attribute mogattr;

	volatile boolean flag = true;
	volatile boolean flagGame = true;
	GraphicsContext gc;
	volatile int point;
	volatile int turn = 0;

	public static void main(String[] args) {
		launch(args);
	}

	public Game01() {
		super(new JavaFXCanvasTarget(WIDTH, HEIGHT));
		canvas.init();
		title = "Mole";
		outputside = canvas;
		inputside = canvas;
		osm = new ShapeManager();
		ism = new ShapeManager();
		rand = new Random(System.currentTimeMillis());
		slot = new int[MCOUNT];
		mogattr = new Attribute(158, 118, 38);
		gc = canvas.getGraphicsContext2D();
		point = 0;
	}

	/**
	 * ルール説明用
	 */
	public void helpShow() {
		if (thread != null) {
			flag = false;
		}

		Platform.runLater(() -> {
			gc.setFont(new Font(40));
			gc.strokeText("ゲーム説明", 100, 140);
			gc.setFont(new Font(20));
			gc.strokeText("モグラに当たったら得点プラス５", 80, 180);
			gc.strokeText("100点になったら終わりになる", 60, 200);
			gc.strokeText("左下の数字は難易度の変更（ゲーム", 80, 260);
			gc.strokeText("開始時のみ有効である）", 60, 280);
			gc.strokeText("終わったら、再スタートできる", 80, 340);
		});

	}

	public void gamestart(int v) {
		if (thread != null) {
			flag = true;
			if (!flagGame) {
				flagGame = true;
				thread = new MyThread(v);
				thread.start();
				return;
			}
			synchronized (ism) {
				ism.notify();
			}
			return;
		}
		thread = new MyThread(v);
		thread.start();
	}

	private void mole(int v) {
		if((turn = ++turn %(v*4)) == 0) {
//			int appear = rand.nextInt(40);
//			if (30 < appear) {
				int head = rand.nextInt(MCOUNT - 1) + 1;
				int duration=0;
				if(v ==1|v==2) {
					duration = 600;
				}else if(v==3|v==4) {
					duration = 500;
				}

				if (slot[(last + head) % MCOUNT] <= 0) {
					slot[(last + head) % MCOUNT] = duration;
//				}
			}
		}		
		for (int i = 0; i < MCOUNT; i++) {
			slot[i] = slot[i] - 10;
		}
		for (int i = 0; i < MCOUNT; i++) {
			if (0 < slot[i]) {
				Garden.setMole(10 + i * 10, (i % XCOUNT) * 130 + 60, (i / XCOUNT) * 130 + 60, slot[i] / 25, osm);
				// osm.put(new Circle(10+i*10, (i%XCOUNT)*130+60, (i/XCOUNT)*130+60,
				// slot[i]/25,mogattr));
			} else {
				Garden.removeMole(10 + i * 10, osm);
				// osm.remove(10+i*10);
			}
		}
		inputside.draw(osm);
	}

	@Override
	public void start(Stage stage) {
		super.start(stage);
		Button button2 = new Button("Rule");
		button2.setPrefHeight(30);
		button2.setPrefWidth(100);
		button2.setOnAction(ev -> {
			helpShow();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			helpShow();
		});
		VBox vb = (VBox) stage.getScene().getRoot();
		HBox hb = (HBox) vb.getChildren().get(1);
		hb.getChildren().add(button2);
	}

	class MyThread extends Thread {
		int v;

		public MyThread(int v) {
			this.v = v;
		}

		@Override
		public void run() {
			point = 0;
			ism.put(new Digit(v + 1, 130, 440, 30, point / 100, new Attribute(200, 200, 200)));
			ism.put(new Digit(v + 2, 200, 440, 30, (point % 100) / 10, new Attribute(200, 200, 200)));
			ism.put(new Digit(v + 3, 270, 440, 30, point % 10, new Attribute(200, 200, 200)));
			Attribute attr = new Attribute(250, 80, 80);

			while (flagGame) {
				synchronized (ism) {
					if (!flag) {
						try {
							ism.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				try {
					Thread.sleep((int) (120 / v));
				} catch (InterruptedException ex) {
				}
				SynchronizedPoint p = xy.copy();
				if (prev != p.getTime()) {
					prev = p.getTime();
					ism.put(new Circle(v, (int) p.getXY()[0], (int) p.getXY()[1], 20, attr));
					Shape s = InsideChecker.check(osm, new Vec2(p.getXY()[0], p.getXY()[1]));
					if (s != null) {
						point += 100;
						ism.remove(v + 1);
						ism.remove(v + 2);
						ism.remove(v + 3);
						ism.put(new Digit(v + 1, 130, 440, 30, point / 100, new Attribute(200, 200, 200)));
						ism.put(new Digit(v + 2, 200, 440, 30, (point % 100) / 10, new Attribute(200, 200, 200)));
						ism.put(new Digit(v + 3, 270, 440, 30, point % 10, new Attribute(200, 200, 200)));

						slot[(s.getID() - 10) / 10] = 0;
						System.out.println(p.getXY()[0] + " " + p.getXY()[1] + " " + p.getTime());
						
						
						if (point >= 100) {
							flagGame = false;
						}
					}
				} else if (300 < System.currentTimeMillis() - prev) {
					ism.remove(v);
				}
				inputside.clear();
				mole(v);
				inputside.draw(ism);
				inputside.flush();
			}
			
			ism.remove(v);
			for (int i = 0; i < MCOUNT; i++) {
					slot[i]=0;
			}
			inputside.clear();
			inputside.draw(osm);
			inputside.draw(ism);
			inputside.flush();
			for(int i = 1;i<4;i++) {
				ism.remove(v+i);
			}
			Platform.runLater(() -> {
			gc.setFont(new Font(40));
			gc.strokeText("Game Clear", 100, 100);
			});
		}
	}
}
