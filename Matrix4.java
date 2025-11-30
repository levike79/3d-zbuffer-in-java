
package j3d;




public class Matrix4
{
    private float[][] m;
    

    public Matrix4()
    {
	m = new float [4][4];
    }

    

    public Matrix4(Matrix4 m4)
    {
	this();
	for (byte i=0; i < 4; i++)
	    for (byte j=0; j < 4; j++) 
		m[i][j] = m4.m[i][j];
    }
    


    public void toNormal()
    {
	m[0][0]=1; m[0][1]=0; m[0][2]=0; m[0][3]=0;
	m[1][0]=0; m[1][1]=1; m[1][2]=0; m[1][3]=0;
	m[2][0]=0; m[2][1]=0; m[2][2]=1; m[2][3]=0;
	m[3][0]=0; m[3][1]=0; m[3][2]=0; m[3][3]=1;
    }




    public String toString()
    {
	StringBuffer sb = new StringBuffer(2048);
	sb.append('{');
	for (byte i=0; i < 4; i++ ) {
	    sb.append('{');
	    for (byte j=0; j < 4; j++) {
		sb.append(m[i][j] + "  ");
	    }
	    sb.append("}  ");
	}
	sb.append('}');
	
	return sb + "";
    }
	


    
    
    public static void mult(final Matrix4 a, final Matrix4 b, Matrix4 c)
    {
	for (byte i=0; i < 4; i++)
	    for (byte j=0; j < 4; j++) {
		c.m[i][j] = 0;
		for (byte k=0; k < 4; k++) c.m[i][j] += a.m[i][k] * b.m[k][j];
	    }
    }
    


    
    public static void mult(final Matrix4 a, final float[] v, float[] out)
    {
	for (byte i=0; i <4; i++) out[i] = 0;
	
	for (byte i=0; i < 4; i++) 
	    for (byte j=0; j< 4; j++) out[i] += a.m[i][j] * v[j];
    }
    

    


    private static float[] tmpv = new float [4];
    private static Matrix4 tmpM = new Matrix4();



    public void setMult(Matrix4 b)
    {
	for (byte i=0; i < 4; i++) {
	    for (byte k=0; k < 4; k++) tmpv[k] = m[i][k];
	    for (byte j=0; j < 4; j++) {
		m[i][j] = 0;
		for (byte k=0; k < 4; k++) m[i][j] += tmpv[k] * b.m[k][j];
	    }
	}
    }

    
    

    public void toRotate(float t, float x, float y, float z)
    {
	{
	    float tmp = (float)Math.sqrt(x*x + y*y + z*z);
	    x /= tmp; 
	    y /= tmp;
	    z /= tmp;
	}
	
	t = (((float)Math.PI) * t) / 180.0f;
	float sint = (float)Math.sin(t);
	float cost = (float)Math.cos(t);
	float xx = x*x, xy = x*y, xz = x*z, yz = y*z, yy=y*y, zz=z*z;
	
	
	m[0][0] = xx + cost *(1.0f - xx);
	m[0][1] = xy + cost *(-xy) + sint * -z;
	m[0][2] = xz + cost *(-xz) + sint *  y;
	m[0][3] = 0;
	
	m[1][0] = xy + cost *(-xy) + sint *  z;
	m[1][1] = yy + cost *(1.0f - yy) ;
	m[1][2] = yz + cost *(-yz) + sint * -x;
	m[1][3] = 0;

	m[2][0] = xz + cost *(-xz) + sint * -y;
	m[2][1] = yz + cost *(-yz) + sint *  x;
	m[2][2] = zz + cost *(1.0f - zz);
	m[2][3] = 0;
	
	m[3][0] = 0;
	m[3][1] = 0;
	m[3][2] = 0;
	m[3][3] = 1;
	
	    
    }
    


    public void rotate(float t, float x, float y, float z)
    {
	tmpM.toRotate(t, x, y, z);
	setMult(tmpM);
    }




    public void toTranslate(float x, float y, float z)
    {
	toNormal();
	m[0][3] = x;
	m[1][3] = y;
	m[2][3] = z;
    }


    
    public void translate(float x, float y, float z)
    {
	tmpM . toTranslate(x, y, z);
	setMult(tmpM);
    }




    public void toScale(float x, float y, float z)
    {
	toNormal();
	m[0][0] = x;
	m[1][1] = y;
	m[2][2] = z;
    }




    public void scale(float x, float y, float z)
    {
	tmpM.toScale(x, y, z);
	setMult(tmpM);
    }



    
    
    
}
