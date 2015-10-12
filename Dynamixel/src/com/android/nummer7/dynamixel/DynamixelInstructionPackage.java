package com.android.nummer7.dynamixel;

import java.util.Arrays;

import android.util.Log;

public class DynamixelInstructionPackage{
	
	final byte analyseLength = (byte) 128;
	byte analyseError;
	
	byte[] params;
	//byte length;
	byte packageLength;
	
	byte error;
	
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
	
	byte[] ReadByte(byte id, byte address){
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
		
		output[packageLength-1] =  getChecksum(output,packageLength);
		return output;
	}
	
	byte[] Read(byte id, byte address, byte length){
		this.packageLength = 8;
		
		byte[] output = new byte[packageLength];
		output[0] = (byte) 0xff;
		output[1] = (byte) 0xff;
		output[Dynamixel.PROTOCOL.PRT1_PKT.ID] = id;
		output[Dynamixel.PROTOCOL.PRT1_PKT.LENGTH] = (byte) (packageLength-4);
		output[Dynamixel.PROTOCOL.PRT1_PKT.INSTRUCTION] = Dynamixel.PROTOCOL.INST.READ;
		
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+0] = address;
		output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+1] = length;
		//output[Dynamixel.PROTOCOL.PRT1_PKT.PARAMETER0+2] = (byte)((value & 0xff00) >> 8);
		
		output[packageLength-1] =  getChecksum(output,packageLength-1);
		return output;
	}
	
	short getWord(byte[] data){
		error = data[4];
		Log.d("STUFF", "-> "+Arrays.toString(data));
		return (short) (  ( (short) (0xFF & data[6]) << 8) & ((short) 0x00FF & data[5]) );
	}
	byte getByte(byte[] data)
	{
		error = data[4];
		Log.d("STUFF", "-> "+Arrays.toString(data));
		return data[5];
	}
	
	void analysePackage(byte[] data){

		int start = 0;
		/*
		for (int i=0; i<analyseLength-1; i++)
			if (data[i] == (byte)0xff && data[i+1] == (byte)0xff){
				start = i;
				break;
			}
		
		int tempLen = (int) data[start+3]+3;
		byte[] dst = Arrays.copyOfRange(data,start,start+tempLen);
		Log.d("STUFF", "-> "+Arrays.toString(dst));
		*/
		Log.d("STUFF", "-> "+Arrays.toString(data));
	}
	
}