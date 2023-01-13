package winkelTest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Zeichenfeld {

	private int hoehe, breite;
	private int posX, posY;
	private Rectangle rec;

	private Point p1, p2;

	public Zeichenfeld(int x, int y, int h, int b) {
		hoehe = h;
		breite = b;
		posX = x;
		posY = y;
		rec = new Rectangle(x, y, b, h);
		p1 = new Point(0, 0);
		p2 = new Point(0, 0);

	}

	public void klickInFeld(int x, int y) {
		p1 = p2;
		p2 = new Point(x, y);
	}

	public void klick(int x, int y) {

		x -= posX;
		y -= posY;
		p1 = p2;
		p2 = new Point(x, y);
	}

	public Line getLine() {
		return new Line(p1, p2);
	}

	public boolean inFeld(int x, int y) {
		return rec.contains(new Point(x, y));

	}

	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(this.posX, this.posY, this.breite, this.hoehe);
		g.setColor(Color.RED);
		g.drawLine(p1.x + posX, p1.y + posY, p2.x + posX, p2.y + posY);
	}

}
