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

    void looping(){
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
        
        if(utils.dotWithRect(mouseX, mouseY, width / 4 - 75, height / 8 * 4.75 - 25, 150, 50)){
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
        utils.makeText("Easy", 30, width / 4, height / 8 * 4.75, 150, 50, strCol[0], innerCol[0], wakuCol[0]);
        
        //普通
        if(this.setLevel == 1) fill(0, 0, 90);
        else fill(0, 0, 0);
        utils.makeText("15 × 15の\nほどよい迷路生成", 25, 30, width / 2, height / 2);
        if(utils.dotWithRect(mouseX, mouseY, width / 2 - 75, height / 8 * 4.75 - 25, 150, 50)){
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
        utils.makeText("Normal", 30, width / 2, height / 8 * 4.75, 150, 50, strCol[1], innerCol[1], wakuCol[1]);
        
        //難しい
        if(this.setLevel == 2) fill(0, 0, 80);
        else fill(0, 0, 0);
        utils.makeText("20 × 20の\n楽しくない迷路生成", 25, 30, width / 4 * 3, height / 2);
        if(utils.dotWithRect(mouseX, mouseY, width / 4 * 3 - 75, height / 8 * 4.75 - 25, 150, 50)){
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
        utils.makeText("Difficult", 30, width / 4 * 3, height / 8 * 4.75, 150, 50, strCol[2], innerCol[2], wakuCol[2]);
        
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
