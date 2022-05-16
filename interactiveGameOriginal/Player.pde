class Player {
    MyVector pos = new MyVector(0, 0);
    float ang;
    float d = 8;
    MyVector pPos = new MyVector(0, 0);

    Player(MyVector p_pos, float an) {
        this.pos.set(p_pos);
        this.ang = an;
    }

    void update() {
        this.pPos.set(this.pos);
        if (keyD) {
            this.ang += 0.025;
        }
        if (keyA) {
            this.ang -= 0.025;
        }
        if (keyW) {
            this.pos.x += cos(this.ang) * 0.5;
            this.pos.y += sin(this.ang) * 0.5;
        }
        if (keyS) {
            this.pos.x -= cos(this.ang) * 0.5;
            this.pos.y -= sin(this.ang) * 0.5;
        }
    }

    void show(float startAng, float endAng, float angMargin, float rayHaba) {
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

    void collision(ArrayList<Ray> wall, float rayHaba){
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
                                        w.pos.getX(), w.pos.getY() - 0.25, w.end.getX()- w.pos.getX(), 0.5))
                    this.pos.set(this.pPos);
                }else{//それ以外つまり縦に長い。
                    if(utils.dotWithRect(this.pos.x, this.pos.y,
                                        w.pos.getX() - 0.25, w.pos.getY(), 0.5, w.end.getY() - w.pos.getY()))
                    this.pos.set(this.pPos);
                }
            }
        }
    }

    //アクセサメソッド
    float getAng(){
        return this.ang;
    }
}
