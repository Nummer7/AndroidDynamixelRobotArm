package com.android.nummer7.dynamixel;

import java.util.Arrays;

import android.util.Log;

public class DynamixelInstructionPackage{
	byte[] params;
	//byte length;
	byte packageLength;
	
	byte getChecksum(byte[] bytes, int length){
		int sum = 0;
		for(int i=2; i<length; i++)
			sum += bytes[i];
		return (byte)((~sum) & 0xff);
	}
	
	byte[] WriteByte(byte id, byte address, byte value){
		this.packageLength = 8;
		
		byte[] output = new byte[packageLength];
		output[0] = (byte) 0xff;
		output[1] = (byte) 0xff;
		output[Dynamixel.PROTOCOL.PRT1_PKT.ID] = id;
		output[Dynamixel.PROTOCOL.PRT1_PKT.LENGTH] = (byte) (packageLength-4);
		output[Dynamixel.PROTOCOL.PRT1_PKT.INSTRUCTION] = Dynamixel.PROTOCOL.INST.WRITE;
		
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+0] = address;
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+1] = (byte)(value & 0x00ff);
		//output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+2] = (byte)((value & 0xff00) >> 8);
		
		output[packageLength-1] =  getChecksum(output,packageLength-1);
		return output;
	}
	
	byte[] WriteWord(byte id, byte address, short value){
		this.packageLength = 9;
		
		byte[] output = new byte[packageLength];
		output[0] = (byte) 0xff;
		output[1] = (byte) 0xff;
		output[Dynamixel.PROTOCOL.PRT1_PKT.ID] = id;
		output[Dynamixel.PROTOCOL.PRT1_PKT.LENGTH] = (byte) (packageLength-4);
		output[Dynamixel.PROTOCOL.PRT1_PKT.INSTRUCTION] = Dynamixel.PROTOCOL.INST.WRITE;
		
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+0] = address;
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+1] = (byte)(value & 0x00ff);
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+2] = (byte)((value & 0xff00) >> 8);
		
		output[packageLength-1] =  getChecksum(output,packageLength-1);
		return output;
	}
	
	byte[] ReadByte(byte id, byte address, byte value){
		this.packageLength = 8;
		
		byte[] output = new byte[packageLength];
		output[0] = (byte) 0xff;
		output[1] = (byte) 0xff;
		output[Dynamixel.PROTOCOL.PRT1_PKT.ID] = id;
		output[Dynamixel.PROTOCOL.PRT1_PKT.LENGTH] = (byte) (packageLength-4);
		output[Dynamixel.PROTOCOL.PRT1_PKT.INSTRUCTION] = Dynamixel.PROTOCOL.INST.READ;
		
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+0] = address;
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+1] = 1;
		//output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+2] = (byte)((value & 0xff00) >> 8);
		
		output[packageLength-1] =  getChecksum(output,packageLength-1);
		return output;
	}
	
	/*
	for(byte bt : output){
		Log.d("STUFF", "-> "+bt);
	}
	*/
}