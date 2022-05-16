import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class interactiveGameOriginal extends PApplet {

final int TITLE = 0;
final int MENU = 1;
final int MAINGAME = 2;
final float fps = 60;
boolean debug = false;

Game game;
Utils utils;
Button btn;

//きーの判定。
boolean mouseClicked = false;
boolean someKey = false;
boolean keyD = false;
boolean keyA = false;
boolean keyW = false;
boolean keyS = false;
int keyE = -1;
int keyR = -1;

public void setup(){
    
    colorMode(HSB, 360, 100, 100);
    frameRate(fps);

    PFont font = createFont("Meiryo", 50);
    textFont(font);

    game = new Game(TITLE);
    utils = new Utils();
    btn = new Button();
}

public void draw(){
    game.gameLoop();
    // println(mouseClicked);
}


public void mousePressed(){
    mouseClicked = true;
}
public void mouseReleased(){
    mouseClicked = false;
}
public void keyPressed(){
    someKey = true;
    if (key == 'd') {
        keyD = true;
    }
    if (key == 'a') {
        keyA = true;
    }
    if (key == 'w') {
        keyW = true;
    }
    if (key == 's') {
        keyS = true;
    }
    if(key == 'e'){
        keyE++;
    }
    if(key == 'r') {
        keyR++;
    }
}
public void keyReleased() {
    someKey = false;
    if (key == 'd') {
        keyD = false;
    }
    if (key == 'a') {
        keyA = false;
    }
    if (key == 'w') {
        keyW = false;
    }
    if (key == 's') {
        keyS = false;
    }
    if(key == 'e'){
        keyE = -1;
    }
    if(key == 'r') {
        keyR = -1;
    }
}
class Animation{
    final float corner = dist(width / 2, height / 2, width, height);
    boolean finished = false;
    int screen = 0;
    float startAn = 0;
    float endAn = 0;
    float dAn = 6;

    //ゲームの設定
    int setLevel = -1;

    public void looping(){
        fill(0, 0, 20);
        arc(width / 2, height / 2, corner * 2, corner * 2, radians(this.startAn), radians(this.endAn));

        switch(this.screen){
            case 0:
                this.endAn += this.dAn;
                if(this.endAn >= 360){
                    this.screen = 1;
                }
            break;
            case 1:
                    game.setScreen(game.getNextScreen());
                    this.screen = 2;
                    if(game.mainGame.getFinish() || game.mainGame.getAgain()){
                        game.mainGame.init(this.setLevel);
                        if(debug) println(this.setLevel);
                    }   
            break;
            case 2:
                this.endAn = 360;
                this.startAn += this.dAn;
                if(this.startAn >= 360){
                    this.finished = true;
                }
            break;
        }

        if(this.finished){
            game.setMoveAnime(false);
            this.finished = false;
            this.screen = 0;
            this.startAn = 0;
            this.endAn = 0;
        }
    }

    public void setSetLevel(int level){
        this.setLevel = level;
    }
}
class Button{
    final int START = 0;
    final int AGAIN = 1;
    final int R_LEVEL = 2;
    final int R_TITLE = 3;
    final int AKIRAMERU = 4;
    final int TEXT_MAX = 5;

    float[][] strCol = new float[][]{
        {0, 0, 100},    //START
        {0, 0, 100},      //AGAIN
        {0, 0, 100},      //R_LEVEL
        {0, 0, 100},      //R_TITLE
        {0, 0, 0}       //AKIRAMERU
    };
    float[][] innerCol = new float[][]{
        {220, 100, 100},//START
        {220, 100, 100},//AGAIN
        {220, 100, 100},//R_LEVEL
        {220, 100, 100},//R_TITLE
        {20, 100, 100} //AKIRAMERU
    };
    float[][] wakuCol = new float[][]{
        {0, 0, 100},    //START
        {0, 0, 100},      //AGAIN
        {0, 0, 100},      //R_LEVEL
        {0, 0, 100},      //R_TITLE
        {0, 0, 0}       //AKIRAMERU
    };

    public void startBtn(float buttonX, float buttonY, float buttonWidth, float buttonHeight, float txtSize){
        this.buttonUpdate(buttonX, buttonY, buttonWidth, buttonHeight,
                          START, 220, 200, MENU);

        utils.makeText("START", txtSize, buttonX, buttonY, buttonWidth, buttonHeight, strCol[START], innerCol[START], wakuCol[START]);
    }

    public void again(float buttonX, float buttonY, float buttonWidth, float buttonHeight, float txtSize){
        if(utils.dotWithRect(mouseX, mouseY, buttonX- buttonWidth / 2, buttonY - buttonHeight / 2, buttonWidth, buttonHeight)){
            if(innerCol[AGAIN][0] > 200)
            innerCol[AGAIN][0] -= 2;
            if(mouseClicked){
                game.mainGame.setAgain(true);
                game.moveToAnime();
                game.setNextScreen(MAINGAME);
            }
        }else{
            if(innerCol[AGAIN][0] < 220)
            innerCol[AGAIN][0] += 2;
        }
        utils.makeText("もう一度やる", txtSize, buttonX, buttonY, buttonWidth, buttonHeight, strCol[AGAIN], innerCol[AGAIN], wakuCol[AGAIN]);
    }

    public void level(float buttonX, float buttonY, float buttonWidth, float buttonHeight, float txtSize){
        this.buttonUpdate(buttonX, buttonY, buttonWidth, buttonHeight,
                          R_LEVEL, 220, 200, MENU);

        utils.makeText("難易度選択に戻る", txtSize, buttonX, buttonY, buttonWidth, buttonHeight, strCol[R_LEVEL], innerCol[R_LEVEL], wakuCol[R_LEVEL]);
    }

    public void title(float buttonX, float buttonY, float buttonWidth, float buttonHeight, float txtSize){
        this.buttonUpdate(buttonX, buttonY, buttonWidth, buttonHeight,
                          R_TITLE, 220, 200, TITLE);

        utils.makeText("タイトルに戻る", txtSize, buttonX, buttonY, buttonWidth, buttonHeight, strCol[R_TITLE], innerCol[R_TITLE], wakuCol[R_TITLE]);
    }

    public void akirameru(float buttonX, float buttonY, float buttonWidth, float buttonHeight, float txtSize){
        if(utils.dotWithRect(mouseX, mouseY, buttonX- buttonWidth / 2, buttonY - buttonHeight / 2, buttonWidth, buttonHeight)){
            if(innerCol[AKIRAMERU][0] > 0)
            innerCol[AKIRAMERU][0] -= 2;
            if(mouseClicked){
                game.mainGame.setFinished(true);
                game.mainGame.setAkirame(true);
            }
        }else{
            if(innerCol[AKIRAMERU][0] < 20)
            innerCol[AKIRAMERU][0] += 2;
        }
        utils.makeText("あきらめる", txtSize, buttonX, buttonY, buttonWidth, buttonHeight, strCol[AKIRAMERU], innerCol[AKIRAMERU], wakuCol[AKIRAMERU]);
    }


    //補助関数
    public void buttonUpdate(float buttonX, float buttonY, float buttonWidth, float buttonHeight,
                      int textNum, float startCol, float endCol, int nextAnime){
        if(utils.dotWithRect(mouseX, mouseY, buttonX- buttonWidth / 2, buttonY - buttonHeight / 2, buttonWidth, buttonHeight)){
            if(innerCol[textNum][0] > endCol)
            innerCol[textNum][0] -= 2;
            if(mouseClicked){
                game.moveToAnime();
                game.setNextScreen(nextAnime);
            }
        }else{
            if(innerCol[textNum][0] < startCol)
            innerCol[textNum][0] += 2;
        }
    }
}
class Game{
    int screen;
    int nextScreen;
    boolean moveAnime = false;

    Title title;
    Menu menu;
    MainGame mainGame;
    Animation animation;

    float oldTime = 0;
    int counter = 0;
    int FPS = 0;

    Game(int firstScreen){
        this.screen = firstScreen;

        title = new Title();
        menu = new Menu();
        mainGame = new MainGame();
        animation = new Animation();
    }

    public void gameLoop(){
        //draw関数の中でループさせるやつ。
        background(0, 0, 100);

        switch(screen){
            case TITLE:
                title.looping();
            break;
            case MENU:
                menu.looping();
            break;
            case MAINGAME:
                mainGame.looping();
            break;
        }
        if(moveAnime){
            animation.looping();
        }
        
        if(debug) this.dispFps();
    }

    public void moveToAnime(){
        this.moveAnime = true;
    }

    public void dispFps(){
        counter++;
        float nowTime = millis();
        if(nowTime - oldTime >= 1000){
            FPS = counter;
            counter = 0;
            oldTime = nowTime;
        }
        fill(0, 0, 50);
        utils.makeText("FPS: " + Integer.toString(FPS), 20, width - 40, height - 15);
    }

    //アクセサメソッド
    public void setScreen(int screen){
        this.screen = screen;
    }
    public void setNextScreen(int screen){
        this.nextScreen = screen;
    }
    public void setMoveAnime(boolean anime){
        this.moveAnime = anime; 
    }

    public int getScreen(){
        return this.screen;
    }
    public int getNextScreen(){
        return this.nextScreen;
    }

}
class MainGame{
    //プレイヤーに関して
    Player player;
    float rayHaba;                  //視界の大きさ

    //3D画面に関して
    final int FIELD_BAR_WIDTH = 7;  //変更はそんなしないほうが良い。これが画面の大きさを生業する。
    final int FIELD_Y_CONTROL = 50; //フィールドの縦y座標のコントロール。真ん中からの調整用。
    float visuAng = 0;
    float startAng = 0;
    float endAng = 0;
    float angMargin = 0;
    int rayIndex = 0;
    
    // スタート位置。ゴール位置。
    float rsx = 0;                  //スタート位置（後に正しい位置に初期化）
    float rsy = 0;                  
    float rgx = 0;                  //ゴール位置（のちに正しい位置に初期化）
    float rgy = 0;
    float rw;                       //スタート位置の横の大きさ
    float rh;                       //スタート位置の縦の大きさ

    //ミニマップに関して。
    ArrayList<Ray> wall;            //壁
    int[][] field;                  //マップの情報保持用の配列
    int MAP_WIDTH;                  //マップ横の大きさ（何マス分あるか）
    int MAP_HEIGHT;                 //マップ縦の大きさ（何マス分あるか）
    int VWIDTH;                     //バーチャルな横の大きさ
    int VHEIGHT;                    //バーチャルな縦の大きさ
    int MAP_SCL;                    //一マス分がどれくらいの大きさか
    final int MAP_START_X = 25;     //ミニマップのx座標
    final int MAP_START_Y = 25;     //ミニマップのy座標
    float map_width;                //ミニマップの実際の横の大きさ
    float map_height;               //ミニマップの実際の縦の大きさ
    boolean visuan;                 //ミニマップを写すか写さないか
    
    //テキスト表示
    boolean textVisuan;             //テキストを写すか写さないか
    int txtNum = 0;
    float[][][] buttonInfo = new float[][][]{
        {//右上のやつ,0
            {width - 70, width - 70, width - 70}, //x
            {20, 55, 90}, //y
            {125},        //width
            {30},         //height
            {15}          //テキストサイズ
        },
        {//ゴールした後のやつ。1
            {width / 4, width / 2, width / 4 * 3},
            {height / 6 * 5, height / 6 * 5, height / 6 * 5},
            {250},
            {50},
            {30}
        }
    };

    //ゲームの要素に関わる
    boolean finished;       //ゴールしたかどうかのフラグ
    boolean again;          //もう一度やるフラグ
    boolean akirame;
    boolean timeStart;      //タイマー開始するかどうか
    float nowTime = 0;      //今の時間
    int time = 0;           //プレイ時間
    float timeX;            //タイマーのx座標
    float timeY;            //タイマーのy座標
    int level;              //レベル、0 ~ 2まで、-1は設定されていないということ。

    MainGame(){
        this.init(-1);
    }

    public void looping(){
        background(0, 0, 0);
        
        player.update();
        player.collision(wall, rayHaba);
        
        this.disp3DField();

        this.dispMap();
        player.show(startAng, endAng, angMargin, rayHaba);

        if(this.textVisuan){
            this.dispTime();
            this.dispText();
        }

        //ゴール判定
        if(!this.finished && utils.dotWithRect(player.pos.getX(), player.pos.getY(), rgx - rw / 2, rgy - rh / 2, rw, rh)){
            this.finished = true;
        }

        if(this.finished){//ゴールしたら
            if(keyR == 0){
                this.textVisuan = !this.textVisuan;
                keyR++;
            }
        }
    }

    public void init(int level){
        this.level = level;

        if(this.level == 0){//簡単
            MAP_WIDTH = 10;
            MAP_HEIGHT = 10;
            MAP_SCL = 10;
            rayHaba = 75;
        }else if(this.level == 1){//普通
            MAP_WIDTH = 15;
            MAP_HEIGHT = 15;
            MAP_SCL = 7;
            rayHaba = 75;
        }else if(this.level == 2){//難しい
            MAP_WIDTH = 20;
            MAP_HEIGHT = 20;
            MAP_SCL = 5;
            rayHaba = 50;
        }

        visuAng = PI / 2;
        angMargin = 0.01f;
        rayIndex = floor(visuAng / angMargin) + 1;

        VWIDTH = MAP_WIDTH * 2 + 1;
        VHEIGHT = MAP_HEIGHT * 2 + 1;

        player = new Player(new MyVector(0, 0), -PI / 4);

        wall = new ArrayList<Ray>();
        field = new int[VHEIGHT][VWIDTH];

        map_width = VWIDTH * MAP_SCL;
        map_height = VHEIGHT * MAP_SCL;

        visuan = false;
        textVisuan = true;

        this.rw = 9;
        this.rh = 9;

        finished = false;
        again = false;
        akirame = false;
        timeStart = false;
        nowTime = 0;

        time = 0;
        timeX =  width / 2;
        timeY =  75;

        /*
            0: 壁に位置
            1: 空白
            2: スタート位置
            3: ゴール位置
            各場所を初期化。
        */
        for (int i = 0; i < VHEIGHT; i++) {
            for (int j = 0; j< VWIDTH; j++) {
                if (i == 0 || i == (VHEIGHT - 1)) {
                    field[i][j] = 1;
                } else if (j == 0 || j == (VWIDTH - 1)) {
                    field[i][j] = 1;
                } else {
                    field[i][j] = 0;
                }

                if (i == (VHEIGHT - 2) && j == 1) {
                    field[i][j] = 2;
                }
                if (i == (VHEIGHT - 2) && j == (VWIDTH - 2)) {
                    field[i][j] = 3;
                }
            }
        }

        /*
            迷路の作成。棒倒し法
        */
        for (int i = 0; i < (MAP_WIDTH - 1); i++) {
            for (int j = 0; j < (MAP_HEIGHT - 1); j++) {
                int ny = 2 * i + 2;
                int nx = 2 * j + 2;
                int where = 0;
                boolean put = false;
                field[ny][nx] = 1;

                while (!put) {
                    if (i == 0) {
                    where = floor(random(0, 4));
                    } else {
                    where = floor(random(0, 3));
                    }

                    if (where == 3) {
                    if (field[ny - 1][nx] != 1) {
                        field[ny - 1][nx] = 1;
                        put = true;
                    }
                    } else if (where == 2) {
                    if (field[ny][nx + 1] != 1) {
                        field[ny][nx + 1] = 1;
                        put = true;
                    }
                    } else if (where == 1) {
                    if (field[ny + 1][nx] != 1) {
                        field[ny + 1][nx] = 1;
                        put = true;
                    }
                    } else if (where == 0) {
                    if (field[ny][nx - 1] != 1) {
                        field[ny][nx - 1] = 1;
                        put = true;
                    }
                    }
                }
            }
        }

        /*
            二つ上のブロックの番号に合わせて、位置を初期化。
        */
        for (int i = 0; i < VHEIGHT; i++) {
            for (int j = 0; j < VWIDTH; j++) {
                if (field[i][j] == 2) {
                    player.pos.set(MAP_START_X + j * MAP_SCL, MAP_START_Y + i * MAP_SCL);
                    rsx = MAP_START_X + j * MAP_SCL;
                    rsy = MAP_START_Y + i * MAP_SCL;
                }
                if (field[i][j] == 3) {
                    rgx = MAP_START_X + j * MAP_SCL;
                    rgy = MAP_START_Y + i * MAP_SCL;
                }

                if (field[i][j] != 1) {
                    continue;
                }
                if (j != (VWIDTH - 1)) {
                    if (field[i][j + 1] == 1) {
                    wall.add(new Ray(new MyVector(MAP_START_X + j * MAP_SCL, MAP_START_Y + i * MAP_SCL), new MyVector(MAP_SCL, 0)));
                    }
                }
                if (i != (VHEIGHT - 1)) {
                    if (field[i + 1][j] == 1) {
                    float gosaY = map(i, 0, VHEIGHT, 0.011f, 0.05f);
                    wall.add(new Ray(new MyVector(MAP_START_X + j * MAP_SCL, MAP_START_Y + i * MAP_SCL), new MyVector(gosaY, MAP_SCL)));
                    }
                }
            }
        }
    }

    public void disp3DField(){
        startAng = player.getAng() - visuAng / 2;
        endAng = player.getAng() + visuAng / 2;

        for (float i = startAng, j = 0; i < endAng; i += angMargin, j++) {
            float nowAng = i;
            Ray pRay = new Ray(player.pos.copy(), new MyVector(cos(nowAng) * rayHaba, sin(nowAng) * rayHaba));
            float prevDist = 1000;
            for (Ray w : wall) {
                if(w.checked){
                    continue;
                }
                MyVector hitPos = pRay.intersection(w);

                if (hitPos == null) continue;

                MyVector dist = new MyVector(0, 0);
                MyVector subP = new MyVector(player.pos.getX(), player.pos.getY());
                float distan = dist.dist(subP.sub(hitPos));
                if (distan < prevDist) {
                    prevDist = distan;
                } else {
                    continue;
                }

                float distance = 6000 / (distan * cos(nowAng - player.getAng()));
                if (distance > 600) distance = 600;
                strokeWeight(1);
                //長さは150の棒
                float startRootY = (height / 2) - (distance / 2) + FIELD_Y_CONTROL;
                float barW = FIELD_BAR_WIDTH;
                float posMargin = (width - barW * rayIndex) / 2;
                Ray viewRoot = new Ray(new MyVector(posMargin + (barW * j), startRootY), new MyVector(0, distance)); 
                fill(0, 0, (distance * 2.5f)/ 6);
                noStroke();
                rect(viewRoot.pos.getX(), viewRoot.pos.getY(), barW, distance);
            }
        }
    }

    public void dispMap(){
        fill(0, 0, 60);
        rect(MAP_START_X - MAP_SCL / 2, MAP_START_Y - MAP_SCL / 2 ,map_width, map_height);

        noStroke();
        rectMode(CENTER);
        fill(200, 100, 100);
        rect(rsx, rsy, 9, 9);
        fill(120, 100, 100);
        rect(rgx, rgy, 9, 9);
        rectMode(CORNER);

        strokeWeight(1);
        stroke(255, 0, 100);
        if (keyE == 0 && (this.finished || debug)) {
            this.visuan  = !this.visuan;
            keyE++;
        }
        if (visuan) {
            for (Ray w : wall) {
                line(w.pos.getX(), w.pos.getY(), w.end.getX(), w.end.getY());//end.pos.x, end.pos.y);
            }
        }
    }

    public void dispTime(){
        if(!this.finished && someKey){
            timeStart = true;
        }
        if(this.finished){
            timeStart = false;
        }

        if(timeStart){
            float timeBuff = millis();
            time = floor((timeBuff - nowTime) / 1000);
        }else{
            nowTime = millis();
            if(!this.finished){
                fill(200, 100, 100);
                utils.makeText("何かボタンを押すとタイマーがスタートします。" , 20, width / 2, height / 5 * 4);
            }
        }

        fill(0, 0, 100);
        if(this.finished){
            timeX = width / 2;
            timeY = height / 2;
            fill(0, 100, 100);
        }
        if(this.akirame){
            utils.makeText("No Time", 40, timeX, timeY);
        }else{
            utils.makeText("Time: " + Integer.toString(time), 40, timeX, timeY);
        }
    }

    public void dispText(){
        if(!this.finished){
            txtNum = 0;
            if(timeStart){
                btn.akirameru(width / 2, height - 25, 150, 30, 20);
            }
        }else{
            if(this.akirame){
                fill(0, 100, 100);
                utils.makeText("残念", 80, width / 2, height / 4);
            }else{
                fill(50, 100, 100);
                utils.makeText("ゴール！", 80, width / 2, height / 4);
            }

            fill(100, 100, 80);
            utils.makeText("Eキー　→　マップ全貌表示/非表示", 25, width / 2, height / 10 * 6.5f);
            utils.makeText("Rキー　→　テキスト表示/非表示", 25, width / 2, height / 10 * 7);

            txtNum = 1;
        }
        //もう一度やる
        btn.again(buttonInfo[txtNum][0][0], buttonInfo[txtNum][1][0], buttonInfo[txtNum][2][0], buttonInfo[txtNum][3][0], buttonInfo[txtNum][4][0]);

        //難易度選択画面に戻る
        btn.level(buttonInfo[txtNum][0][1], buttonInfo[txtNum][1][1], buttonInfo[txtNum][2][0], buttonInfo[txtNum][3][0], buttonInfo[txtNum][4][0]);

        //タイトルに戻る
        btn.title(buttonInfo[txtNum][0][2], buttonInfo[txtNum][1][2], buttonInfo[txtNum][2][0], buttonInfo[txtNum][3][0], buttonInfo[txtNum][4][0]);
    }

    //アクセサメソッド
    public boolean getFinish(){
        return this.finished;
    }
    public boolean getAgain(){
        return this.again;
    }
    public void setAgain(boolean what){
        this.again = what;
    }
    public void setFinished(boolean what){
        this.finished = what;
    }
    public void setAkirame(boolean what){
        this.akirame = what;
    }
}
class Menu{
    float[][] strCol;
    float[][] innerCol;
    float[][] wakuCol;
    float easyCol = 30;
    float normalCol = 110;
    float diffCol = 270;

    //ゲームの設定
    int setLevel = -1;

    Menu(){
        strCol = new float[][] {{0, 0, 0},//easy
                                {0, 0, 0},//normal
                                {0, 0, 0},//diff
                                {0, 0, 0}};//決定
        innerCol = new float[][] {{easyCol, 100, 100},
                                  {normalCol, 100, 80},
                                  {diffCol, 100, 100},
                                  {220, 100, 100}};
        wakuCol = new float[][] {{0, 0, 0},
                                 {0, 0, 0},
                                 {0, 0, 0},
                                 {0, 0, 100}}; 
    }

    public void looping(){
        fill(0, 0, 100);
        if(this.setLevel == 0){
            background(easyCol, 100, 100);
            utils.makeText("EASY", 75, width / 2, height / 5 * 2);
        }else if(this.setLevel == 1){
            background(normalCol, 100, 90);
            fill(0, 0, 80);
            utils.makeText("NORMAL", 75, width / 2, height / 5 * 2);
        }else if(this.setLevel == 2){
            background(diffCol, 100, 100);
            fill(0, 0, 80);
            utils.makeText("DIFFICULT", 75, width / 2, height / 5 * 2);
        }else{
            background(0, 0, 90);
        }
        fill(0, 0, 0);
        utils.makeText("難易度選択", 75, width / 2, height / 4);


        //簡単
        if(this.setLevel == 0) fill(0, 0, 100);
        else fill(0, 0, 0);
        utils.makeText("10 × 10の\n簡単すぎる迷路生成", 25, 30, width / 4, height / 2);
        
        if(utils.dotWithRect(mouseX, mouseY, width / 4 - 75, height / 8 * 4.75f - 25, 150, 50)){
            if(innerCol[0][0] < 40)
            innerCol[0][0] += 1;
            if(mouseClicked){
                game.animation.setSetLevel(0);
                this.setLevel = 0;
            }
        }else{
            if(innerCol[0][0] > easyCol)
            innerCol[0][0] -= 1;
        }
        if(this.setLevel == 0){
            innerCol[0][0] = easyCol;
        }
        utils.makeText("Easy", 30, width / 4, height / 8 * 4.75f, 150, 50, strCol[0], innerCol[0], wakuCol[0]);
        
        //普通
        if(this.setLevel == 1) fill(0, 0, 90);
        else fill(0, 0, 0);
        utils.makeText("15 × 15の\nほどよい迷路生成", 25, 30, width / 2, height / 2);
        if(utils.dotWithRect(mouseX, mouseY, width / 2 - 75, height / 8 * 4.75f - 25, 150, 50)){
            if(innerCol[1][0] > 70)
            innerCol[1][0] -= 2;
            if(mouseClicked){
                game.animation.setSetLevel(1);
                this.setLevel = 1;
            }
        }else{
            if(innerCol[1][0] < normalCol)
            innerCol[1][0] += 2;
        }
        if(this.setLevel == 1){
            innerCol[1][0] = normalCol;
        }
        utils.makeText("Normal", 30, width / 2, height / 8 * 4.75f, 150, 50, strCol[1], innerCol[1], wakuCol[1]);
        
        //難しい
        if(this.setLevel == 2) fill(0, 0, 80);
        else fill(0, 0, 0);
        utils.makeText("20 × 20の\n楽しくない迷路生成", 25, 30, width / 4 * 3, height / 2);
        if(utils.dotWithRect(mouseX, mouseY, width / 4 * 3 - 75, height / 8 * 4.75f - 25, 150, 50)){
            if(innerCol[2][0] > 250)
            innerCol[2][0] -= 2;
            if(mouseClicked){
                game.animation.setSetLevel(2);
                this.setLevel = 2;
            }
        }else{
            if(innerCol[2][0] < diffCol)
            innerCol[2][0] += 2;
        }
        if(this.setLevel == 2){
            innerCol[2][0] = diffCol;
        }
        utils.makeText("Difficult", 30, width / 4 * 3, height / 8 * 4.75f, 150, 50, strCol[2], innerCol[2], wakuCol[2]);
        
        //決定
        if(this.setLevel != -1){
            innerCol[3][1] = 100;
            innerCol[3][2] = 100;
            strCol[3][2] = 100;
            if(utils.dotWithRect(mouseX, mouseY, width / 2 - 75, height / 4 * 3 - 25, 150, 50)){
                if(innerCol[3][0] > 200)
                    innerCol[3][0] -= 2;
                if(mouseClicked && this.setLevel != -1){
                    game.moveToAnime();
                    game.setNextScreen(MAINGAME);
                    game.mainGame.finished = true;
                }
            }else{
                if(innerCol[3][0] < 220)
                innerCol[3][0] += 2;
            }
        }else{
            innerCol[3][1] = 0;
            innerCol[3][2] = 60;
        }
        utils.makeText("Play", 30, width / 2, height / 4 * 3, 150, 50, strCol[3], innerCol[3], wakuCol[3]);
        
        //戻る
        btn.title(width / 2, height / 8 * 7, 150, 30, 15);
    }
}
class Player {
    MyVector pos = new MyVector(0, 0);
    float ang;
    float d = 8;
    MyVector pPos = new MyVector(0, 0);

    Player(MyVector p_pos, float an) {
        this.pos.set(p_pos);
        this.ang = an;
    }

    public void update() {
        this.pPos.set(this.pos);
        if (keyD) {
            this.ang += 0.025f;
        }
        if (keyA) {
            this.ang -= 0.025f;
        }
        if (keyW) {
            this.pos.x += cos(this.ang) * 0.5f;
            this.pos.y += sin(this.ang) * 0.5f;
        }
        if (keyS) {
            this.pos.x -= cos(this.ang) * 0.5f;
            this.pos.y -= sin(this.ang) * 0.5f;
        }
    }

    public void show(float startAng, float endAng, float angMargin, float rayHaba) {
        for (float i = startAng, j = 0; i < endAng; i += angMargin, j++) {
            float nowAng = i;
            Ray pRay = new Ray(this.pos, new MyVector(cos(nowAng) * rayHaba, sin(nowAng) * rayHaba));

            stroke(0, 100, 100, 50);
            line(pRay.pos.getX(), pRay.pos.getY(), pRay.end.getX(), pRay.end.getY());
        }

        noStroke();
        fill(0, 100, 100);
        ellipse(this.pos.x, this.pos.y, this.d, this.d);
    }

    public void collision(ArrayList<Ray> wall, float rayHaba){
        for(Ray w: wall){
            if(!utils.dotWithCircle(w.pos.getX(), w.pos.getY(), this.pos.x, this.pos.y, rayHaba)){
                w.checked = true;
                continue;
            }
            w.checked = false;
            //横に長い場合
            if(!debug){//デバッグ用。
                if(w.pos.getY() - w.end.getY() == 0){
                    if(utils.dotWithRect(this.pos.x, this.pos.y,
                                        w.pos.getX(), w.pos.getY() - 0.25f, w.end.getX()- w.pos.getX(), 0.5f))
                    this.pos.set(this.pPos);
                }else{//それ以外つまり縦に長い。
                    if(utils.dotWithRect(this.pos.x, this.pos.y,
                                        w.pos.getX() - 0.25f, w.pos.getY(), 0.5f, w.end.getY() - w.pos.getY()))
                    this.pos.set(this.pPos);
                }
            }
        }
    }

    //アクセサメソッド
    public float getAng(){
        return this.ang;
    }
}
class Ray{
    MyVector pos = new MyVector();
    MyVector end = new MyVector();
    boolean checked;

    Ray(MyVector pos, MyVector way){
        this.pos = pos;
        this.end = way.add(pos);
        this.checked = false;
    }

    public MyVector makeWay(){
        MyVector pEnd = new MyVector(end.x, end.y);
        return pEnd.sub(pos);
    }

    public MyVector intersection(Ray wall){
        if(abs(this.makeWay().x) < 0.01f) this.makeWay().x = 0.01f;
        if(abs(wall.makeWay().x) < 0.01f) wall.makeWay().x = 0.01f;
        
        float t1 = this.makeWay().y / this.makeWay().x;
        float t2 = wall.makeWay().y / wall.makeWay().x;
        float x1 = this.pos.x;
        float y1 = this.pos.y;
        float x2 = wall.pos.getX();
        float y2 = wall.pos.getY();
        
        float sX = (t1*x1 - t2*x2 - y1 + y2) / (t1 - t2);
        float sY = t1 * (sX - x1) + y1;
        
        if(sX > min(this.pos.x, this.end.x)
        && sX < max(this.pos.x, this.end.x)
        && sX > min(wall.pos.getX(), wall.end.getX())
        && sX < max(wall.pos.getX(), wall.end.getX())){
            return new MyVector(sX, sY);
        }
        return null;
  }
}
class Title{
    String explanation = "ミニマップ上の青い四角がスタート、緑の四角がゴールです\nゴールするとゲームクリアです！\n\n※3Dフィールド上にはスタートやゴールを示す印はないので注意\nミニマップを頼りにしてください。\n\n<操作方法>\n前へ進む → Wキー\n方向転換 → A、Dキー\n後ろに下がる → Sキー";

    public void looping(){
        background(0, 0, 10);

        fill(0, 0, 100);
        utils.makeText("3D迷路", 75, width / 2, height / 4 - 25);

        this.dispExpl(width / 2, height / 2);

        btn.startBtn(width / 2, height / 4 * 3, 150, 50, 30);
    }

    public void dispExpl(float x, float y){//中心座標
        utils.makeText(explanation, 20, 30, x, y);
    }

    // void
}
class Utils{

    public void makeText(String str, float textsize, float x, float y){
        textAlign(CENTER, CENTER);
        textSize(textsize);
        text(str, x, y);
    }
    public void makeText(String str, float textsize, float leading, float x, float y){
        textAlign(CENTER, CENTER);
        textSize(textsize);
        textLeading(leading);
        text(str, x, y);
    }
    public void makeText(String str, float textsize, float x, float y, float w, float h,
                  float[] strCol, float[] innerCol, float[] wakuCol){
        if(strCol.length != 3 || innerCol.length != 3 || wakuCol.length != 3)
        return;

        rectMode(CENTER);
        fill(innerCol[0], innerCol[1], innerCol[2]);
        stroke(wakuCol[0], wakuCol[1], wakuCol[2]);
        rect(x, y + 3, w, h);
        rectMode(CORNER);

        textAlign(CENTER, CENTER);
        textSize(textsize);
        fill(strCol[0], strCol[1], strCol[2]);
        text(str, x, y);
    }

    //あたり判定
    public boolean dotWithRect(float dotX, float dotY, float rectX, float rectY, float rectW, float rectH){
        if(rectX <= dotX && dotX <= rectX + rectW 
        && rectY <= dotY && dotY <= rectY + rectH){
               return true;
           }
        return false;
    }
    public int dotWithRect(float dotX, float dotY, float pDotX, float pDotY, float rectX, float rectY, float rectW, float rectH){
        //0上、1右、2下、3左、4端
        int place = -1;

        if(this.dotWithRect(dotX, dotY, rectX, rectY, rectW, rectH)){
            if(pDotX < rectX){
                place = 3;
            }else if(pDotX > rectX + rectW ){
                place = 1;
            }else if(pDotY < rectH){
                place = 0;
            }else if(pDotY > rectY + rectH){
                place = 2;
            }else{
                place = 4;
            }
        }
        
        return place;
    }
    public boolean rectWithRect(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2){
        float centerX1 = x1 + w1 / 2;
        float centerY1 = y1 + h1 / 2;
        float centerX2 = x2 + w2 / 2;
        float centerY2 = y2 + h2 / 2;
        
        float distX = abs(centerX1 - centerX2);
        float distY = abs(centerY1 - centerY2);
        float sumW = w1 / 2 + w2 / 2;
        float sumH = h1 / 2 + h2 / 2;

        if(distX <= sumW && distY <= sumH){
            return true;
        }
        return false;
    }
    public boolean dotWithCircle(float dotX, float dotY, float circleX, float circleY, float circleR){
        float distX = dotX - circleX;
        float distY = dotY - circleY;
        float sumR  = circleR;

        if(distX * distX + distY * distY <= sumR * sumR){
            return true;
        }
        return false;
    }
    public boolean circleWithCircle(float x1, float y1, float r1, float x2, float y2, float r2){
        float distX = x1 - x2;
        float distY = y1 - y2;
        float sumR  = r1 + r2;

        if(distX * distX + distY * distY <= sumR * sumR){
            return true;
        }
        return false;
    }
}

class MyVector{
    float x;
    float y;

    MyVector(){
        this.x = 0;
        this.y = 0;
    }

    MyVector(float x, float y){
        this.x = x;
        this.y = y;
    }

    public MyVector add(MyVector target){
        this.x += target.getX();
        this.y += target.getY();
        return this;
    }
    public MyVector sub(MyVector target){
        this.x -= target.getX();
        this.y -= target.getY();
        return this;
    }

    public float dist(MyVector target){
        float distX = this.x - target.getX();
        float distY = this.y - target.getY();
        float distan = sqrt(distX * distX + distY * distY);
        return distan;
    }


    //アクセサメソッド
    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }
    public MyVector copy(){
        return this;
    }

    public void set(MyVector target){
        this.x = target.getX();
        this.y = target.getY();
    }
    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }
}
  public void settings() {  size(1200, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "interactiveGameOriginal" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
