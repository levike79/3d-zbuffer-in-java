package j3d;



import java.awt.Point;


public class Point3D
{
    public float v[];
    
    public Point3D()
    {
	v = new float [3];
	v[0] = 0.0f; v[1] = 0.0f; v[2] = 0.0f;
    }

    public Point3D(float v[])
    {
	this.v = v;
    }

    public Point3D(int v[])
    {
	this();
	this.v[0] = (float)v[0];
	this.v[1] = (float)v[1];
	this.v[2] = (float)v[2];
    }
    
    public Point3D(float x, float y, float z)
    {
	v = new float [3];
	v[0] = x; v[1] = y; v[2] = z;
    }


    public Point3D(int x, int y, int z)
    {
	this();
	v[0] = (float)x;  v[1] = (float)y;  v[2] = (float)z;
    }

    

    public Point getNewPoint2D(float znear, float zfar)
    {
	if (v[2] > znear && v[2] < zfar) {
	    return new Point( (int)((v[0] * znear) / v[2]),
			      (int)((v[1] * znear) / v[2]) );
	}
	else return new Point(0, 0);
    }
    

    public int getX(float znear, float zfar)
    {
	if (v[2] > znear && v[2] < zfar) return (int)( (v[0]*znear)/v[2] );
	else return 0;
    }

    public int getY(float znear, float zfar)
    {
	if (v[2] > znear && v[2] < zfar) return (int)( (v[1]*znear)/v[2] );
	else return 0;
    }


    

    public Point3D add(Point3D p)
    {
	return new Point3D(v[0]+p.v[0], v[1]+p.v[1], v[2]+p.v[2]);
    }


    public Point3D sub(Point3D p)
    {
	return new Point3D(v[0]-p.v[0], v[1]-p.v[1], v[2]-p.v[2]);
    }

    
    public Point3D cross(Point3D p)
    {
	return new Point3D(v[1]*p.v[2] - v[2]*p.v[1],
			   v[2]*p.v[0] - v[0]*p.v[2],
			   v[0]*p.v[1] - v[1]*p.v[0]);
    }
    

    
    public static void cross(final float[] a, final float[] b, float[] c)
    {
	c[0] = a[1]*b[2] - a[2]*b[1];
	c[1] = a[2]*b[0] - a[0]*b[2];
	c[2] = a[0]*b[1] - a[1]*b[0];
    }

    
    



    public float dot(Point3D p)
    {
	return v[0]*p.v[0] + v[1]*p.v[1] + v[2]*p.v[2];
    }


    public static float dot(final float[] a, final float[] b)
    {
	return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
    }


    public float length()
    {
	return (float)Math.sqrt(dot(this));
    }

    public void normalize()
    {
	float tmp = length();
	v[0] /= tmp;
	v[1] /= tmp;
	v[2] /= tmp;
    }
    
    
    public float[] getNormal()
    {
	float[] n = new float [3];
	float m = (float)Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
	n[0] = v[0]/m;  n[1] = v[1]/m;  n[2]=v[2]/m;
	return n;
    }



    public void setMultWithNumber(float x)
    {
	v[0] *= x; v[1] *= x; v[2]*=x;
    }

    



    public static void normalize(float[] v)
    {
	float tmp = (float)Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
	v[0] /= tmp;
	v[1] /= tmp;
	v[2] /= tmp;
    }
    


}
