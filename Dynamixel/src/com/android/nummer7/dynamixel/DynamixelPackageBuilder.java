package com.android.nummer7.dynamixel;

import java.util.Arrays;

import android.util.Log;


/* Package builder for dynamixel AX12 motors  
 * 
 * */
public class DynamixelPackageBuilder{
	byte packageLength;
	
	//calculate checksum from byte array
	byte getChecksum(byte[] bytes, int length){
		int sum = 0;
		for(int i=2; i<length; i++)
			sum += bytes[i];
		return (byte)((~sum) & 0xff);
	}
	
	//get package that writes Position and Speed at the same time
	byte[] WritePositionAndSpeed(byte id, short position, short speed){
		this.packageLength = 11;

		byte[] output = new byte[packageLength];
		output[0] = (byte) 0xff;
		output[1] = (byte) 0xff;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.ID] = id;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.LENGTH] = (byte) (packageLength-4);
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.INSTRUCTION] = DynamixelConstants.PROTOCOL.INST.WRITE;
		
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+0] = DynamixelConstants.INSTRUCTIONS.REGRAM.GOAL_POSITION_L;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+1] = (byte)(position & 0x00ff);
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+2] = (byte)((position & 0xff00) >> 8);
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+3] = (byte)(speed & 0x00ff);
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+4] = (byte)((speed & 0xff00) >> 8);
		
		output[packageLength-1] =  getChecksum(output,packageLength-1);
		return output;
	}
	
	/*
	byte[] WritePositionAndSpeedAction(byte id, short position, short speed){
		this.packageLength = 11;
		//Log.d("STUFF", "writting: id/position/speed -- "+ id+"/"+position+"/"+speed);
		byte[] output = new byte[packageLength];
		output[0] = (byte) 0xff;
		output[1] = (byte) 0xff;
		output[Dynamixel.PROTOCOL.PRT1_PKT.ID] = id;
		output[Dynamixel.PROTOCOL.PRT1_PKT.LENGTH] = (byte) (packageLength-4);
		output[Dynamixel.PROTOCOL.PRT1_PKT.INSTRUCTION] = Dynamixel.PROTOCOL.INST.REG_WRITE;
		
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+0] = Dynamixel.INSTRUCTIONS.REGRAM.GOAL_POSITION_L;
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+1] = (byte)(position & 0x00ff);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+2] = (byte)((position & 0xff00) >> 8);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+3] = (byte)(speed & 0x00ff);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+4] = (byte)((speed & 0xff00) >> 8);
		
		output[packageLength-1] =  getChecksum(output,packageLength-1);
		return output;
	}
	
	byte[] WriteAction(){
		this.packageLength = 6;
		
		byte[] output = new byte[packageLength];
		output[0] = (byte) 0xff;
		output[1] = (byte) 0xff;
		output[Dynamixel.PROTOCOL.PRT1_PKT.ID] = (byte)0xFE;
		output[Dynamixel.PROTOCOL.PRT1_PKT.LENGTH] = (byte) (packageLength-4);
		output[Dynamixel.PROTOCOL.PRT1_PKT.INSTRUCTION] = Dynamixel.PROTOCOL.INST.ACTION;
		
		output[packageLength-1] =  getChecksum(output,packageLength-1);
		return output;
	}
	
	byte[] WritePositionAndSpeedAction_6axes(short[] position, short[] speed){
		this.packageLength = 24;
		
		byte[] output = new byte[packageLength];
		output[0] = (byte) 0xff;
		output[1] = (byte) 0xff;
		output[Dynamixel.PROTOCOL.PRT1_PKT.ID] = (byte)0xFE;
		output[Dynamixel.PROTOCOL.PRT1_PKT.LENGTH] = (byte) (packageLength-4);
		output[Dynamixel.PROTOCOL.PRT1_PKT.INSTRUCTION] = Dynamixel.PROTOCOL.INST.SYNC_WRITE;
		
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+0] = Dynamixel.INSTRUCTIONS.REGRAM.GOAL_POSITION_L;
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+1] = 4;
		
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+2] = (byte) 1;
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+3] = (byte)(position[0] & 0x00ff);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+4] = (byte)((position[0] & 0xff00) >> 8);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+5] = (byte)(speed[0] & 0x00ff);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+6] = (byte)((speed[0] & 0xff00) >> 8);
		
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+7] = (byte) 2;
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+8] = (byte)(position[1] & 0x00ff);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+9] = (byte)((position[1] & 0xff00) >> 8);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+10] = (byte)(speed[1] & 0x00ff);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+11] = (byte)((speed[1] & 0xff00) >> 8);
		
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+12] = (byte) 3;
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+13] = (byte)(position[2] & 0x00ff);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+14] = (byte)((position[2] & 0xff00) >> 8);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+15] = (byte)(speed[2] & 0x00ff);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+16] = (byte)((speed[2] & 0xff00) >> 8);
		
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+17] = (byte) 4;
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+18] = (byte)(position[3] & 0x00ff);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+19] = (byte)((position[3] & 0xff00) >> 8);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+20] = (byte)(speed[3] & 0x00ff);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+21] = (byte)((speed[3] & 0xff00) >> 8);
		
		output[packageLength-1] =  getChecksum(output,packageLength-1);
		return output;
	}
	
	*/

	
	//get package to write one byte
	byte[] WriteByte(byte id, byte address, byte value){
		this.packageLength = 8;
		
		byte[] output = new byte[packageLength];
		output[0] = (byte) 0xff;
		output[1] = (byte) 0xff;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.ID] = id;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.LENGTH] = (byte) (packageLength-4);
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.INSTRUCTION] = DynamixelConstants.PROTOCOL.INST.WRITE;
		
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+0] = address;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+1] = (byte)(value & 0x00ff);
		
		output[packageLength-1] =  getChecksum(output,packageLength-1);
		return output;
	}
	
	//get package to write word (2 bytes)
	byte[] WriteWord(byte id, byte address, short value){
		this.packageLength = 9;
		Log.d("STUFF", "geschrieben wird byteL: "+(byte)(value & 0x00ff)+" byteH: "+(byte)((value & 0xff00) >> 8));
		byte[] output = new byte[packageLength];
		output[0] = (byte) 0xff;
		output[1] = (byte) 0xff;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.ID] = id;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.LENGTH] = (byte) (packageLength-4);
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.INSTRUCTION] = DynamixelConstants.PROTOCOL.INST.WRITE;
		
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+0] = address;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+1] = (byte)(value & 0x00ff);
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+2] = (byte)((value & 0xff00) >> 8);
		
		output[packageLength-1] =  getChecksum(output,packageLength-1);
		return output;
	}
	
	//get package to read one byte
	byte[] ReadByte(byte id, byte address){
		return Read(id, address, (byte)1);
	}
	
	//get package to read <length> bytes
	byte[] Read(byte id, byte address, byte length){
		this.packageLength = 8;
		
		byte[] output = new byte[packageLength];
		output[0] = (byte) 0xff;
		output[1] = (byte) 0xff;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.ID] = id;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.LENGTH] = (byte) (packageLength-4);
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.INSTRUCTION] = DynamixelConstants.PROTOCOL.INST.READ;
		
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+0] = address;
		output[DynamixelConstants.PROTOCOL.PRT1_PKT.PARAMETER0+1] = length;

		output[packageLength-1] =  getChecksum(output,packageLength-1);
		return output;
	}
	
	// check the read bytes
	// package is complete? etc.
	byte[] checkReadBytes(byte[] a, int length) throws Exception{
		int start=0, len=0;
		for (int i=0; i<length; i++)
		{
			//look for start
			if (a[i]==(byte)-1 && a[i+1]==(byte)-1){
					start = i;
					len = (4+a[i+3]); //4+ length of the package
					if (start+len > 127){
						throw new Exception("Read, Packet wurde nicht vollstaendig gelesen");
					}
					
					//copy package to new bytearr
					if (len <= 0){
						throw new Exception("Read, Packet wurde nicht vollstaendig gelesen");
					}
					byte[] b =  new byte[len];
					for(int j=0; j<len; j++)
					{
						b[j] = a[j+start];
						//Log.d("STUFF", "checkReadBytes"+ j+"-> "+b[j]);
					}
					
					//error occured?
					if (b[4] != (byte)0)
						analyseError(b[4]);
					
					//checksum is correct? recalculate
					if (getChecksum(b,len-1) != (byte)b[len-1])
						throw new Exception("Read, checksumme stimmt nicht ueberein.");
					return b;
			}
		}
		throw new Exception("Read, konnte das Packet nicht erkennen.");
	}

	//parse error byte
	private void analyseError(byte error) throws Exception{
		if ( (DynamixelConstants.PROTOCOL.ERR.ANGLE_LIMIT & error) > 0)
			throw new Exception("Package Error: Goal Position is written with the value that is not between CW Angle Limit and CCW Angle Limit.");
		
		if ( (DynamixelConstants.PROTOCOL.ERR.INSTRUCTION & error) > 0)
			throw new Exception("Package Error: Undefined Instruction is transmitted or the Action command is delivered without the reg_write command.");
		if ( (DynamixelConstants.PROTOCOL.ERR.OVERLOAD & error) > 0)
			throw new Exception("Package Error: Current load cannot be controlled with the set maximum torque.");
		if ( (DynamixelConstants.PROTOCOL.ERR.CRC & error) > 0)
			throw new Exception("Package Error: Checksum of the transmitted Instruction Packet is invalid.");
		if ( (DynamixelConstants.PROTOCOL.ERR.DATA_RANGE & error) > 0)
			throw new Exception("Package Error: Command is given beyond the range of usage.");
		if ( (DynamixelConstants.PROTOCOL.ERR.OVERHEATING & error) > 0)
			throw new Exception("Package Error: Internal temperature is out of the range of operating temperature set in the Control Table.");
		
		if ( (DynamixelConstants.PROTOCOL.ERR.INPUT_VOLTAGE & error) > 0)
			throw new Exception("Package Error: The applied voltage is out of the range of operating voltage set in the Control Table.");
		
		throw new Exception("Package Error: something went wrong when analysing the error byte.");
	}
}