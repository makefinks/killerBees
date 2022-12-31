	import java.awt.*;
	import java.security.AllPermission;
	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.logging.Logger;

	import javax.swing.JPanel;


	public class Canvas extends JPanel {

		Logger log = Logger.getLogger("CanvasLog");
		ArrayList<Vehicle> 			allVehicles;
		ArrayList<Target> 			allTargets;

		Double[][] winkel;
		double pix;
		boolean firstDraw;

		static int height;
		static int width;


		Canvas(ArrayList<Vehicle> allVehicles,ArrayList<Target> allTargets, double pix, Double[][] winkel){
			this.allVehicles = allVehicles;
			this.allTargets = allTargets;
			this.pix         = pix;
			this.setBackground(Color.WHITE);
			setPreferredSize(new Dimension(1000,800));
			log.info("Simulation Canvas created");
			this.winkel = winkel;
		}

		public Polygon kfzInPolygon(Vehicle fz){
			Polygon   q = new Polygon();
			int    l    = (int)(fz.FZL/pix);
			int    b    = (int)(fz.FZB/pix);
			int    x    = (int)(fz.pos[0]/pix);
			int    y    = (int)(fz.pos[1]/pix);
			int    dia  = 5 * (int)(Math.sqrt(Math.pow(l/2, 2)+Math.pow(b/2, 2)));
			double    t = Vektorrechnung.winkel(fz.vel);
			double phi1 = Math.atan(fz.FZB/fz.FZL);
			double phi2 = Math.PI-phi1;
			double phi3 = Math.PI+phi1;
			double phi4 = 2*Math.PI-phi1;
			int      x1 = (int)(x+(dia*Math.cos(t+  phi1)));
			int      y1 = (int)(y+(dia*Math.sin(t+  phi1)));
			int      x2 = (int)(x+(dia*Math.cos(t+  phi2)));
			int      y2 = (int)(y+(dia*Math.sin(t+  phi2)));
			int      x3 = (int)(x+(dia*Math.cos(t+  phi3)));
			int      y3 = (int)(y+(dia*Math.sin(t+  phi3)));
			int      x4 = (int)(x+(dia*Math.cos(t+  phi4)));
			int      y4 = (int)(y+(dia*Math.sin(t+  phi4)));
			q.addPoint(x1, y1);
			q.addPoint(x2, y2);
			q.addPoint(x3, y3);
			q.addPoint(x4, y4);
			return q;
		}



		@Override
		public void paintComponent(Graphics g) {

			width = getWidth();
			height = getHeight();
			super.paintComponent(g);

			//log.info("Canvas paintComponent called");

			Graphics2D g2d = (Graphics2D) g;

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				for(int x=0;x<winkel.length; x++){
					for(int y=0; y<winkel[0].length; y++){
						if(winkel[x][y] != null){
							g2d.drawLine((int) (x/pix),(int) (y/pix),(int) (x/pix),(int) (y/pix));
						}
					}
				}

			for(int i=0;i<allVehicles.size();i++){
				Vehicle fz = allVehicles.get(i);
				Polygon q = kfzInPolygon(fz);

				//Draw velocity
				g2d.setColor(Color.BLUE);
				g2d.drawLine((int) (fz.pos[0]/pix), (int) (fz.pos[1]/pix), (int) ((fz.pos[0] + fz.vel[0])/pix), (int) ((fz.pos[1] + fz.vel[1])/pix));

				if(fz.type==1)g2d.setColor(Color.RED);
				else 		  g2d.setColor(Color.BLACK);

				g2d.draw(q);

				//g2d.fillOval((int) fz.pos[0], (int) fz.pos[1], 10, 10);

				int    x  = (int)(fz.pos[0]/pix);
				int    y  = (int)(fz.pos[1]/pix);

				if(fz.type==1){
					int seite = (int)(fz.rad_zus/pix);
					//g2d.drawOval(x-seite, y-seite, 2*seite, 2*seite);
					seite = (int)(fz.rad_sep/pix);
					//g2d.drawOval(x-seite, y-seite, 2*seite, 2*seite);

				}
			}

				 for(Target t : allTargets){
				 g2d.setColor(Color.RED);
				 g2d.drawOval((int) Math.round(t.pos[0]), (int) Math.round(t.pos[1]), 1, 1);
			 }

		}
	}
