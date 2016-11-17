package joystick;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class LogitechExtreme3DPro extends Thread implements Gamepad{

	private PollEventListener listener = null;
	private Controller controller = null;
	private int sleepFor;
	
	private Component xAxis;
	private Component yAxis;
	private Component zAxis;
	private Component slider;
	private Component hat;
	
	private Component trigger;
	
	private LogitechExtreme3DPro(Controller c, double pollRate, boolean start){
		this.controller = c;
		this.sleepFor = (int)(1.0/pollRate * 1000);
		xAxis 	= controller.getComponent(Component.Identifier.Axis.X);
		yAxis 	= controller.getComponent(Component.Identifier.Axis.Y);
		zAxis 	= controller.getComponent(Component.Identifier.Axis.RZ);
		slider 	= controller.getComponent(Component.Identifier.Axis.SLIDER);
		hat 	= controller.getComponent(Component.Identifier.Axis.POV);
		trigger = controller.getComponent(Component.Identifier.Button._0);
		if(start)
			this.start();
	}

	public static LogitechExtreme3DPro getJoystick(double pollRate, boolean startPoller){      
        for(Controller c: ControllerEnvironment.getDefaultEnvironment().getControllers()) {	        	
            if((c.getType() == Controller.Type.STICK || c.getType() == Controller.Type.GAMEPAD) && c.getName().equals("Logitech Extreme 3D"))
            {
        		LogitechExtreme3DPro joystick = new LogitechExtreme3DPro(c, pollRate, startPoller);
                return joystick;
            }
        }
        return null;
	}
		
	public double getX(){
		return xAxis.getPollData();
	}
	
	public double getY(){
		return yAxis.getPollData();
	}
	
	public double getRotation(){
		return zAxis.getPollData();
	}
	
	public double getSlider(){
		return slider.getPollData();
	}
	
	public double getHat(){
		return hat.getPollData();
	}
	
	public boolean isTriggerPressed(){
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
