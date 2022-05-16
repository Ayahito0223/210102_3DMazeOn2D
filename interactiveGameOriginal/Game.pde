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

    void gameLoop(){
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

    void moveToAnime(){
        this.moveAnime = true;
    }

    void dispFps(){
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
    void setScreen(int screen){
        this.screen = screen;
    }
    void setNextScreen(int screen){
        this.nextScreen = screen;
    }
    void setMoveAnime(boolean anime){
        this.moveAnime = anime; 
    }

    int getScreen(){
        return this.screen;
    }
    int getNextScreen(){
        return this.nextScreen;
    }

}
