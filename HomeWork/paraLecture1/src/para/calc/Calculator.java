package para.calc;

import java.lang.StringIndexOutOfBoundsException;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

/**
 * JavaFX 電卓アプリケーションのメインクラス
 */
public class Calculator extends Application{
  Label input;
  Label output;
  StringBuilder buff;
  Executor ex;
  StringBuilder str;
  
  String[] buttonname = 
	     {"9","8","7","+",
          "6","5","4","-",
          "3","2","1","*",
          "0",".",",","/"};
  
  boolean numFlag = true;
  
  public static void main(String[] args) {
	  launch("");
 }
  
  public Calculator(){
    input = new Label();
    output = new Label();
    buff = new StringBuilder();
    ex = new Executor1();
  }


  public void start(Stage stage){
    VBox root = new VBox();
    root.setAlignment(Pos.TOP_CENTER);
    GridPane grid = new GridPane();
    Scene scene = new Scene(root, 200,300);
    Button[] buttons = new Button[16];
    Button buttoncal = new Button("=");
    buttoncal.setPrefSize(28,56);
    Button buttondel = new Button("<");
    buttondel.setPrefSize(28,56);
    StackPane stack = new StackPane();
    stack.getChildren().add(new Rectangle(140,30,Color.WHITE));
    stack.getChildren().add(input);
    root.getChildren().addAll(stack, output);
    EventHandler<MouseEvent> pushHandle = new pushHandler();
    for(int i=0;i<16;i++){
      buttons[i] = new Button(buttonname[i]);
      buttons[i].setPrefSize(28, 28);
      buttons[i].addEventHandler(MouseEvent.MOUSE_CLICKED, pushHandle);
      grid.add(buttons[i], i%4, i/4);
    }
    grid.add(buttondel, 4, 0, 1, 2);
    grid.add(buttoncal, 4, 2, 1, 2);
    root.getChildren().add(grid);
    VBox.setMargin(grid, new Insets(0, (200-28*5)/2, 0, (200-28*5)/2));
    buttondel.addEventHandler(MouseEvent.MOUSE_CLICKED, ((ev)->{
        try{
            buff.deleteCharAt(buff.length()-1);
        }catch(StringIndexOutOfBoundsException e){
            System.out.println("nothing here");
        }
    	
    	input.setText(buff.toString());
    }));
    buttoncal.addEventHandler(MouseEvent.MOUSE_CLICKED,((ev)->{
    	String outputLabel = ex.operation(buff.toString());
    	output.setText(outputLabel);
    })	);











    













    stage.setWidth(200);
    stage.setHeight(200);
    stage.setScene(scene);
    stage.setTitle("JavaFX Calc");
    stage.show();
  }
  class pushHandler implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			String test = ((Button)event.getSource()).getText();
			if(test == buttonname[3]||test == buttonname[7]||test == buttonname[11]||test == buttonname[15]) {
				test = test+",";
				numFlag = false;
			}
//			else if(numFlag == false){
//				test = ","+test;
//				numFlag = true;
//			}
			buff.append(test);
			input.setText(buff.toString());
		}
		  
	}

}


