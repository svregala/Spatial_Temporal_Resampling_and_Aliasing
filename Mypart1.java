/*
Name: Steve Regala
Email: sregala@usc.edu
ID: 7293040280
Due Date: 2/13/2023

Homework 1: PART 1 - Spatial Resampling and Aliasing
*/


import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.lang.Math;

public class Mypart1 {

	JFrame frame;
	JLabel lbIm1;
	JLabel lbIm2;
	BufferedImage img;
	BufferedImage img_modified;
	int width = 512;
	int height = 512;

	// Draws a black line on the given buffered image from the pixel defined by (x1, y1) to (x2, y2)
	private void drawLine(BufferedImage image, int x1, int y1, int x2, int y2) {
		Graphics2D g = image.createGraphics();
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1));
		g.drawLine(x1, y1, x2, y2);
		g.drawImage(image, 0, 0, null);
	}

	// Draws a plain white image
	private void drawWhiteSpace(BufferedImage img){
		for(int y = 0; y < img.getHeight(); y++){
			for(int x = 0; x < img.getWidth(); x++){
				// byte a = (byte) 255;
				byte r = (byte) 255;
				byte g = (byte) 255;
				byte b = (byte) 255;

				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
				img.setRGB(x,y,pix);
			}
		}
	}

	// Draws a black border around both images
	public void drawBlackBorder(BufferedImage image){
		drawLine(image, 0, 0, image.getWidth()-1, 0);				// top edge
		drawLine(image, 0, 0, 0, image.getHeight()-1);				// left edge
		drawLine(image, 0, image.getHeight()-1, image.getWidth()-1, image.getHeight()-1);	// bottom edge
		drawLine(image, image.getWidth()-1, image.getHeight()-1, image.getWidth()-1, 0); 	// right edge
	}

	// Function to draw ORIGINAL image on the left
	public BufferedImage drawOriginalImage(int line_num){
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		drawWhiteSpace(result);		// set up white space (canvas)
		drawBlackBorder(result);	// draw borders on canvas

		// start at center, n>0 -- always start at origin, i.e. x1=255, y1=255
		// if n==2, then degree between them is 360/2 = 180, n==3, 360/3= 120; n==4, 360/4=90, n==5, 360/5=72
		double theta = 360.0/line_num;
		double angle = 0;
		for(int i=0; i<line_num; i++){
			double x_rad = Math.toRadians(angle);
			double y_rad = Math.toRadians(angle);

			//double radian = Math.toRadians(i*theta);
			int x = (int)(Math.sqrt(2)*0.5*width*Math.cos(x_rad));
			int y = (int)(Math.sqrt(2)*0.5*width*Math.sin(y_rad));

			angle += theta;

			drawLine(result, 255, 255, x+255, y+255);
		}
		return result;
	}


	// Create a deep copy of bufferedImage to prevent null pointer exceptions
	// By creating a copy, we are also preventing the original image from being altered
	// CITATION: https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	private BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	// Function to remove aliasing by passing the original image to a low-pass filter
	// Employs th 3x3 neighborhood average technique - take a 3x3 neighborhood and average
	// the value of all the 9 pixels that you have there and copy the average value back in
	private BufferedImage removeAliasing(BufferedImage image){
		// initialize 2 arrays to get neighbors on both x and y-axis
		int[] x_neighbor = new int[] {-1,0,1};
		int[] y_neighbor = new int[] {-1,0,1};

		// Create a deep copy of the buffered image to prevent null pointer exceptions
		BufferedImage image_copy = deepCopy(image);
		// remove aliasing first - start 1 before the edges of canvas
		for(int y=1; y<image.getHeight()-1; y++){
			for(int x=1; x<image.getWidth()-1; x++){
				int sum_R=0, sum_G=0, sum_B=0;

				for(int i=0; i<x_neighbor.length; i++){
					for(int j=0; j<y_neighbor.length; j++){
						int pixel = image.getRGB(x+x_neighbor[i], y+y_neighbor[j]);
						sum_R += (pixel >> 16) & 0xff;
						sum_G += (pixel >> 8) & 0xff;
						sum_B += (pixel) & 0xff;
					}
					int avg_R=sum_R/9, avg_G=sum_G/9, avg_B=sum_B/9;
					int pix = 0xff000000 | ((avg_R & 0xff) << 16) | ((avg_G & 0xff) << 8) | (avg_B & 0xff);
					image_copy.setRGB(x,y,pix);
				}
			}
		}
		return image_copy;
	}

	// Function to draw modified image
	// 2 options: (1) draw scaled image WITH aliasing OR (2) draw scaled image WITHOUT aliasing
	public BufferedImage drawModifiedImage(double scale_factor, int alias_bool){
		int width_mod = (int)(width*scale_factor);
		int height_mod = (int)(height*scale_factor);
		BufferedImage result = new BufferedImage(width_mod, height_mod, BufferedImage.TYPE_INT_RGB);
		drawWhiteSpace(result);

		// CASE 1: ALIASING - Just scale image
		if(alias_bool==0){
			for(int y=0; y<height_mod; y++){
				for(int x=0; x<width_mod; x++){
					int p = img.getRGB((int)(x/scale_factor), (int)(y/scale_factor));
					result.setRGB(x,y,p);
				}
			}
		}
		// CASE 2: NO ALIASING - Get rid of aliasing and scale image
		else if(alias_bool==1) {
			BufferedImage img_removeAlias = removeAliasing(img);
			for(int y=0; y<height_mod; y++){
				for(int x=0; x<width_mod; x++){
					int p = img_removeAlias.getRGB((int)(x/scale_factor), (int)(y/scale_factor));
					result.setRGB(x,y,p);
				}
			}
		}
		drawBlackBorder(result);
		return result;
	}

	public void showIms(String[] args){

		int num_lines = Integer.parseInt(args[0]);
		double scale = Double.parseDouble(args[1]);
		int alias = Integer.parseInt(args[2]);

		// CASE 1: Do part 1 (Spatial Resampling and Aliasing) - draw original image, scale, keep/eliminate aliasing
		// Draw ORIGINAL Image on the left
		img = drawOriginalImage(num_lines);

		// Draw MODIFIED Image on the right
		img_modified = drawModifiedImage(scale, alias);

		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);

		JLabel lbText1 = new JLabel("Original image (Left)");
		lbText1.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lbText2;
		if(alias==0){
			lbText2 = new JLabel("Image after modification (Right) - ALIASING");
		}else{
			lbText2 = new JLabel("Image after modification (Right) - ANTI-ALIASING");
		}

		lbText2.setHorizontalAlignment(SwingConstants.CENTER);
		lbIm1 = new JLabel(new ImageIcon(img));
		lbIm2 = new JLabel(new ImageIcon(img_modified));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		frame.getContentPane().add(lbText1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		frame.getContentPane().add(lbText2, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		frame.getContentPane().add(lbIm1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		frame.getContentPane().add(lbIm2, c);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		Mypart1 ren = new Mypart1();
		ren.showIms(args);
	}
}