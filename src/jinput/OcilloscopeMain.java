package jinput;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JPanel;

import main.Window;
import msg.CheckSum;
import msg.MAVLink;
import msg.MAVLinkTypes.MAV_MESSAGE;
import plotter.GradientPane;
import plotter.d3.Vector3D;
import tools.VectorConversions;
import bytetools.ByteTools;


public class OcilloscopeMain{		
	
	private static Window window; 			// The application window
	public static GradientPane pane1 = null; 		// Pane containing filled rectangles
	public static GradientPane pane2 = null; 		// Pane containing filled rectangles
	
	static double Vvalues[]=new double[101];
    
    static int counter=0;
    
    public double xCenter = 0;
    public double yCenter = 0;
    

	Vector3D xAxis = new Vector3D(15, 0, 0);
	Vector3D yAxis = new Vector3D(0, 15, 0);
	Vector3D zAxis = new Vector3D(0, 0, 25);
    
	private int mouseX = 0;
	private int mouseY = 0;
    private boolean cntlrPressed = false;
    private boolean rotating = false;
    
    public double theta = 45;
    public double phi1 	= 45;
    public double phi2 	= 45;
    
    public boolean loadIntoArray(byte[] src, byte[] dst, int offset){
    	if(offset < 0) return false;
    	if(offset+src.length > dst.length) return false;
    	for(int i = 0; i < src.length; i++)
    		dst[offset+i] = src[i];
    	return true;
    }
    
    public OcilloscopeMain(){

    	this.creatGUI();
    	
    	pane2.setMaxX(2);
    	pane2.setMinX(0);
    	pane2.setdeltaX(0.5);
    	pane2.setMaxY(1.2);
    	pane2.setMinY(-1.2);
    	pane2.setdeltaY(.1);
    	
    	pane1.setMaxX(2);
    	pane1.setMinX(0);
    	pane1.setdeltaX(0.5);
    	pane1.setMaxY(1.2);
    	pane1.setMinY(-1.2);
    	pane1.setdeltaY(.1);
    	
    }
	
	// Method to create the application GUI
	private void creatGUI() {
		
		window = new Window("Ocilloscope"); 			// Create the app window
		Toolkit theKit = window.getToolkit(); 		// Get the window toolkit
		Dimension wndSize = theKit.getScreenSize(); // Get screen size
		
		int width = wndSize.width;
		int height = wndSize.height;
		
		int sizeFactor = 90;
		
		if(width>height){
			width = height;
			height = height-50;
		}
		if(height>width){
			height = width;
		}
		
		Dimension prefdim = new Dimension(width,height);//Preffered dimensions
		
		// Set the position & size of window
		window.setBounds(0, 0, 	// Position
				width*sizeFactor/100, height*sizeFactor/100); 				// Size
		window.setPreferredSize(prefdim);

		window.setVisible(true);		// Shows window
		window.pack();					// Packs window
		
		//Sends the size to the class
		Dimension dim = window.getContentPane().getSize();
		pane1 = new GradientPane(dim.width,dim.height/2-2); // Pane containing filled rectangles
		pane2 = new GradientPane(dim.width,dim.height/2-2); // Pane containing filled rectangles
		
		//Adds the background picture for the first time
		JPanel pane = new JPanel();
		pane.setPreferredSize(dim);
		pane.add(pane1);
		pane.add(pane2);
		window.getContentPane().add(pane); 
		
		window.setResizable(false);		// Prevents resizing
				
//		System.out.println("Width:  "+pane.dim.width);
//		System.out.println("Heigth: "+pane.dim.height);

	}
	
}