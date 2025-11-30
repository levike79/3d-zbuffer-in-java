
package j3d;


//import java.applet.*;
import java.awt.*;
import java.awt.geom.*;
//import java.awt.image.*;
//import java.awt.event.*;




class Triangle3D implements Renderable3D
{
    float[][] v;
    private float[] nv;
    private Dimension dim = null;
    
    
    public Triangle3D(Dimension dim)
    {
	this.dim = dim;
	v = new float [3][3];
	//n = new float [3];
    }
	


    public Triangle3D(Dimension dim, 
		      float x0, float y0, float z0,
		      float x1, float y1, float z1,
		      float x2, float y2, float z2)
    {
	this(dim);
	v[0][0] = x0;      v[0][1] = y0;   v[0][2] = z0;
	v[1][0] = x1;      v[1][1] = y1;   v[1][2] = z1;
	v[2][0] = x2;      v[2][1] = y2;   v[2][2] = z2;


	    
	    
    }
    


    public void setNormal()
    {
	Point3D A = new Point3D(v[0]);
	Point3D B = new Point3D(v[1]);
	Point3D C = new Point3D(v[2]);
	Point3D n = C.sub(A).cross( B.sub(A) );
	n.normalize();
	nv = n.v;
    }
	

    public void setNormal(float[] nv)
    {
	this.nv = nv;
    }


    public void setNormal(int x, int y, int z)
    {
	nv = new float [3];
	nv[0] = x; nv[1] = y; nv[2] = z;
	Point3D.normalize(nv);
    }


    public void setNormal(float x, float y, float z)
    {
	nv = new float [3];
	nv[0]=x; nv[1]=y; nv[2]=z;
	Point3D.normalize(nv);
    }
	

	
    public void setLocation(float x0, float y0, float z0,
			    float x1, float y1, float z1,
			    float x2, float y2, float z2)
    {
	v[0][0] = x0;      v[0][1] = y0;   v[0][2] = z0;
	v[1][0] = x1;      v[1][1] = y1;   v[1][2] = z1;
	v[2][0] = x2;      v[2][1] = y2;   v[2][2] = z2;
    }

	
    private final int[] xPoints = new int [3];
    private final int[] yPoints = new int [3];
	
	
    private void makePointsOrt()
    {
	int w = dim.width / 2;
	int h = dim.height / 2;
	xPoints[0] = Math.round(v[0][0]) + w;
	xPoints[1] = Math.round(v[1][0]) + w;
	xPoints[2] = Math.round(v[2][0]) + w;
	    
	yPoints[0] = Math.round(v[0][1]) +h;
	yPoints[1] = Math.round(v[1][1]) + h;
	yPoints[2] = Math.round(v[2][1]) + h;
    }
	
	
    private void makePoints(float znear)
    {
	float tmp;
	int w = dim.width / 2;
	int h = dim.height / 2;
	    
	tmp = znear / v[0][2];
	xPoints[0] = Math.round(tmp * v[0][0]) + w;
	yPoints[0] = Math.round(tmp * v[0][1]) + h;
	    
	tmp = znear / v[1][2];
	xPoints[1] = Math.round(tmp * v[1][0]) +w;
	yPoints[1] = Math.round(tmp * v[1][1]) + h;
	    
	tmp = znear / v[2][2];
	xPoints[2] = Math.round(tmp * v[2][0]) + w;
	yPoints[2] = Math.round(tmp * v[2][1]) + h;
    }
	


    public void renderOrt(Graphics g)
    {
	makePointsOrt();
	g.drawPolygon(xPoints, yPoints, 3);
    }


    public void renderPersp(Graphics g, float znear)
    {
	makePoints(znear);
	g.drawPolygon(xPoints, yPoints, 3);
    }
	
    public void renderFillPersp(Graphics g, float znear)
    {
	makePoints(znear);
	g.fillPolygon(xPoints, yPoints, 3);
    }

}
	    

