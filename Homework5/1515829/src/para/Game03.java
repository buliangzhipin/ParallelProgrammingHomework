/**
 * 1515829
 * 李　墨然
 */
package para;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import para.graphic.target.*;
import para.graphic.shape.*;
import para.graphic.parser.*;
import para.game.*;

public class Game03 extends GameFrame {
	volatile Thread thread;
	final ShapeManager sm, wall, board;
	Vec2 pos;
	Vec2 vel;
	int bpos;
	static final int WIDTH = 320;
	static final int HEIGHT = 660;

	// new
	final ShapeManager digital;
	// レンガの数を管理する
	final int defaultX = 9;
	final int defaultY = 5;
	// ゲーム進行を管理する
	volatile boolean gameOngoingFlag = false;
	volatile boolean pauseFlag = false;
	volatile boolean cheatFlag = false;
	VBox root;

	public static void main(String[] args) {
		launch(args);
	}

	public Game03() {
		super(new JavaFXCanvasTarget(WIDTH, HEIGHT));
		title = "BreakOut";
		sm = new OrderedShapeManager();
		wall = new OrderedShapeManager();
		board = new ShapeManager();
		Attribute wallattr = new Attribute(250, 230, 200, true, 0, 0, 0);
		wall.add(new Rectangle(0, 0, 0, 320, 20, wallattr));
		wall.add(new Rectangle(1, 0, 0, 20, 300, wallattr));
		wall.add(new Rectangle(2, 300, 0, 20, 300, wallattr));
		wall.add(new Rectangle(3, 0, 281, 320, 20, wallattr));
		// wall.add(new Rectangle(3, 0,281, 120, 20, wallattr));
		// wall.add(new Rectangle(4, 200,281, 120, 20, wallattr));

		// new
		digital = new ShapeManager();
	}

