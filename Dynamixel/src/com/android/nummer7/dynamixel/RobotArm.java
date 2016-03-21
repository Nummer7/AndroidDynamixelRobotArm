package com.android.nummer7.dynamixel;

import android.util.Log;

public class RobotArm {
	public
	
	DynamixelSocket control;
	DynamixelAX12[] dyns;
	double a1=7, a2=11, a3=11;
	double al1=Math.PI/2, al2=0, al3=0;
	double d1=0,d2=0,d3=0;
	double v1=0,v2=0,v3=0;
	
	//DH parameters
	
	//WORKSPACE
	
	//EQUATIONS
	
	RobotArm(DynamixelSocket socket) throws Exception{
		control = socket;
		dyns = new DynamixelAX12[4];
		for(int i = 0; i < dyns.length; i++){
			dyns[i] = new DynamixelAX12((byte)(i+1),control);
			dyns[i].resetTorque();
			dyns[i].setServoMode();
			dyns[i].setSpeed((short) 500);
		}
	}
	
	void goTo(double x, double y, double z){
		
		
		double c3 = (Math.pow(x, x)+Math.pow(y, y)+Math.pow(z, z)-Math.pow(a2, a2)-Math.pow(a3, a3))/(2*a2*a3);
		double s3 = Math.sqrt(1-Math.pow(c3,c3));
		v3 = Math.atan2(s3, c3);
		
		double c2 = (Math.sqrt(Math.pow(x,x)+Math.pow(y,y))*(a2+a3*c3)+z*a3*s3)/(Math.pow(a2,a2)+Math.pow(a3,a3)+2*a2*a3*c3);
		double s2 = (z*(a2+a3*c3)+Math.sqrt(Math.pow(x,x)+Math.pow(y,y)*a3*s3))/(Math.pow(a2,a2)+Math.pow(a3,a3)+2*a3*c3);
		v2 = Math.atan2(s2, c2);
		
		v1 = Math.atan2(y, x);
		
		Log.d("STUFF", "v1: "+v1+" v2: "+v2+ " v3:"+v3);
	}
	
}
