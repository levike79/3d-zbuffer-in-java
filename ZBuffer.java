
package j3d;

import java.awt.*;
import java.awt.image.BufferedImage;




class ZBuffer
{
    private Perspective persp;
    private short[][] zb;
    private Graphics g;
    private BufferedImage bi;
    
    
    Graphics getG() { return g; }
    BufferedImage getBi() { return bi; }
    


    
    ZBuffer(Graphics g, BufferedImage bi, Perspective persp)
    {
	this.g = g;
	this.bi = bi;
	this.persp = persp;
	zb = new short [persp.width][persp.height];
	
	clear();
	
	ys = new short [persp.height];

	for (short i=0; i < persp.height; i++) ys[i] = 5;
    }


    
    private boolean update(float z, short x, short y)
    {
	short s = toZB(z);
	if (s < /* <= ? */ zb[x][y]) { zb[x][y] = s; return true; }
	return false;
    }



    
    private byte[] points = new byte [3];
    
    
    void drawTriangle(final short[] xPoints, final short[] yPoints 
		      /*, Graphics g, BufferedImage bi*/)
    {
	final int rgb = g.getColor().getRGB();
	
	{
	    short min = yPoints[0];
	    byte imin = 0;
	    byte i;
	    /*for (i=1; i < (byte)3; i++)
	      if (min > yPoints[i]) {
	      min = yPoints[i];
	      imin = i;
	      }*/
	
	    if (min > yPoints[1]) { min = yPoints[1]; imin = 1; }
	    if (min > yPoints[2]) { min = yPoints[2]; imin = 2; }
	
	
	    points[0] = imin;
	
	    i = (byte)( (imin + (byte)1) % (byte)3 );
	    byte j=  (byte) ( (i + (byte)1) % (byte)3 );
	    
	    if (yPoints[i] < yPoints[j]) {
		points[1] = i;
		points[2] = j;
	    }
	    else {
		points[2] = i;
		points[1] = j;
	    }
	}

	


	if (yPoints[ points[0] ] == yPoints[ points[1]] ) {
	    if (xPoints[ points[0]] < xPoints[ points[1]] ) {
		
		draw_line0(xPoints[points[0]], yPoints[points[0]],
			   xPoints[points[2]], yPoints[points[2]]);
		
		draw_line1(xPoints[points[1]], yPoints[points[1]],
			   xPoints[points[2]], yPoints[points[2]] /*, bi*/, rgb);
		
	    }
	    else {
		draw_line0(xPoints[points[1]], yPoints[points[1]],
			   xPoints[points[2]], yPoints[points[2]]);
		
		draw_line1(xPoints[points[0]], yPoints[points[0]],
			   xPoints[points[2]], yPoints[points[2]], rgb );
	    }
	    
	}

	
	else if (yPoints[points[1]] == yPoints[points[2]] ) {
	    //System.out.println("Haho!!");
	    if (xPoints[points[1]] < xPoints[points[2]]) {
		draw_line0(xPoints[points[0]], yPoints[points[0]],
			   xPoints[points[1]], yPoints[points[1]]);
		draw_line1(xPoints[points[0]], yPoints[points[0]],
			   xPoints[points[2]], yPoints[points[2]], rgb );
	    }
	    else {
		draw_line0(xPoints[points[0]], yPoints[points[0]],
			   xPoints[points[2]], yPoints[points[2]]);
		draw_line1(xPoints[points[0]], yPoints[points[0]],
			   xPoints[points[1]], yPoints[points[1]] ,rgb );
	    }
	}
	else {
	    short a = (short)(yPoints[ points[2] ] - yPoints [points[0]]);
	    short b = (short)(xPoints[ points[0] ] - xPoints [points[2]]);
	    int c = -a * xPoints[points[0]] - b * yPoints[points[0]];
	    int n = a * xPoints[points[1]] +
		b * yPoints[points[1]] + c;
 
	    if (n > 0) {
		
		//System.out.println("Hallo1");
		draw_line0(xPoints[points[0]], yPoints[points[0]],
			   xPoints[points[2]], yPoints[points[2]]);

		draw_line1(xPoints[points[0]], yPoints[points[0]],
			   xPoints[points[1]], yPoints[points[1]] ,rgb );
		
		draw_line1(xPoints[points[1]], yPoints[points[1]],
			   xPoints[points[2]], yPoints[points[2]] ,rgb );
	    }
	    else if (n < 0) {
		//System.out.println("Hallo2");
		draw_line0(xPoints[points[0]], yPoints[points[0]],
			   xPoints[points[1]], yPoints[points[1]]);
		
		draw_line0(xPoints[points[1]], yPoints[points[1]],
			   xPoints[points[2]], yPoints[points[2]]);
		
		draw_line1(xPoints[points[0]], yPoints[points[0]],
			   xPoints[points[2]], yPoints[points[2]], rgb);
	    }
	}
    }

    

    
    
    
    private float[] _r0=null,  _n = null;

    
    void addTriangle(short[] xPoints, short[] yPoints, float[] r0, float[] n
		     /*,     Graphics g, BufferedImage bi*/ )
    {
	_r0 = r0;
	_n = n;
	
	drawTriangle(xPoints, yPoints );

	
	
    }
    


    
    
