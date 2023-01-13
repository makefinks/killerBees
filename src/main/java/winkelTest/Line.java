package winkelTest;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Line {

	private static final double pi = Math.PI;
	private static int posKX = 100, posKY = 300;
	double angle;
	int angleDeg;
	double length;
	Point sp, ep;
	Color farbe;
	private int dx, dy;

	public Line(Point a, Point b) {
		sp = a;
		ep = b;
		dx = ep.x - sp.x;
		dy = ep.y - sp.y;
		length = Math.sqrt((dx) * (dx) + (dy) * (dy));
		if (dx != 0) {

			angle = Math.atan2(dy, dx);

			// System.out.println("angle:"+angle);
		} else {
			//System.err.println("DX=0");
		}

		angleDeg = (int) ((angle / (2 * pi)) * 360);

	}

	public Line getSpiegelLine(Line spiegel) {

		int newangle = spiegel.getAngle() - (this.angleDeg - spiegel.getAngle());
		// richtige berechnung f�r einfallswinkel = ausfallswinkel
		

//		if(newangle<-180) {
//			newangle+=180;
//		} 
//		if(newangle>180) {
//			newangle-=180;
//		}
		
		return Line.LineOfAngle(newangle);
	}

	public int getAngle() {
		return angleDeg;
	}

	public Point getStartPunkt() {
		return sp;
	}

	public Point getEndPunkt() {
		return ep;
	}


	public Point getEPRev() {
		return new Point(posKX - dx, posKY - dy);
	}

	public void draw(Graphics2D g) {

		g.drawLine(sp.x, sp.y, ep.x, ep.y);
		g.drawLine(sp.x + 1, sp.y, ep.x + 1, ep.y);
		g.drawLine(sp.x - 1, sp.y, ep.x - 1, ep.y);
		g.drawLine(sp.x, sp.y + 1, ep.x, ep.y + 1);
		g.drawLine(sp.x, sp.y - 1, ep.x, ep.y - 1);
	}

	public static Line LineOfAngle(int deg) {

		Line l = new Line(new Point(posKX, posKY), new Point((int) (posKX + Math.cos(Math.toRadians(deg)) * 100),
				(int) (posKY + Math.sin(Math.toRadians(deg)) * 100)));

		return l;
	}

	protected Line InKreis() {

		return new Line(new Point(posKX, posKY),
				new Point(posKX + (int) ((dx * 100) / this.length), posKY + (int) ((dy * 100) / this.length)));

	}

	public void print() {
		System.out.println(
				"P: " + "angle=" + angle + " AngleDeg=" + angleDeg + " DX=" + dx + " DY=" + dy + " lenght=" + length);
	}

	public String toSting() {
		return "P: " + "angle=" + angle + " AngleDeg=" + angleDeg + " DX=" + dx + " DY=" + dy + " lenght=" + length;
	}

}
