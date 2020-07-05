package joystick;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class DS4 extends Thread implements Gamepad{

	private PollEventListener listener = null;
	private Controller controller = null;
	private int sleepFor;
	
	private Component lX;
	private Component lY;
	private Component rY;
	private Component rX;
	private Component arrows;
	private Component l2;
	private Component r2;
	private Component l1;
	private Component r1;
	private int invertL2 = 1;
	private int invertR2 = 1;
	
	private Component trigger;
	
	private DS4(Controller c, double pollRate, boolean start){
		this.controller = c;
		this.sleepFor = (int)(1.0/pollRate * 1000);
		
		String OS = System.getProperty("os.name").toLowerCase();
		if(OS.indexOf("win") >= 0) {
			lX 	= controller.getComponent(Component.Identifier.Axis.X);
			lY 	= controller.getComponent(Component.Identifier.Axis.Y);
			rX 	= controller.getComponent(Component.Identifier.Axis.Z);
			rY 	= controller.getComponent(Component.Identifier.Axis.RZ);
			l2  = controller.getComponent(Component.Identifier.Axis.RX);
			r2  = controller.getComponent(Component.Identifier.Axis.RY);
			l1  = controller.getComponent(Component.Identifier.Button._4);
			r1  = controller.getComponent(Component.Identifier.Button._5);
			arrows 	= controller.getComponent(Component.Identifier.Axis.POV);
		}else if(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) {
			lX 	= controller.getComponent(Component.Identifier.Axis.X);
			lY 	= controller.getComponent(Component.Identifier.Axis.Y);
			rX 	= controller.getComponent(Component.Identifier.Axis.RX);
			rY 	= controller.getComponent(Component.Identifier.Axis.RY);
			l2  = controller.getComponent(Component.Identifier.Axis.Z);
			r2  = controller.getComponent(Component.Identifier.Axis.RZ);
			l1  = controller.getComponent(Component.Identifier.Button.LEFT_THUMB);
			r1  = controller.getComponent(Component.Identifier.Button.RIGHT_THUMB);
			arrows 	= controller.getComponent(Component.Identifier.Axis.POV);
		}
		if(start)
			this.start();
	}

	public static DS4 getJoystick(double pollRate, boolean startPoller){      
        for(Controller c: ControllerEnvironment.getDefaultEnvironment().getControllers()) {
            if((c.getType() == Controller.Type.STICK || c.getType() == Controller.Type.GAMEPAD) && c.getName().equals("Wireless Controller"))
            {
            	DS4 joystick = new DS4(c, pollRate, startPoller);
                return joystick;
            }
        }
        return null;
	}
		
	public double getLX(){
		return lX.getPollData();
	}
	
	public double getLY(){
		return lY.getPollData();
	}
	
	public double getRY(){
		return rY.getPollData();
	}
	
	public double getRX(){
		return rX.getPollData();
	}
	
	public double getR2(){
		return r2.getPollData()*invertR2;
	}

	public double getL2(){
		return l2.getPollData()*invertL2;
	}
	
	public double getR1(){
		if(r1!=null)
			return r1.getPollData();
		return 0;
	}

	public double getL1(){
		if(l1!=null)
			return l1.getPollData();
		return 0;
	}
	
	public double getArrows(){
		return arrows.getPollData();
	}
	
	public boolean isTriggerPressed(){
		if(trigger==null) return false;
		return trigger.getPollData() != 0;
	}
	
	public void setPollEventListener(PollEventListener e){
		this.listener = e;
	}
	
	
	public void run(){
		while (true){
			controller.poll();
			if(this.listener!=null)
				listener.onPoll(this);
			try {Thread.sleep(sleepFor);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}