    void clear()
    {
	for (short i=0; i < persp.width; i++)
	    for (short j=0; j < persp.height; j++)
		zb[i][j] = Short.MAX_VALUE;
    }
    
    

    void clearRect(short x1, short y1, short x2, short y2)
    {
	for ( ; x1 <= x2; x1++)
	    for ( short j=y1 ; j <= y2; j++)
		zb[x1][j] = Short.MAX_VALUE;
    }
    


    short toZB(float z)
    {
	return (short) 
	    ((int)Short.MIN_VALUE + 
	     (int)Math.round( (((float) ((int)Short.MAX_VALUE-
					 (int)Short.MIN_VALUE)) *
			       (z-persp.znear)) / (persp.zfar-persp.znear)) );
    }







    private void putpixel(short x, short y, int rgb)
    {

	bi.setRGB(x, y, rgb );
    }
    


    
    private void putpixelZ(short x, short y, int rgb)
    {

	if ( x < (short)persp.width && 
	     x >= (short)0 && 
	     y < (short)persp.height && 
	     y >= (short)0 ) 

	    {
		/* z = az + mu * ez ,where
		   ez = znear
		   a = (0, 0, 0)
		   ex = x'
		   ey = y'
		   mu =  < n, r0-a> / < n, e >
		*/
	
		float ex = 
		    ((float)(x - persp.width / (short)2)) / persp.zoom ;  

		float ey = 
		    ((float)(y - persp.height / (short)2)) / persp.zoom;
		
		float mu = 
		    (_n[0]*_r0[0] + _n[1]*_r0[1] + _n[2]*_r0[2] ) /
		    (_n[0]*ex + _n[1]*ey + _n[2]*persp.znear);
		
		if ( update(mu * persp.znear, x, y)  )
		    putpixel(x, y,  rgb);
	    }
    }

    

    void putpixel(short x, short y)
    {
	putpixel(x, y, g.getColor().getRGB() );
    }
    


    
    private void setYs(short i, short x) 
    {
	if (i < persp.height && i >= 0) ys[i] = x;
    }



    private short[] ys ;

    
    private void draw_line0(short x1, short y1, short x2, short y2)
    {
	short a = (short)(y2 - y1); //a > 0
	short b = (short)(x1 - x2);
	if (b == 0) { 
	    while (y2 >=  y1 ) {
		//ys[y1] = x1;
		setYs(y1, x1);
		y1++;
	    }
	}
	
	else {
	    
	    if ( a < -b  ) {
		short d = (short)((short)2*a+b);
		for (;;) {
		    
		    //putpixel(x1, y1, bi, rgb );
		    x1++;
		    if (x1 > x2) { 
			//ys[y2] = x1; 
			setYs(y2, x1);
			break; 
		    }		    
		    if (d <= 0) { d += (short)2*a; }
		    else { 
			//System.out.println("0\t1");
			//ys[y1] = x1; 
			setYs(y1, x1);
			y1++;  d += (short)2*(a+b); 
		    }

		}
	    }
	    else if ( b < 0 /*a >= -b*/ ) {
		short d = (short)(a+(short)2*b);
		for (;;) {
		    
		    //putpixel(x1, y1, bi, rgb );
		    //System.out.println("0\t2");
		    //ys[y1] = x1; 
		    setYs(y1, x1);
		    y1++;
		    
		    if (y1 > y2) break;
		    if (d <= 0) { d+=(short)2*(a+b);  x1++; }
		    else { d += (short)2*b; }

		}
	    }



	    else if ( a > b) {
		short d = (short)( (short)2 * b - a);
		for (;;) {
		    //System.out.println("0\t3");
		    //ys[y1] = x1;
		    setYs(y1, x1);
		    y1++;
		    if (y1 > y2) break;
		    if (d <= 0) { d += (short)2 * b; }
		    else { d += (short)2 * (b-a); x1--; }
		}
	    }

	    

	    
	    else if (a <= b) {
		short d = (short) (b - (short)2 * a);
		for (;;) {
		    x1--;
		    if (x1 < x2) { 
			//ys[y2]=x1; 
			setYs(y2, x1);
			break; 
		    }
		    if (d <= 0) { 
			d += (short)2 * (b-a);
			//System.out.println("0\t4");
			//ys[y1] = x1;
			setYs(y1, x1);
			y1++;
		    }
		    else {
			d -= (short)2 * a;
		    }
		}
	    }
	}
    }



    
    private short getYs(short i)
    {
	if ( i >= 0 && i < persp.height) return ys[i];
	return 32000;
    }

    

