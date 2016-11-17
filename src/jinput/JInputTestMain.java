package jinput;

import java.awt.Color;

import bytetools.ByteTools;
import joystick.Gamepad;
import joystick.LogitechExtreme3DPro;
import joystick.PollEventListener;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Rumbler;

@SuppressWarnings({"unused"})
public class JInputTestMain {
	
	static double yDeadBand = 0.01;
		
	static long t0 	= System.nanoTime();
	static double t 	=0;
	static double lt 	= 0;
	static double dt;
	
	static double ax = 0;
	static double ay = 0;
	static double az = 0;

	static double rTrack = 0;
	static double lTrack = 0;
	
	static double lax = 0;
	static double lay = 0;
	static double laz = 0;
	
	static double rTrackLast = 0;
	static double lTrackLast = 0;
	
	
	public static void main(String[] args){

//		final tank.wifi.WiFiInterface tank = new tank.wifi.WiFiInterface();
//		int dev = tank.getDevices();
//		if(dev==0) System.exit(0);
//		System.out.println(dev);
//		tank.connect(0);
		
		OcilloscopeMain osc = new OcilloscopeMain();
		    	    			
		LogitechExtreme3DPro joystick = LogitechExtreme3DPro.getJoystick(10, true);
		joystick.setPollEventListener(new PollEventListener() {
			@Override
			public void onPoll(Gamepad gamepad) {
				LogitechExtreme3DPro joystick = (LogitechExtreme3DPro) gamepad;			
				ax = joystick.getX();
				ay = joystick.getY();
				az = joystick.getRotation();
				
				double[] track = getTracks(ax, ay, az);
				lTrack = track[0];
				rTrack = track[1];
				
//				tank.send((int)(255*rTrack), (int)(255*lTrack));

				t = (System.nanoTime() - t0) / 1000000000d;
				dt = (t - lt);

				OcilloscopeMain.pane1.drawLine(lt, rTrackLast, t, rTrack, Color.GREEN);
				OcilloscopeMain.pane1.drawLine(lt, lTrackLast, t, lTrack, Color.YELLOW);
				
				OcilloscopeMain.pane2.drawLine(lt, lax, t, ax, Color.GREEN);
				OcilloscopeMain.pane2.drawLine(lt, lay, t, ay, Color.BLUE);
				OcilloscopeMain.pane2.drawLine(lt, laz, t, az, Color.yellow);

				lt = t;
				lax = ax;
				lay = ay;
				laz = az;
				lTrackLast = lTrack; 
				rTrackLast = rTrack;
				OcilloscopeMain.pane1.autoscale(true, false);
				OcilloscopeMain.pane1.repaint();
				OcilloscopeMain.pane2.autoscale(true, false);
				OcilloscopeMain.pane2.repaint();			
			}
		});
				
//		Controller controller = null;
//		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
//        
//        for(int i=0; i < controllers.length && controller == null; i++) {
//        	System.out.println(controllers[i].getType());      
//            if(
//               controllers[i].getType() == Controller.Type.STICK ||
//               controllers[i].getType() == Controller.Type.GAMEPAD
//              )
//            {
//                controller = controllers[i];
//                break;
//            }
//        }
//                
//        if(controller != null){
//        	System.out.println("Got Controller: "+controller.getName());
//        	controller.poll();
//        	for(Component c: controller.getComponents()){
//        		System.out.println("\t"+c.getName());
////        		System.out.println("\t\t"+c.getPollData());
////        		System.out.println("\t\tAng: "+c.isAnalog());
////        		System.out.println("\t\tRel: "+c.isRelative());
//        	}
//            Rumbler[] rumblers = controller.getRumblers();
//            for(float i = 0.1f; i<=1.0f; i+=0.1){
//	            rumblers[0].rumble(i);
//	            try {Thread.sleep(1000);} catch (InterruptedException e) {}
//            }
//        }
		
//		JInputJoystick joystick = new JInputJoystick(Controller.Type.STICK, Controller.Type.GAMEPAD);
//		// Check if the controller was found.
//		if( !joystick.isControllerConnected() ){
//		   System.out.println("No controller found!");
//		   // Do some stuff.
//		}
//		
//		while (true){
//			// Get current state of joystick! And check, if joystick is disconnected.
//			if( !joystick.pollController() ) {
//			   System.out.println("Controller disconnected!");
//			   break;
//			}else{
//				// Left controller joystick
//				int x = joystick.getXAxisPercentage();
//				int y = joystick.getYAxisPercentage();
//				System.out.println(x);
//				System.out.println(y);
//			}
//			try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
//		}
	}

	public static double[] getTracks(double x, double y, double z){
		if(y>-yDeadBand && y<yDeadBand)
			return new double[]{z, -z};		
		double left 	= (-y);
		double right 	= (-y);
		if(x<0)
			left 	*= (1+x);
		else
			right 	*= (1-x);		
		return new double[]{left, right};
	}
}
