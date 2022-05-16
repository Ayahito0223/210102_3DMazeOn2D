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

    void looping(){
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

    void init(int level){
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
        angMargin = 0.01;
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
                    float gosaY = map(i, 0, VHEIGHT, 0.011, 0.05);
                    wall.add(new Ray(new MyVector(MAP_START_X + j * MAP_SCL, MAP_START_Y + i * MAP_SCL), new MyVector(gosaY, MAP_SCL)));
                    }
                }
            }
        }
    }

    void disp3DField(){
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
                fill(0, 0, (distance * 2.5)/ 6);
                noStroke();
                rect(viewRoot.pos.getX(), viewRoot.pos.getY(), barW, distance);
            }
        }
    }

    void dispMap(){
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

    void dispTime(){
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

    void dispText(){
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
            utils.makeText("Eキー　→　マップ全貌表示/非表示", 25, width / 2, height / 10 * 6.5);
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
    boolean getFinish(){
        return this.finished;
    }
    boolean getAgain(){
        return this.again;
    }
    void setAgain(boolean what){
        this.again = what;
    }
    void setFinished(boolean what){
        this.finished = what;
    }
    void setAkirame(boolean what){
        this.akirame = what;
    }
}
