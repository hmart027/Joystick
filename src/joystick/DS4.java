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
	
	private Component trigger;
	
	private DS4(Controller c, double pollRate, boolean start){
		this.controller = c;
		this.sleepFor = (int)(1.0/pollRate * 1000);
		lX 	= controller.getComponent(Component.Identifier.Axis.X);
		lY 	= controller.getComponent(Component.Identifier.Axis.Y);
		rX 	= controller.getComponent(Component.Identifier.Axis.RX);
		rY 	= controller.getComponent(Component.Identifier.Axis.RY);
		arrows 	= controller.getComponent(Component.Identifier.Axis.POV);
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