    private void draw_line1(short x1, short y1, short x2, short y2, int rgb )
    {
	short a = (short)(y2 - y1); //a > 0
	short b = (short)(x1 - x2);

	if (b == 0) { 
	    while (y2 >=  y1 ) {
		for (short i= getYs(y1); i < x1 ; i++) putpixelZ(i, y1,  rgb);
		y1++;
	    }
	}
	
	else {
	    
	    if ( a < -b  ) {
		short d = (short)((short)2*a+b);
		for (;;) {
		    
		    //putpixel(x1, y1, bi, rgb );
		    x1++;
		    if (x1 > x2) break;
		    if (d <= 0) { d += (short)2*a; }
		    else { 
			//getYs(y1) = x1; 
			//System.out.println("\t1");
			for (short i= getYs(y1); i < x1 ; i++) 
			    putpixelZ(i, y1, rgb);
			y1++;  d += (short)2*(a+b); 
		    }
		}
	    }
	    else if ( b < 0 /*a >= -b*/ ) {
		short d = (short)(a+(short)2*b);
		for (;;) {
		    
		    //putpixel(x1, y1, bi, rgb );
		    //getYs(y1) = x1; 
		    //System.out.println("\t2");
		    for (short i= getYs(y1); i < x1 ; i++) putpixelZ(i, y1,  rgb);
		    y1++;
		    
		    if (y1 > y2) break;
		    if (d <= 0) { d+=(short)2*(a+b);  x1++; }
		    else { d += (short)2*b; }

		}
	    }



	    else if ( a > b) {
		short d = (short)( (short)2 * b - a);
		for (;;) {
		    //getYs(y1) = x1;
		    //System.out.println("\t3");
		    for (short i= getYs(y1); i < x1 ; i++) putpixelZ(i, y1, rgb);
		    y1++;
		    if (y1 > y2) break;
		    if (d <= 0) { d += (short)2 * b; }
		    else { d += (short)2 * (b-a); x1--; }
		}
	    }

	    

	    
	    else if (a <= b) {
		short d = (short) (b - (short)2 * a);
		for (;;) {
		    x1--;
		    if (x1 < x2) break;
		    if (d <= 0) { 
			d += (short)2 * (b-a);
			//getYs(y1) = x1;

			//System.out.println("\t4");
			for (short i= getYs(y1); i < x1 ; i++) 
			    putpixelZ(i, y1,  rgb);
			y1++;
		    }
		    else {
			d -= (short)2 * a;
		    }
		}
	    }
	}
    }









	
    void drawLine(short x1, short y1, short x2, short y2)
    {
	if (x1 > x2) {
	    short tmp;
	    tmp = x1; x1 = x2; x2 = tmp;
	    tmp = y1; y1 = y2; y2 = tmp;
	}
	short a = (short)(y2 - y1);
	short b = (short)(x1 - x2);
	
	int rgb = g.getColor().getRGB();

	byte s = y2 > y1 ? (byte)1 : (byte)-1;
	if (b == 0) { 
	    while (y2 != y1 - s) {

		//if (x1< persp.width && x1 >= 0 && y1 < persp.height && y1 >= 0)
		putpixel(x1, y1, rgb );
		
		y1 += s;
	    }
	}
	else if (a == 0) {
	    byte sx = x2 > x1 ? (byte)1 : (byte)-1;
	    while (x2 != x1 - sx) { 
		//if (x1< persp.width && x1 >= 0 && y1 < persp.height && y1 >= 0)
		putpixel(x1, y1, rgb );
		x1 += sx;
	    }
	}


	else {
	    
	    //b = -b;
	    //float m = (float)a / (float)-b;
	    //short c = (short)(a * x1 + b * y1);
	    
	    
	    if ( a > -b /*m > 1.0f*/ ) {
		short d = (short)(a+(short)2*b);
		for (;;) {
	
		    putpixel(x1, y1,  rgb );
		    y1++;
		    if (y1 > y2) break;
		    if (d <= 0) { d+=(short)2*(a+b);  x1++; }
		    else { d += (short)2*b; }

		}
	    }
	    else if ( a > (short)0 /*m > 0.0f*/ /*&& m <= 1.0f*/ ) {
		short d = (short)((short)2*a+b);
		for (;;) {
	
		    putpixel(x1, y1,  rgb );
		    x1++;
		    if (x1 > x2) break;
		    if (d <= 0) { d += 2*a; }
		    else { y1++;  d += 2*(a+b); }

		}
		
	    }
	    else if ( a >  b/*m < 0.0f &&*/ /*m > -1.0f*/ ) {
		short d = (short) ((short)2 * a - b);
		for (;;) {

		    putpixel(x1, y1,  rgb );
		    x1++;
		    if (x1 > x2) break;
		    if (d <= 0) { y1--;  d += (short)2*(a-b); }
		    else { d += 2*a; }

		}
	    }
	    
	    else /*if (m < -1.0f)*/ {
		short d = (short)( a - (short)2 * b);
		for (;;) {
		    putpixel(x1, y1,  rgb );
		    y1--;
		    if (y2 >  y1) {  /*System.out.println(y1);*/  break; }
		    if ( d <= 0) { d += (short)-2 * b; }
		    else { x1++; d += (short)2 * (a-b); }
		    
		}
	    }

	}
    }
    
    
}
    
