package j3d;



import java.applet.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.util.Iterator;




class Rajz1 extends Canvas
{
    /*
      Rajz1()
      {
      init_();
      }*/
    


    
    private BufferedImage bi;
    private Graphics2D big2D;
    private Dimension dim;
    private int w, h;
    private Rectangle area, r;

    //private final Color bc = new Color( 0.8f, 0.8f, 0.8f );

    private final Color bc = new Color( 0.83f, 0.83f, 0.83f );



    
    private void init_() 
    {
	setBackground( bc );
	dim = getSize();

	System.out.println("\n\ndim= " + dim);
	System.out.println("getBounds= " + getBounds());
	
	
	w = dim.width; h = dim.height;

	area = new Rectangle(dim);
	bi = (BufferedImage) createImage(w, h);
	big2D = bi.createGraphics();
	big2D.setStroke( new BasicStroke(0.7f) );
	
	setBackground( bc );
    }
    


    
    
    
    public void paint(Graphics g)
    {
	init_();
	

	
	Perspective pp = new Perspective();
	pp.znear = 1.0f;
	pp.zfar = 6.0f;
	pp.width = (short)dim.width;
	pp.height = (short)dim.height;
	pp.zoom = 300.0f;
	
	
	
	
	ZBuffer zb = new ZBuffer(big2D, bi, pp);
	
	
	Coordinator coo1 = new Coordinator(zb);
	

	coo1.setPerspective(pp);
	
	coo1.addLightSource(-1.0f, 0.0f, 0.0f, true);

	coo1.translate(0.0f, 0.0f, 2.1f);
	
	
	coo1.computeNormal(true);
	coo1.cullFace( true );
	
	//float factor = 0.05f ;
	float zeps = 1;

	float rx = 1, ry = 1, rz = 1 ;
	float t = 0;



	



	FPS fps = new FPS();
	
	
	Cube cube = new Cube(1.2f);
	

	/* main render loop */

	
	fps.setFrames((short)20);
	fps.start();
	    
	
	float tr=0;

	for (short i = 0; i < 1000; i++) {
	    big2D.setColor(bc);
	    big2D.clearRect(0, 0, area.width, area.height);
		

	    t += 0.5f;
	    

		
	    coo1.pushMatrix();
		
	    coo1.translate(2.7f - tr, 0.0f, 0.0f);
	    tr += 0.01f;
	    coo1.rotate(-2 * t, 1.0f, 0.8f, 0.3f);	    	    


	    coo1.setColor(0.8f, 1.0f, 0.8f );
	    
	    for ( cube.init() ; cube.hasNext() ;   ) {
		    
		    
		coo1.setTrianglePoint( (float[]) cube.next() );
		    
	    }
		
	    coo1.popMatrix();



	    
	    coo1.pushMatrix();
	    
	    coo1.rotate(t, 1.0f, 0.8f, 0.3f);	    	    
	    
	    coo1.setColor(1.0f, 1.0f, 0.0f );
	    
	    big2D.setColor(Color.black);
		
	    big2D.drawString(i + "", 500, 20);
	    big2D.drawString(fps.getFPS() + " fps", 500, 40);
	    
		
		
	    /*for (short jj=10; jj < 400; jj += 1) {
	      big2D.setColor(Color.red);
	      zb.drawLine((short)10, (short)200, 
	      (short)110, (short)jj, big2D, bi);
	      //big2D.setColor(Color.red);
	      //big2D.drawLine(10, 200, 110, jj);
	      }*/
		
		
		

		

		
	    /*
	      big2D.setColor(Color.yellow);
	      big2D.drawLine((short)10, (short)200, (short)110, (short)20);
	      big2D.drawLine((short)110, (short)20, (short)110, (short)400);
	      big2D.drawLine((short)110, (short)400, (short)10, (short)200);
	    */

		
		
	    //short[] xps = new short [] {100, 80, 310};
	    //short[] yps = new short [] {10, 165, 166 };
		
	    /*
	      big2D.setColor(Color.red);
	      zb.drawTriangle(xps, yps );		
		
		
	      big2D.setColor(Color.black);
	      zb.drawLine(xps[0], yps[0], xps[1], yps[1]);
	      zb.drawLine(xps[1], yps[1], xps[2], yps[2]);
	      zb.drawLine(xps[2], yps[2], xps[0], yps[0]);
	    */

		

		

		
	    coo1.scale(0.99f, 0.39f, 0.99f);
	    

		
	    final int eps = 15;
	    float 
		x0, y0, z0, 
		x1, y1, z1, 
		x2, y2, z2,
		x3, y3, z3;
		
		
		
	    zeps += 0.01;
		
	    
	    for (int u=0; u < 360 ; u += eps ) {
		for (int v=-90; v < 90; v+= eps) {
		    float ur = (float)u * (float)Math.PI / 180.0f;
		    float vr = (float)v * (float)Math.PI / 180.0f;
		
		    float sinur=(float)Math.sin(ur);
		    float sinvr=(float)Math.sin(vr);
		    float cosur=(float)Math.cos(ur);
		    float cosvr=(float)Math.cos(vr);
			
		    x0 = rx * sinur * cosvr; 
		    y0 = ry * cosur * cosvr;
		    z0 = rz * sinvr;
			
		    u += eps;
		    ur = (float)u * (float)Math.PI / 180;
		    vr = (float)v * (float)Math.PI / 180;
		    
		    x1 = rx * (float)Math.sin(ur) * (float)Math.cos(vr); 
		    y1 = ry * (float)Math.cos(ur) * (float)Math.cos(vr);
		    z1 = rz * (float)Math.sin(vr);
		    
		    v += eps;
		    ur = (float)u * (float)Math.PI / 180;
		    vr = (float)v * (float)Math.PI / 180;
		    x2 = rx * (float)Math.sin(ur) * (float)Math.cos(vr); 
		    y2 = ry * (float)Math.cos(ur) * (float)Math.cos(vr);
		    z2 = rz * (float)Math.sin(vr);
		    
		    u -= eps;
		    ur = (float)u * (float)Math.PI / 180;
		    vr = (float)v * (float)Math.PI / 180;
		    x3 = rx * (float)Math.sin(ur) * (float)Math.cos(vr); 
		    y3 = ry * (float)Math.cos(ur) * (float)Math.cos(vr);
		    z3 = rz * (float)Math.sin(vr);
			
		    v -= eps;


		    
		    
		    if (v > -90) {
			coo1.setTrianglePoint(x0, y0, z0);
			coo1.setTrianglePoint(x1, y1, z1);
			coo1.setTrianglePoint(x2, y2, z2);
		    }
		    
		    //big2D.setColor(Color.yellow);
		    
		    if (v < 90-eps) {
			coo1.setTrianglePoint(x0, y0, z0);
			coo1.setTrianglePoint(x2, y2, z2);
			coo1.setTrianglePoint(x3, y3, z3);
		    }
		}
	    }

	    coo1.popMatrix();

	    zb.clear();
		
	    ((Graphics2D)g).drawImage(bi, 0, 0, this);
		
	    /*try { Thread.sleep(200); } 
	      catch (InterruptedException e) { 
	      System.err.println("levi in sleep: " + e); 
	      System.err.flush();
	      }*/
		
		
	    
	    fps.tick();
	}


	    
	    
	/*}
	  catch (Exception e) {
	    
	  System.err.println("\t*levi in exc:* " + e);
	  System.err.flush();

	  }*/
	//finally { System.exit(0); }
		
    }

    
}

