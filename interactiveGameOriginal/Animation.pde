class Animation{
    final float corner = dist(width / 2, height / 2, width, height);
    boolean finished = false;
    int screen = 0;
    float startAn = 0;
    float endAn = 0;
    float dAn = 6;

    //ゲームの設定
    int setLevel = -1;

    void looping(){
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

    void setSetLevel(int level){
        this.setLevel = level;
    }
}
