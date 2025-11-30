
package j3d;


import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;





class Coordinator
{
    public final byte msize = 100;
    private Matrix4[] modelv;
    private byte modeli;
    
    
    private class LightSource
    {
	float[] v = new float [4];
	boolean eye = true;
    }

    public final byte lsize = 10;
    private LightSource[] lights = new LightSource [lsize];
    private byte li = 0;
    
    
    
    private Graphics g;
    
    
    void addLightSource(float x, float y, float z, boolean eye)
    {
	lights[li] = new LightSource();
	lights[li].eye = eye;
	//for (byte i=0; i < 4; i++ ) lights[li].v[i] = v[i];
	lights[li].v[0] = -x;
	lights[li].v[1] = -y;
	lights[li].v[2] = -z;
	li++;
    }
    

    private ZBuffer zb;
    private BufferedImage bi;
    
    
    

    Coordinator(ZBuffer zb)
    {
	this.zb = zb;
	this.bi = zb.getBi() ;
	this.g = zb.getG() ;
	modeli = 0;
	modelv = new Matrix4 [msize];
	for (byte i=0; i < msize; i++) modelv[i] = null;
	pushMatrix();
	
    }
    


    Matrix4 getCurrentMatrix()
    {
	return modelv [modeli - 1];
    }


    void pushMatrix()
    {
	if (modelv[modeli] == null) modelv[modeli] = new Matrix4();
	modelv[modeli++].toNormal();
    }


    void popMatrix()
    {
	--modeli;
    }
    

    void multWithMatrix(Matrix4 m)
    {
	getCurrentMatrix().setMult(m);
    }


    void translate(float x, float y, float z)
    {
	getCurrentMatrix().translate(x, y, z);
    }


    void rotate(float t, float x, float y, float z)
    {
	getCurrentMatrix().rotate(t, x, y, z);
    }


    void scale(float x, float y, float z)
    {
	getCurrentMatrix().scale(x, y, z);
    }


    void loadIdentity()
    {
	getCurrentMatrix().toNormal();
    }
    


    void setPerspective(Perspective p)
    {
	this.znear = p.znear;
	this.zfar = p.zfar;
	this.width = p.width;
	this.height = p.height;
	this.zoom = p.zoom;
    }
    
    
    
    private byte tri_n = 0;
    private int[] xPoints = new int [4];
    private int[] yPoints = new int [4];
    private float[] vTr = new float [4];
    

    private float znear, zfar;
    private short width, height;
    
    private final float[] tmpv1 = new float [4];
    
    private boolean dont_draw = false;
    
    private float zoom /*= 300.0f*/ ;
    
    private final float[][] for_cross = new float [3][3];
    private float[] normal = for_cross[0];
    private float[] ch1, ch, cht;

    
    private void transform(final float[] v)
    {
	ch1 = vTr;
	ch = tmpv1;
	for (byte i=0; i<4; i++) tmpv1[i] = v[i];
	
	for (byte i=(byte)(modeli-1); i >= 0; i--) {
	    cht = ch; ch = ch1; ch1 = cht;
	    Matrix4.mult(modelv[i], ch1, ch);
	}
    }
    
	
    
    private void addPointQuad(final float[] v)
    {
	
    }


    private boolean computeNormal = true;
    private boolean cullFace = false;
    
    

    private void addPoint(final float[] v)
    {
	transform(v);
	
	if (ch[2] <= znear || ch[2] >= zfar) { 
	    dont_draw = true;
	    return;
	}
	
	if (computeNormal) {
	    if (tri_n == 0) {
		for_cross[tri_n][0] = ch[0];
		for_cross[tri_n][1] = ch[1];
		for_cross[tri_n][2] = ch[2];
	    }
	    else {
		for_cross[tri_n][0] = ch[0] - for_cross[0][0];
		for_cross[tri_n][1] = ch[1] - for_cross[0][1];
		for_cross[tri_n][2] = ch[2] - for_cross[0][2];
	    }
	}
	
	
	float tmp = znear / ch[2];
	xPoints[tri_n] = Math.round(tmp * ch[0] * zoom) + width / 2;
	yPoints[tri_n] = Math.round(tmp * ch[1] * zoom) + height / 2;	
    }

    

    private void drawLine(float x0, float y0, float z0,
			  float x1, float y1, float z1)
    {
	float tmp0 = znear / z0;
	float tmp1 = znear / z1;
	g.drawLine(Math.round(tmp0*x0*zoom)+ width/2,
		   Math.round(tmp0*y0*zoom)+ height/2,
		   Math.round(tmp1*x1*zoom)+ width/2,
		   Math.round(tmp1*y1*zoom)+ height/2);
    }
    

    
    
