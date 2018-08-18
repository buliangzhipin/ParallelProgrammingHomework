package para;

import java.nio.IntBuffer;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

public class test {
    /**
     * RGB値をずらした画像を作成
     * 赤成分を右下に、緑成分を左上に、青成分を左下に移動させている
     * ピクセル毎にgetPixel,setPixel関数を呼びだす（内部でピクセル走査が発生する）ため、低速な処理となる
     * @param img
     * @return
     */
    public WritableImage createRGBMoveImage( Image img )
    {
        // 白紙イメージを作成
        WritableImage   wImg        = new WritableImage( (int) img.getWidth() , (int) img.getHeight() );
 
        // ピクセル処理
        PixelReader     reader      = img.getPixelReader();
        PixelWriter     writer      = wImg.getPixelWriter();
        for( int y = 0 ; y < wImg.getHeight() ; y++ )
            for( int x = 0 ; x < wImg.getWidth() ; x++  )
            {
                // ずれ幅の設定
                int gap     = 5;
                 
                // ピクセル値を作成
                int argb    = 0x000000;
                 
                // α成分を作成
                int a       = ( reader.getArgb( x , y ) >> 24 ) & 0xFF ;
             
                // 赤成分を作成
                int r_x     = ( x + gap ) % (int) img.getWidth();
                int r_y     = ( y + gap ) % (int) img.getHeight();
                int r       = ( reader.getArgb( r_x , r_y ) >> 16 ) & 0xFF ;
             
                // 緑成分を作成
                int g_x     = ( x - gap + (int) img.getWidth() ) % (int) img.getWidth();
                int g_y     = ( y - gap + (int) img.getHeight() ) % (int) img.getHeight();
                int g       = ( reader.getArgb( g_x , g_y ) >> 8 ) & 0x0FF;
                 
                // 青成分を作成
                int b_x     = ( x + gap ) % (int) img.getWidth();
                int b_y     = ( y - gap + (int) img.getHeight() ) % (int) img.getHeight();
                int b       = ( reader.getArgb( b_x , b_y ) & 0xFF );
                argb        = ( a << 24 ) | ( r << 16 ) | ( g << 8 ) | b;
                 
                // ピクセル値を設定
                writer.setArgb( x , y, argb);
            }
         
        return wImg;
         
    }
    
    /**
     * 色を反転した画像を作成する
     * ピクセル処理を一括して行うため、比較的高速な処理
     * @param img
     * @return
     */
    public WritableImage createInversionImage( Image img )
    {
        // 画像サイズを取得
        int     width   = (int) img.getWidth();
        int     height  = (int) img.getHeight();
         
        // 複製したイメージを作成
        WritableImage   wImg        = new WritableImage( width , height );
        PixelWriter     writer      = wImg.getPixelWriter();
         
        // ピクセル配列(フォーマットはARGBの順)を取得
        WritablePixelFormat<IntBuffer>  format  = WritablePixelFormat.getIntArgbInstance();
        int[]                           pixels  = new int[ width * height ]; 
        img.getPixelReader().getPixels( 0 , 0 , width , height ,
                                        format, pixels, 0 , width );
         
        // ピクセル操作
        for( int y = 0 ; y < height ; y++ )
            for( int x = 0 ; x < width ; x++ )
            {
                // ピクセルを取得
                int index   = ( y * width ) + x;
                int pixel   = pixels[ index ];
                 
                // ピクセルを反転
                // α成分を作成
                int a       = ( pixel >> 24 ) & 0xFF;
             
                // 赤成分を作成
                int r       = 0xFF -  ( ( pixel >> 16 ) & 0xFF );
             
                // 緑成分を作成
                int g       = 0xFF - ( ( pixel >> 8  ) & 0xFF );
                 
                // 青成分を作成
                int b       = 0xFF - ( pixel & 0xFF );
                 
                // ピクセルを設定
                pixel           = ( a << 24 ) | ( r << 16 ) | ( g << 8 ) | b;
                pixels[ index ] = pixel;
            }
         
        // ピクセル配列を設定
        writer.setPixels(0 , 0 , width , height , format, pixels, 0 , width);
         
        return wImg;
    }
}
