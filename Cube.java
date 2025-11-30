
package j3d;
import java.util.Iterator;


class Cube implements Iterator
{
    private final float[][] v = new float [8][4];
    private final byte[] vertex = new byte [36]; 
    private byte i;
    
    Cube(float a) 
    { 
	float ap2 = a / 2.0f; 
	
	v[0][0] = -ap2; v[0][1]= -ap2; v[0][2] = -ap2; v[0][3]=1;
	v[1][0] = ap2; v[1][1]= -ap2; v[1][2] = -ap2; v[1][3]=1;
	v[2][0] = ap2; v[2][1]= ap2; v[2][2] = -ap2; v[2][3]=1;
	v[3][0] = -ap2; v[3][1]= ap2; v[3][2] = -ap2; v[3][3]=1;
	
	v[4][0] = -ap2; v[4][1]= -ap2; v[4][2] = ap2; v[4][3]=1;
	v[5][0] = ap2; v[5][1]= -ap2; v[5][2] = ap2; v[5][3]=1;
	v[6][0] = ap2; v[6][1]= ap2; v[6][2] = ap2; v[6][3]=1;
	v[7][0] = -ap2; v[7][1]= ap2; v[7][2] = ap2; v[7][3]=1;
	
	//0
	vertex[0] = 0; vertex[1] = 1; vertex[2]=2;
	//1
	vertex[3] = 2; vertex[4] = 3; vertex[5]=0;
	//2
	vertex[6] = 1; vertex[7] = 5; vertex[8]=6;
	//3
	vertex[9] = 6; vertex[10] = 2; vertex[11]=1;
	//4
	vertex[12] = 5; vertex[13] = 4; vertex[14]=7;
	//5
	vertex[15] = 7; vertex[16] = 6; vertex[17]=5;
	//6
	vertex[18] = 4; vertex[19] = 0; vertex[20]=3;
	//7
	vertex[21] = 3; vertex[22] = 7; vertex[23]= 4;
	//8
	vertex[24] = 3; vertex[25] = 2; vertex[26]=6;
	//9
	vertex[27] = 6; vertex[28] = 7; vertex[29]=3;
	//10
	vertex[30] = 1; vertex[31] = 0; vertex[32]=4;
	//11
	vertex[33] = 4; vertex[34] = 5; vertex[35]=1;

	

    }
    

    public void init() { 
	i=0;
    }

    public boolean hasNext() { return  i < 36; }
    


    public Object next() 
    {
	return v[ vertex [i++] ];
    }

    public void remove() { }


    
}
    