    void setNormal(float[] v)
    {
	normal = v;
    }
    

    void setNormal(float x, float y, float z) 
    {
    }
    
    
    void setNormal(byte x, byte y, byte z)
    {
    }
    
    
    float[] getNormal()
    {
	return normal;
    }

    void computeNormal(boolean b)
    {
	computeNormal = b;
    }

    

    void cullFace(boolean b)
    {
	cullFace = b;
    } 

    //private Color color = new Color();




    public static float map(float A, float B, float a, float b, float x)
    {
	return A + ((B-A) * (x - a)) / (b-a);
    }





    private float color[] = new float [] { 1.0f, 1.0f, 0.0f };
    
    void setColor(final float[] color) 
    {
	this.color[0] = color[0];
	this.color[1] = color[1];
	this.color[2] = color[2];
    }

    
    void setColor(float r, float g, float b)
    {
	color[0] = r;
	color[1] = g;
	color[2] = b;
    }

    
    void setTrianglePoint(final float[] v)
    {
	addPoint(v);
	tri_n++;
	if (tri_n == 3) {
	    
	    if (computeNormal) {
		Point3D.cross(for_cross[2], for_cross[1], for_cross[0]);
		Point3D.normalize(for_cross[0]);
		normal = for_cross[0];
	    }		

	    
	    if (!dont_draw && 
		(!cullFace || 
		 normal[0]*ch[0] + normal[1]*ch[1] + normal[2]*ch[2]<=0.0f)) {
		
		float cc = 
		    normal[0]*lights[0].v[0] +
		    normal[1]*lights[0].v[1] +
		    normal[2]*lights[0].v[2];
		
		cc = map( 0.3f, 1.0f, -1.0f, 1.0f, cc);
		
		
		//g.setColor(new Color(cc, cc, 0.0f));
		g.setColor(new Color(cc*color[0], cc*color[1], cc*color[2]));
		
		//g.fillPolygon(xPoints, yPoints, 3);
		short[] xps = new short [3];
		short[] yps = new short [3];
		
		xps[0] = (short)xPoints[0];
		xps[1] = (short)xPoints[1];
		xps[2] = (short)xPoints[2];

		yps[0] = (short)yPoints[0];
		yps[1] = (short)yPoints[1];
		yps[2] = (short)yPoints[2];
		
		//zb.drawTriangle(xps, yps );

		zb.addTriangle(xps, yps, ch, normal);
		
	    }
	    
	    tri_n = 0;
	    dont_draw = false;
	    
	    
	    
	    /* foo */
	    /*g.setColor(Color.red);
	    drawLine(ch[0]+normal[0]/5, ch[1]+normal[1]/5, ch[2]+normal[2]/5,
		     ch[0], ch[1], ch[2]);
		     g.setColor(Color.yellow);*/
	    /* foo end */

	}
    }


    
    private float[] tmpv = new float [4];
    


    void setTrianglePoint(float x, float y, float z)
    {
	tmpv[0] = x; tmpv[1]=y; tmpv[2]=z; tmpv[3]=1.0f;
	setTrianglePoint(tmpv);
    }
    



    void setQuadranglePoint(final float[] v)
    {
	
	addPointQuad(v);
	tri_n++;
	if (tri_n == 4) {
	    if (!dont_draw) {
		g.drawPolygon(xPoints, yPoints, 3);
		xPoints[1] = xPoints[3];
		yPoints[1] = yPoints[3];
		g.drawPolygon(xPoints, yPoints, 3);
	    }
	    tri_n = 0;
	    dont_draw = false;
	}
    }
	

    
    void setQuadranglePoint(float x, float y, float z)
    {
	tmpv[0]=x; tmpv[1]=y; tmpv[2]=z; tmpv[3]=1.0f;
	setQuadranglePoint(tmpv);
    }


    void setLinePoint(final float[] v)
    {
	addPoint(v);
	tri_n++;
	if (tri_n == 2) {
	    g.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
	    tri_n = 0;
	}
    }

    

    void setLinePoint(float x, float y, float z)
    {
	tmpv[0]=x; tmpv[1]=y; tmpv[2]=z; tmpv[3]=1.0f;
	setLinePoint(tmpv);
    }
    

    /*
    void setPolyLines(final float[] v)
    {
	addPoint(v);
	tri_n %= 2;
	if (tri_n == 
	}*/
	


}
