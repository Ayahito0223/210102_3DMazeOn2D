class Utils{

    void makeText(String str, float textsize, float x, float y){
        textAlign(CENTER, CENTER);
        textSize(textsize);
        text(str, x, y);
    }
    void makeText(String str, float textsize, float leading, float x, float y){
        textAlign(CENTER, CENTER);
        textSize(textsize);
        textLeading(leading);
        text(str, x, y);
    }
    void makeText(String str, float textsize, float x, float y, float w, float h,
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
    boolean dotWithRect(float dotX, float dotY, float rectX, float rectY, float rectW, float rectH){
        if(rectX <= dotX && dotX <= rectX + rectW 
        && rectY <= dotY && dotY <= rectY + rectH){
               return true;
           }
        return false;
    }
    int dotWithRect(float dotX, float dotY, float pDotX, float pDotY, float rectX, float rectY, float rectW, float rectH){
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
    boolean rectWithRect(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2){
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
    boolean dotWithCircle(float dotX, float dotY, float circleX, float circleY, float circleR){
        float distX = dotX - circleX;
        float distY = dotY - circleY;
        float sumR  = circleR;

        if(distX * distX + distY * distY <= sumR * sumR){
            return true;
        }
        return false;
    }
    boolean circleWithCircle(float x1, float y1, float r1, float x2, float y2, float r2){
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

    MyVector add(MyVector target){
        this.x += target.getX();
        this.y += target.getY();
        return this;
    }
    MyVector sub(MyVector target){
        this.x -= target.getX();
        this.y -= target.getY();
        return this;
    }

    float dist(MyVector target){
        float distX = this.x - target.getX();
        float distY = this.y - target.getY();
        float distan = sqrt(distX * distX + distY * distY);
        return distan;
    }


    //アクセサメソッド
    float getX(){
        return this.x;
    }
    float getY(){
        return this.y;
    }
    MyVector copy(){
        return this;
    }

    void set(MyVector target){
        this.x = target.getX();
        this.y = target.getY();
    }
    void set(float x, float y){
        this.x = x;
        this.y = y;
    }
}