	@Override
	public void start(Stage stage) {
		super.start(stage);
		VBox vb = (VBox) stage.getScene().getRoot();
		this.root = vb;
		HBox hb = (HBox) vb.getChildren().get(1);
		Button pause = new Button("pause");
		pause.setPrefHeight(30);
		hb.getChildren().add(pause);
		pause.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {
			pauseFlag = true;
		});
		Button cheat = new Button("cheat");
		cheat.setPrefHeight(30);
		cheat.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {
			if(gameOngoingFlag) {
				cheatFlag = true;
			}
		});
		hb.getChildren().add(cheat);
	}

	/**
	 * レンガを作るメソッド
	 * @param sm
	 */
	private void setBrick(int xAxis, int yAxis, int xLength, int yLength, ShapeManager sm) {
		int length = 160/yAxis;
		IntStream.range(0, xAxis * yAxis).forEach(n -> {
			int x = n % xAxis;
			int y = n / xAxis;
			sm.add(new Rectangle(10 + n, 30 + x * 30, 180 - y * length, 25, length-5,
					new Attribute(250, 100, 250, true, 0, 0, 0)));
		});
	}
	
	/**
	 * ゲームステージを初期化する
	 * @param v
	 * @return
	 */
	private int[] initialize(int v) {
		sm.clear();

		// new
		gameOngoingFlag = true;
		int[] axis = new int[] { defaultX, v * defaultY, v * defaultX * defaultY };
		setBrick(axis[0], axis[1], 0, 0, sm);
		// デバック用
		// setBrick(1, 1, 0, 0, sm);
		digital.put(new Digit(0, 120, 320, 15, axis[2] / 100, new Attribute(0, 0, 0)));
		digital.put(new Digit(1, 160, 320, 15, axis[2] % 100 / 10, new Attribute(0, 0, 0)));
		digital.put(new Digit(2, 200, 320, 15, axis[2] % 10, new Attribute(0, 0, 0)));
		
		try {
			digital.put(new para.graphic.shape.Image(3, 10, 350, ImageIO.read(new File("rule.png")), null));
		} catch (IOException e) {
			System.err.println("No rule image");
		}
		
		return axis;
	}
	
	/**
	 * pause処理を管理する
	 */
	private void pauseProcess() {
		synchronized (pos) {
			if (pauseFlag) {
				try {
					pos.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			Thread.sleep(80);
		} catch (InterruptedException ex) {
		}
	}
	
	/**
	 * 毎回のdrawを処理する
	 * @param attr
	 */
	private void drawFunction(Attribute attr) {
		if ((lefton == 1 || righton == 1)) {
			bpos = bpos - 8 * lefton + 8 * righton;
			if (bpos < 35) {
				bpos = 35;
			} else if (285 < bpos) {
				bpos = 285;
			}
			board.put(new Rectangle(15000, bpos - 40, 225, 80, 10, attr));
		}
		canvas.clear();
		canvas.draw(board);
		canvas.drawCircle(10000, (int) pos.data[0], (int) pos.data[1], 5,
				new Attribute(0, 0, 0, true, 0, 0, 0));
		// new
		canvas.draw(digital);
		canvas.draw(sm);
		canvas.draw(wall);
		canvas.flush();
	}
	
	/**
	 * スペースを押したら、ゲームがスタートする
	 */
	private void addStartKey() {
		root.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@SuppressWarnings("incomplete-switch")
			@Override
			public void handle(KeyEvent event) {
				switch(event.getCharacter()) {
				case " ":
					System.out.println("2222");
					vel = new Vec2(2, 8);
					root.removeEventHandler(KeyEvent.KEY_TYPED, this);
					break;
				}
			}
			
		});
	}


	public void gamestart(int v) {
		// pause管理
		if (pauseFlag) {
			pauseFlag = false;
			synchronized (pos) {
				pos.notify();
			}
			return;
		}

		if (thread != null && gameOngoingFlag) {
			return;
		}
		
		int axis[] = initialize(v);

		
		thread = new Thread(() -> {
			gameOngoingFlag = true;
			bpos = 150;
			pos = new Vec2(bpos, 223);
			vel = new Vec2(0, 0);
			// board.put(new Camera(0, 0, 380,attr));
			Attribute attr = new Attribute(150, 150, 150, true);
			board.put(new Rectangle(15000, bpos - 40, 225, 80, 10, attr));
			canvas.draw(board);
			canvas.draw(sm);
			canvas.draw(digital);

			float time;
			float[] btime = new float[] { 1.0f };
			float[] stime = new float[] { 1.0f };
			float[] wtime = new float[] { 1.0f };
			
			
			//" "を押したら、玉が動くEventを追加する
			addStartKey();

			while (gameOngoingFlag) {
				
				//pause処理
				pauseProcess();
				//draw処理
				drawFunction(attr);

				
				CollisionChecker ccp = new CollisionCheckerParallel2(true);
				time = 1.0f;
				while (0 < time) {
					
					
					//cheat用
					if(cheatFlag) {
						axis[2] =0;
						digital.put(new Digit(0, 120, 320, 15, axis[2] / 100, new Attribute(0, 0, 0)));
						digital.put(new Digit(1, 160, 320, 15, axis[2] % 100 / 10, new Attribute(0, 0, 0)));
						digital.put(new Digit(2, 200, 320, 15, axis[2] % 10, new Attribute(0, 0, 0)));
						drawFunction(attr);
						if (axis[2] == 0) {
							gameOngoingFlag = false;
							try {
								BufferedImage bf = ImageIO.read(new File("mission.png"));
								canvas.drawImage(100, 80, 100, bf, null);
								canvas.flush();
							} catch (IOException e) {
								System.err.println("Miss the finished picture");
							}
						}				
						cheatFlag = false;
						break;
					}
					
					
					btime[0] = time;
					stime[0] = time;
					wtime[0] = time;
					Vec2 tmpbpos = new Vec2(pos);
					Vec2 tmpbvel = new Vec2(vel);
					Vec2 tmpspos = new Vec2(pos);
					Vec2 tmpsvel = new Vec2(vel);
					Vec2 tmpwpos = new Vec2(pos);
					Vec2 tmpwvel = new Vec2(vel);
					Shape b = ccp.check(board, tmpbpos, tmpbvel, btime);
					Shape s = ccp.check(sm, tmpspos, tmpsvel, stime);
					Shape w = ccp.check(wall, tmpwpos, tmpwvel, wtime);
					if (b != null && (s == null || stime[0] < btime[0]) && (w == null || wtime[0] < btime[0])) {
						pos = tmpbpos;
						vel = tmpbvel;
						time = btime[0];
					} else if (s != null) {
						sm.remove(s);
						pos = tmpspos;
						vel = tmpsvel;
						time = stime[0];

						// 残る数字を表示する
						axis[2] -= 1;
						digital.put(new Digit(0, 120, 320, 15, axis[2] / 100, new Attribute(0, 0, 0)));
						digital.put(new Digit(1, 160, 320, 15, axis[2] % 100 / 10, new Attribute(0, 0, 0)));
						digital.put(new Digit(2, 200, 320, 15, axis[2] % 10, new Attribute(0, 0, 0)));
						if (axis[2] == 0) {
							gameOngoingFlag = false;
							try {
								BufferedImage bf = ImageIO.read(new File("mission.png"));
								canvas.drawImage(100, 80, 100, bf, null);
								canvas.flush();
							} catch (IOException e) {
								System.err.println("Miss the finished picture");
							}
						}

					} else if (w != null) {
						//ゲームおーバを表示する　
						if (w.getID() == 3) {
							time = 0;
							gameOngoingFlag = false;
							try {
								BufferedImage bf = ImageIO.read(new File("gameover.png"));
								canvas.drawImage(100, 100, 100, bf, null);
								canvas.flush();
							} catch (IOException e) {
								System.err.println("Miss the failure picture");
							}
						}
						pos = tmpwpos;
						vel = tmpwvel;
						time = wtime[0];
					} else {
						pos = MathUtil.plus(pos, MathUtil.times(vel, time));
						time = 0;
					}
				}
			}
		});
		thread.start();
	}
}
