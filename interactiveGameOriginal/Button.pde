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

    void startBtn(float buttonX, float buttonY, float buttonWidth, float buttonHeight, float txtSize){
        this.buttonUpdate(buttonX, buttonY, buttonWidth, buttonHeight,
                          START, 220, 200, MENU);

        utils.makeText("START", txtSize, buttonX, buttonY, buttonWidth, buttonHeight, strCol[START], innerCol[START], wakuCol[START]);
    }

    void again(float buttonX, float buttonY, float buttonWidth, float buttonHeight, float txtSize){
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

    void level(float buttonX, float buttonY, float buttonWidth, float buttonHeight, float txtSize){
        this.buttonUpdate(buttonX, buttonY, buttonWidth, buttonHeight,
                          R_LEVEL, 220, 200, MENU);

        utils.makeText("難易度選択に戻る", txtSize, buttonX, buttonY, buttonWidth, buttonHeight, strCol[R_LEVEL], innerCol[R_LEVEL], wakuCol[R_LEVEL]);
    }

    void title(float buttonX, float buttonY, float buttonWidth, float buttonHeight, float txtSize){
        this.buttonUpdate(buttonX, buttonY, buttonWidth, buttonHeight,
                          R_TITLE, 220, 200, TITLE);

        utils.makeText("タイトルに戻る", txtSize, buttonX, buttonY, buttonWidth, buttonHeight, strCol[R_TITLE], innerCol[R_TITLE], wakuCol[R_TITLE]);
    }

    void akirameru(float buttonX, float buttonY, float buttonWidth, float buttonHeight, float txtSize){
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
    void buttonUpdate(float buttonX, float buttonY, float buttonWidth, float buttonHeight,
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
