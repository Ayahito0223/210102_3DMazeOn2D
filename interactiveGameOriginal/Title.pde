class Title{
    String explanation = "ミニマップ上の青い四角がスタート、緑の四角がゴールです\nゴールするとゲームクリアです！\n\n※3Dフィールド上にはスタートやゴールを示す印はないので注意\nミニマップを頼りにしてください。\n\n<操作方法>\n前へ進む → Wキー\n方向転換 → A、Dキー\n後ろに下がる → Sキー";

    void looping(){
        background(0, 0, 10);

        fill(0, 0, 100);
        utils.makeText("3D迷路", 75, width / 2, height / 4 - 25);

        this.dispExpl(width / 2, height / 2);

        btn.startBtn(width / 2, height / 4 * 3, 150, 50, 30);
    }

    void dispExpl(float x, float y){//中心座標
        utils.makeText(explanation, 20, 30, x, y);
    }

    // void
}
