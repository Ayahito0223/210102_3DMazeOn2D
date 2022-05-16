/*
格ゲーは断念。シューティングゲームにします。
やっぱ、迷路ゲームにします。

*/
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

void setup(){
    size(1200, 800);
    colorMode(HSB, 360, 100, 100);
    frameRate(fps);

    PFont font = createFont("Meiryo", 50);
    textFont(font);

    game = new Game(TITLE);
    utils = new Utils();
    btn = new Button();
}

void draw(){
    game.gameLoop();
    // println(mouseClicked);
}


void mousePressed(){
    mouseClicked = true;
}
void mouseReleased(){
    mouseClicked = false;
}
void keyPressed(){
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
void keyReleased() {
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
