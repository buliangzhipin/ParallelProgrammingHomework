package para.paint;

import java.io.File;

import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.canvas.*;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
//import javafx.scene.paint.Paint;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;



/**
 * JavaFX お絵描きアプリケーションのメインクラス
 */
public class Paint extends Application {
	Canvas canvas;
	Canvas canvasColor;
	GraphicsContext gc;
	GraphicsContext gc2;
	double oldx;
	double oldy;
	final int SIZE = 600;
	final int SIZE2 = 30;
	Button clear;
//	Button load;
//	Button save;
	final Menu file = new Menu("file");
	final Menu helpMenu = new Menu("help");
	final Menu effect = new Menu("effect");
	MenuItem save;
	MenuItem load;
	MenuItem help;
	MenuItem rotate;
	MenuItem rotate2;
	MenuBar mb;
	double args=0;

	public static void main(String[] args) {
		launch("");
	}

	/**
	 * お絵描きプログラムの準備をして、ウィンドウを開きます
	 */
	public void start(Stage stage) {
		Group group = new Group();
		//canvasの初期設定
		canvas = new Canvas(SIZE, SIZE);
		canvasColor = new Canvas(SIZE2,SIZE2);
		gc = canvas.getGraphicsContext2D();
		gc2 = canvasColor.getGraphicsContext2D();
		gc2.setFill(Color.BLUE);
		gc2.fillRect(0, 0, SIZE2, SIZE2);
		drawShapes(gc);
		canvas.setOnMousePressed(ev -> {
			oldx = ev.getX();
			oldy = ev.getY();
		});
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent ev) {
				gc.strokeLine(oldx, oldy, ev.getX(), ev.getY());
				oldx = ev.getX();
				oldy = ev.getY();
			}
		});

		//clearボタンの設定
		clear = new Button("clear");
		clear.setOnAction(e -> {
			gc.setFill(Color.WHITE);
			gc.fillRect(0, 0, SIZE, SIZE);
		});

		//Menuの設定（save,load,回転,helpなどある）
		load = new MenuItem("load");
		load.setOnAction(ev -> {
			FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                                                     new ExtensionFilter("jpeg files (*.jpg)", "*.jpg"),
                                                     new ExtensionFilter("png files (*.png)", "*.png"),
                                                     new ExtensionFilter("bmp files (*.bmp)", "*.bmp"));
            try{
                File file = fileChooser.showOpenDialog(stage);
                if(file!=null){
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0, 0, SIZE, SIZE);
                    Image img = new Image(file.toURI().toString());
                    gc.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), 0, 0, SIZE, SIZE);
                }
            }catch(NullPointerException e){
                System.out.println("nothing to be load");
            }
			
		});
		
		save = new MenuItem("save");
		save.setOnAction(ev->{
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(
                                                     new ExtensionFilter("png files (*.png)", "*.png")
                                                     );

            try{
                File file = fileChooser.showSaveDialog(stage);
                String str = getSuffix(file.getName());
                WritableImage image = new WritableImage(SIZE, SIZE);
                image = canvas.snapshot(null,null);
                
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), str, file);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }catch(NullPointerException e){
                System.out.println("nothing to be read");
            }
            
		});
		
		
		rotate = new MenuItem("時計回り");
		rotate.setOnAction(ev->{
			canvas.setRotate(canvas.getRotate()+90);
		});
		
		rotate2 = new MenuItem("逆時計回り");
		rotate2.setOnAction(ev->{
			canvas.setRotate(canvas.getRotate()-90);
		});
		
		help = new MenuItem("help");
		help.setOnAction(ev->{
			final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            VBox dialogVbox = new VBox();
            dialogVbox.getChildren().add(new Text("R：赤色\nG：緑色\nB：青色\nO：透明度\nW：太さ\nload：jpg,png,bmpがloadできる。前の画面を消して、画像の大きさをcanvasに合わせてdrawする。\nsave:pngのみsaveできる\n時計回り：時計回り回転する\n逆時計回り:逆時計回り回転する\nrgbの変更に伴い中間の色も変更する"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
		});

		//色および透明度変更などの設定
		BorderPane bp = new BorderPane();
		VBox vb = new VBox();
		Slider sliderr = new Slider(0, 1, 0);
		sliderr.setPrefWidth(550);
		Slider sliderg = new Slider(0, 1, 0);
		sliderg.setPrefWidth(550);
		Slider sliderb = new Slider(0, 1, 1);
		sliderb.setPrefWidth(550);
		Slider sliderw = new Slider(0, 10, 5);
		sliderw.setPrefWidth(550);
		Slider slidero = new Slider(0, 1, 0.5);
		slidero.setPrefWidth(550);
		sliderr.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldv, Number nv) -> {
			float r = (float) sliderr.getValue();
			Color myStroke = (Color) gc.getStroke();
			gc.setStroke(Color.color(r, myStroke.getGreen(), myStroke.getBlue(), myStroke.getOpacity()));
			gc2.setFill(Color.color(r, myStroke.getGreen(), myStroke.getBlue(), myStroke.getOpacity()));
			gc2.fillRect(0, 0, SIZE2, SIZE2);
		});
		sliderg.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldv, Number nv) -> {
			float g = (float) sliderg.getValue();
			Color myStroke = (Color) gc.getStroke();
			gc.setStroke(Color.color(myStroke.getRed(), g, myStroke.getBlue(), myStroke.getOpacity()));
			gc2.setFill(Color.color(myStroke.getRed(), g, myStroke.getBlue(), myStroke.getOpacity()));
			gc2.fillRect(0, 0, SIZE2, SIZE2);
		});
		sliderb.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldv, Number nv) -> {
			float b = (float) sliderb.getValue();
			Color myStroke = (Color) gc.getStroke();
			gc.setStroke(Color.color(myStroke.getRed(), myStroke.getGreen(), b, myStroke.getOpacity()));
			gc2.setFill(Color.color(myStroke.getRed(), myStroke.getGreen(), b, myStroke.getOpacity()));
			gc2.fillRect(0, 0, SIZE2, SIZE2);
		});
		sliderw.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldv, Number nv) -> {
			float w = (float) sliderw.getValue();
			gc.setLineWidth(w);

		});
		slidero.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldv, Number nv) -> {
			float o = (float) slidero.getValue();
			Color myStroke = (Color) gc.getStroke();
			gc.setStroke(Color.color(myStroke.getRed(), myStroke.getGreen(), myStroke.getBlue(), o));
			gc2.setFill(Color.color(myStroke.getRed(), myStroke.getGreen(), myStroke.getBlue(), o));
			gc2.fillRect(0, 0, SIZE2, SIZE2);
		});
		
	
		//MenuBar
		mb = new MenuBar(file,effect,helpMenu);
		file.getItems().add(save);
		file.getItems().add(load);
		helpMenu.getItems().add(help);
		effect.getItems().add(rotate);
		effect.getItems().add(rotate2);
		
		//Mainの部分
		vb.setAlignment(Pos.CENTER);
		vb.getChildren().add(mb);
		GridPane gp = new GridPane();
		gp.setHgap(30);
		gp.add(new Label("r"), 0, 0);
		gp.add(sliderr, 1, 0);
		gp.add(new Label("g"), 0, 1);
		gp.add(sliderg, 1, 1);
		gp.add(new Label("b"), 0, 2);
		gp.add(sliderb, 1, 2);
		gp.add(new Label("o"), 0, 3);
		gp.add(slidero, 1, 3);
		gp.add(new Label("w"), 0, 4);
		gp.add(sliderw, 1, 4);
		vb.getChildren().add(gp);
		HBox options = new HBox();
		options.setSpacing(255);
		options.getChildren().add(clear);
		options.getChildren().add(canvasColor);
		vb.getChildren().add(options);
		
		
		bp.setTop(vb);	
		bp.setCenter(canvas);
		
		
		Scene scene = new Scene(bp);
		stage.setScene(scene);
		stage.setTitle("JavaFX Draw");
		stage.show();
	}

	/**
	 * 初期化メソッド、startメソッドの呼び出され方とは異なる呼び出され方をする。必要ならば定義する
	 */
	public void init() {
	}

	/**
	 * 図形を描きます。 図形描画の実装サンプルです
	 */
	private void drawShapes(GraphicsContext gc) {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, SIZE, SIZE);
		gc.setFill(Color.GREEN);
		gc.setStroke(Color.BLUE);
		gc.setLineWidth(4);
		gc.strokeLine(40, 10, 10, 40);
		gc.fillOval(60, 10, 30, 30);
		gc.strokeOval(110, 10, 30, 30);
		gc.fillRoundRect(160, 10, 30, 30, 10, 10);
	}
    
    /**
     * ファイル名から拡張子を返します。
     * @param fileName ファイル名
     * @return ファイルの拡張子
     */
    public static String getSuffix(String fileName) {
        if (fileName == null)
            return null;
        int point = fileName.lastIndexOf(".");
        if (point != -1) {
            return fileName.substring(point + 1);
        }
        return fileName;
    }
	

}
