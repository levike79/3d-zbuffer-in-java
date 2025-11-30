
package j3d;


import java.awt.*;
import java.awt.event.*;
import java.applet.*;



public class Main extends Applet
{
    
    private Rajz1 rajz1;
    
    public void init()
    {
	setLayout(new BorderLayout() );
	rajz1 = new Rajz1();
	add("Center", rajz1);
    }


    public void destroy()
    {
	remove(rajz1);
    }

    
    public void start()
    {
    }

    public void stop()
    {
    }

    
    public void processEvent(AWTEvent e)
    {
	if (e.getID() == Event.WINDOW_DESTROY) System.exit(0);
    }

    
    public static void main(String[] args)
    {
	Frame f = new Frame("Rajz1");
	f.setSize(650, 450);
	Main main_ = new Main();
	main_.init();
	main_.start();
	f.add("Center", main_ ) ;

	f.show();
	
    }


    public String getAppletInfo()
    {
	return "A demonstation of software accelerated z-buffer algorithm.";
    }








}


