class Ray{
    MyVector pos = new MyVector();
    MyVector end = new MyVector();
    boolean checked;

    Ray(MyVector pos, MyVector way){
        this.pos = pos;
        this.end = way.add(pos);
        this.checked = false;
    }

    MyVector makeWay(){
        MyVector pEnd = new MyVector(end.x, end.y);
        return pEnd.sub(pos);
    }

    MyVector intersection(Ray wall){
        if(abs(this.makeWay().x) < 0.01) this.makeWay().x = 0.01;
        if(abs(wall.makeWay().x) < 0.01) wall.makeWay().x = 0.01;
        
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
