package com.android.nummer7.dynamixel;

public class DynamixelAX12 {
	private 
		byte id;
		DynamixelSocket C;
	
	public
		
	DynamixelAX12(byte idd, DynamixelSocket socket){
		id = idd;
		C = socket;
	}
	
	void setPosition(short position) throws Exception{
		C.writeWord(id, DynamixelConstants.INSTRUCTIONS.REGRAM.GOAL_POSITION_L, position);
	}
	void setAngle(short angle) throws Exception{
		setPosition((short) ((angle*1023)/300));
	}
	short getPosition() throws Exception{
		return C.readWord(id,DynamixelConstants.INSTRUCTIONS.REGRAM.GOAL_POSITION_L);
	}
	short getAngle() throws Exception{
		return (short) (getPosition()*300/1023);
	}
	
	void setTorque() throws Exception{
		C.writeByte(id, DynamixelConstants.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 1);
	}
	void resetTorque() throws Exception{
		C.writeByte(id, DynamixelConstants.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 1);
	}
	
	void setSpeed(short speed) throws Exception{
		C.writeWord(id, DynamixelConstants.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, speed);
	}
	
	void continuousMoveLeft() throws Exception{
		resetTorque();
		setSpeed((short)500);
		setPosition((short) 100);
		//setTorque();
	}
	
	void continuousMoveRight() throws Exception{
		resetTorque();
		setSpeed((short) 1500);
		setPosition((short) 100);
		//setTorque();
	}

	
	void setContinuousMode() throws Exception{
		resetTorque();
		C.writeWord(id, DynamixelConstants.INSTRUCTIONS.REGEEPROM.CW_ANGLE_LIMIT_L, (short) 0);
		C.writeWord(id, DynamixelConstants.INSTRUCTIONS.REGEEPROM.CCW_ANGLE_LIMIT_L, (short) 0);
	}
	
	void setServoMode() throws Exception{
		resetTorque();
		C.writeWord(id, DynamixelConstants.INSTRUCTIONS.REGEEPROM.CW_ANGLE_LIMIT_L, (short) 0);
		C.writeWord(id, DynamixelConstants.INSTRUCTIONS.REGEEPROM.CCW_ANGLE_LIMIT_L, (short) 1023);
	}

	
	
}
