package com.example.tictactoev0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Point;
import android.view.View;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.util.DisplayMetrics;
import android.util.Log;

import org.w3c.dom.Text;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    private TicTacToe tttGame;
    private Button [][] buttons;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        tttGame = new TicTacToe();
        buildGuiByCode();
    }

    public void buildGuiByCode(){
        // 检索屏幕宽度
        //Point size = new Point();
        //getWindowManager().getDefaultDisplay().getSize(size);   //即将被废除，可以换成下面的方法
        //方法一
        DisplayMetrics metrics2 = getResources().getDisplayMetrics();
        int width_one = metrics2.widthPixels;
        //方法二
        //int width_two = getWindowManager().getCurrentWindowMetrics().getBounds().width()

        int w = width_one / TicTacToe.SIDE;

        // 将布局管理器创建为GridLayout
        GridLayout gridlayout = new GridLayout(this);
        gridlayout.setColumnCount(TicTacToe.SIDE);
        gridlayout.setRowCount(TicTacToe.SIDE + 1);     // 布局添加一行

        //创建按钮并将它们添加到布局中
        buttons = new Button[TicTacToe.SIDE][TicTacToe.SIDE];
        ButtonHandler bh = new ButtonHandler();
        for(int row=0; row<TicTacToe.SIDE; row++){
            for(int col=0; col<TicTacToe.SIDE;col++){
                buttons[row][col] = new Button(this);
                buttons[row][col].setTextSize((int)(w*.2));
                buttons[row][col].setOnClickListener(bh);
                gridlayout.addView(buttons[row][col],w,w);
            }
        }

        // 设置gridLayout第四行的布局参数
        status = new TextView(this);
        GridLayout.Spec rowSpec = GridLayout.spec(TicTacToe.SIDE,1);
        GridLayout.Spec columnSpec = GridLayout.spec(0,TicTacToe.SIDE);
        GridLayout.LayoutParams lpStatus = new GridLayout.LayoutParams(rowSpec,columnSpec);
        status.setLayoutParams(lpStatus);

        // 设置status的特征
        status.setWidth(TicTacToe.SIDE * w);
        status.setHeight(w);
        status.setGravity(Gravity.CENTER);
        status.setBackgroundColor(Color.GREEN);
        status.setTextSize((int)(w*5));
        status.setText(tttGame.result());

        gridlayout.addView(status);

        setContentView(gridlayout);
    }
    public void update(int row, int col){
        int play = tttGame.play(row,col);
        if(play==1)
            buttons[row][col].setText("X");
        else if(play==2)
            buttons[row][col].setText("O");
        if(tttGame.isGameOver()) {
            status.setBackgroundColor(Color.RED);
            enableButtons(false);
            status.setText(tttGame.result());
            showNewGameDialog();
        }
    }

    public void enableButtons(boolean enabled){
        for(int row=0;row<TicTacToe.SIDE;row++)
            for(int col=0;col<TicTacToe.SIDE;col++)
                buttons[row][col].setEnabled(enabled);
    }

    public void resetButtons(){
        for(int row = 0; row < TicTacToe.SIDE; row++)
            for(int col = 0;col < TicTacToe.SIDE;col++)
                buttons[row][col].setText("");
    }

    public void showNewGameDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("This is fun");
        alert.setMessage("Play again ?");
        PlayDialog playAgain = new PlayDialog();
        alert.setPositiveButton("YES", playAgain);
        alert.setNegativeButton("NO",playAgain);
        alert.show();
    }

    private class ButtonHandler implements View.OnClickListener{
        public void onClick(View v){
            Log.w("MainActivity","Inside onClick, v="+v);
            for(int row=0;row<TicTacToe.SIDE;row++)
                for(int column=0;column<TicTacToe.SIDE;column++)
                    if(v==buttons[row][column])
                        update(row,column);
        }
    }

    private class PlayDialog implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int id) {
            if(id==-1){
                tttGame.resetGame();
                enableButtons(true);
                resetButtons();
                status.setBackgroundColor(Color.GREEN);
                status.setText(tttGame.result());
            }else if(id==-2)
                MainActivity.this.finish();
        }
    }
}


//public class MainActivity extends AppCompatActivity {
//
//    public StarSrvGroupClass SrvGroup;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        final File appFile = getFilesDir();  /*-- /data/data/packageName/files --*/
//        final String appLib = getApplicationInfo().nativeLibraryDir;
//
//        AsyncTask.execute(new Runnable() {
//
//            @Override
//            public void run() {
//                loadPy(appLib);
//            }
//        });
//    }
//
//
//    //cle调用python代码
//    void loadPy(String appLib){
//        // Extract python files from assets
//        AssetExtractor assetExtractor = new AssetExtractor(this);
//        assetExtractor.removeAssets("python");
//        assetExtractor.copyAssets("python");
//
//        // Get the extracted assets directory
//        String pyPath = assetExtractor.getAssetsDataDir() + "python";
//
////        try {
////            // 加载Python解释器
////            System.load(appLib + File.separator + "libpython3.6m.so");
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        /*----init starcore----*/
//        StarCoreFactoryPath.StarCoreCoreLibraryPath = appLib;
//        StarCoreFactoryPath.StarCoreShareLibraryPath = appLib;
//        StarCoreFactoryPath.StarCoreOperationPath = pyPath;
//
//        StarCoreFactory starcore = StarCoreFactory.GetFactory();
//        //用户名、密码 test , 123
//        StarServiceClass service = starcore._InitSimple("test", "123", 0, 0);
//        StarSrvGroupClass mSrvGroup = (StarSrvGroupClass) service._Get("_ServiceGroup");
//        service._CheckPassword(false);
//
//        /*----run python code----*/
//        mSrvGroup._InitRaw("python37", service);
//        StarObjectClass python = service._ImportRawContext("python", "", false, "");
//        /* 设置Python模块加载路径 即sys.path.insert() */
//        python._Call("import", "sys");
//        StarObjectClass pythonSys = python._GetObject("sys");
//        StarObjectClass pythonPath = (StarObjectClass) pythonSys._Get("path");
//        pythonPath._Call("insert", 0, pyPath+ File.separator +"python3.7.zip");
//        pythonPath._Call("insert", 0, appLib);
//        pythonPath._Call("insert", 0, pyPath);
//
//
//        //调用Python代码
//        service._DoFile("python", pyPath + "/test.py", "");
//        int result = python._Callint("add", 5, 2);
//        TextView tv1 = (TextView) findViewById(R.id.at_1);
//        tv1.setText("python中计算5+2：   " + result + "");
//    }
//}