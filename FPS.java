package j3d;

class FPS 
{
    private long current;
    private short counter;
    private short frames=10;
    private float fps=0;
    
    void setFrames(short frames) {
	this.frames = frames;
    }
    
    void start() {
	counter=0;
	current = System.currentTimeMillis();
    }
    
    void tick() { 
	counter++; 
	if (counter == frames) {
	    fps =(float)counter /(float)(System.currentTimeMillis()-current);
	    fps *= 1000;
	    counter= 0;
	    current = System.currentTimeMillis();
	}
    }
    
    float getFPS() {
	return fps;
    }
}
