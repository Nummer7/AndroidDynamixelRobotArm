package com.android.nummer7.dynamixel;

import java.io.IOException;

import android.util.Log;
import java.util.Arrays;

public class DynamixelConstants {
	 static final short MAX_ID		 	= (252);
	 static final int DXL_MAXNUM_TXPACKET  = 10000;
	 static final int  DXL_MAXNUM_RXPACKET = 10000;
		
	 final public static byte LATENCY_TIME		= (byte)(16); //ms (USB2Dynamixel Default Latency Time)
	 final public static byte PING_STATUS_LENGTH = (byte)(14);
	 
	 final public static byte ON		= (byte)(1);
	 final public static byte OFF		= (byte)(0);
	 
	 public class PROTOCOL{
		 /*
		 public class COMM{
			 static final byte TXSUCCESS	= (0);  //Succeed transmit instruction packet
			 static final byte RXSUCCESS	= (1);  //Succeed get status packet
			 static final byte TXFAIL		= (2);  //Failed transmit instruction packet
			 static final byte RXFAIL		= (3);  //Failed get status packet
			 static final byte TXERROR		= (4);  //Incorrect instruction packet
			 static final byte RXWAITING	= (5);  //Now recieving status packet
			 static final byte RXTIMEOUT	= (6);  //There is no status packet
			 static final byte RXCORRUPT	= (7);  //Incorrect status packet
		 }
		 */
		 
		 static final short ERRBIT_ALERT	= (128);//When the device has a problem, it is et as 1. Check "Device Status Check" value.
	
		 public class ERR{
			 //static final byte depRESULT_FAIL		= (1);  //Failed to process the instruction packet.
			 
			 static final byte INSTRUCTION		= (64);  //Instruction error
			 static final byte OVERLOAD		= (32);  //current load cannot be controlled with set maximum torque
			 static final byte CRC				= (16);  //CRC check error
			 static final byte DATA_RANGE		= (8);  //Data range error
			 static final byte OVERHEATING		= (4);  
			 static final byte ANGLE_LIMIT		= (2);  
			 static final byte INPUT_VOLTAGE	= (1);
			 
			 //static final byte depDATA_LENGTH		= (16);  //Data length error
			 //static final byte depDATA_LIMIT		= (32);  //Data limit error
			 //static final byte depACCESS			= (64);  //Access error
		 }
	
		 public class PRT1_PKT{
			 static final byte ID					= (2);
			 static final byte LENGTH				= (3);
			 static final byte INSTRUCTION		= (4);
			 static final byte ERRBIT				= (4);
			 static final byte PARAMETER0			= (5);
		 }
	
		 public class INST{
			 /////// Common Instruction for 1.0 and 2.0
			 static final byte PING			= (1);
			 static final byte READ			= (2);
			 static final byte WRITE			= (3);
			 static final byte REG_WRITE		= (4);
			 static final byte ACTION			= (5);
			 static final byte RESET			= (6);
			 static final byte SYNC_WRITE		= (byte)0x83;
			 static final short	BULK_READ      = (byte) 0x92;  
		 }
		 
		 public class PING_INFO{
		 static final byte MODEL_NUM			= (1);
		 static final byte FIRM_VER				= (2);
	 }
	 }	
	
	 public class INSTRUCTIONS{
		 final static short  MAX_IN_CHAR = 128;
		 final static byte  DX_CW_ANGLE_LIMIT = 6;
		 final static byte  DX_CCW_ANGLE_LIMIT = 8;
		 final static byte  DX_GOAL_POSITION = 30;
		 final static byte  DX_MOVING_SPEED = 32;
		
		 public class REGEEPROM{
			 // EEPROM AREA  ///////////////////////////////////////////////////////////
			 final static byte  MODEL_NUMBER_L           = 0;
			 final static byte  MODEL_NUMBER_H           = 1;
			 final static byte  VERSION                  = 2;
			 final static byte  ID                       = 3;
			 final static byte  BAUD_RATE                = 4;
			 final static byte  RETURN_DELAY_TIME        = 5;
			 final static byte  CW_ANGLE_LIMIT_L         = 6;
			 final static byte  CW_ANGLE_LIMIT_H         = 7;
			 final static byte  CCW_ANGLE_LIMIT_L        = 8;
			 final static byte  CCW_ANGLE_LIMIT_H        = 9;
			 final static byte  SYSTEM_DATA2             = 10;
			 final static byte  LIMIT_TEMPERATURE        = 11;
			 final static byte  DOWN_LIMIT_VOLTAGE       = 12;
			 final static byte  UP_LIMIT_VOLTAGE         = 13;
			 final static byte  MAX_TORQUE_L             = 14;
			 final static byte  MAX_TORQUE_H             = 15;
			 final static byte  RETURN_LEVEL             = 16;
			 final static byte  ALARM_LED                = 17;
			 final static byte  ALARM_SHUTDOWN           = 18;
			 final static byte  OPERATING_MODE           = 19;
			 final static byte  DOWN_CALIBRATION_L       = 20;
			 final static byte  DOWN_CALIBRATION_H       = 21;
			 final static byte  UP_CALIBRATION_L         = 22;
			 final static byte  UP_CALIBRATION_H         = 23;
		 }
		 
		 public class REGRAM{
			 // RAM AREA  //////////////////////////////////////////////////////////////
			 final static byte  TORQUE_ENABLE            = 24;
			 final static byte  LED                      = 25;
			 final static byte  CW_COMPLIANCE_MARGIN     = 26;
			 final static byte  CCW_COMPLIANCE_MARGIN    = 27;
			 final static byte  CW_COMPLIANCE_SLOPE      = 28;
			 final static byte  CCW_COMPLIANCE_SLOPE     = 29;
			 final static byte  GOAL_POSITION_L          = 30;
			 final static byte  GOAL_POSITION_H          = 31;
			 final static byte  GOAL_SPEED_L             = 32;
			 final static byte  GOAL_SPEED_H             = 33;
			 final static byte  TORQUE_LIMIT_L           = 34;
			 final static byte  TORQUE_LIMIT_H           = 35;
			 final static byte  PRESENT_POSITION_H       = 37;
			 final static byte  PRESENT_SPEED_L          = 38;
			 final static byte  PRESENT_SPEED_H          = 39;
			 final static byte  PRESENT_LOAD_L           = 40;
			 final static byte  PRESENT_LOAD_H           = 41;
			 final static byte  PRESENT_VOLTAGE          = 42;
			 final static byte  PRESENT_TEMPERATURE      = 43;
			 final static byte  REGISTERED_INSTRUCTION   = 44;
			 final static byte  PAUSE_TIME               = 45;
			 final static byte  MOVING                   = 46;
			 final static byte  LOCK                     = 47;
			 final static byte  PUNCH_L                  = 48;
			 final static byte  PUNCH_H                  = 49;
		 }
		 
		 public class RETURNLEVEL{
			 // Status Return Levels ///////////////////////////////////////////////////////////////
			 final static byte  RETURN_NONE              = 0;
			 final static byte  RETURN_READ              = 1;
			 final static byte  RETURN_ALL               = 2;
		 }

		 	// Specials ///////////////////////////////////////////////////////////////
		 final static byte  LEFT						= 0;
		 final static byte  RIGTH                       = 1;
		 final static byte  DX_BYTE_READ                = 1;
		 final static byte  DX_BYTE_READ_POS            = 2;
		 final static byte  DX_RESET_LENGTH				= 2;
		 final static byte  DX_ACTION_LENGTH				= 2;
		 final static byte  DX_ID_LENGTH                = 4;
		 final static byte  DX_LR_LENGTH                = 4;
		 final static byte  DX_SRL_LENGTH               = 4;
		 final static byte  DX_RDT_LENGTH               = 4;
		 final static byte  DX_LEDALARM_LENGTH          = 4;
		 final static byte  DX_SALARM_LENGTH            = 4;
		 final static byte  DX_TL_LENGTH                = 4;
		 final static byte  DX_VL_LENGTH                = 6;
		 final static byte  DX_CM_LENGTH                = 6;
		 final static byte  DX_CS_LENGTH                = 6;
		 final static byte  DX_CCW_CW_LENGTH            = 8;
		 final static byte  DX_BD_LENGTH                = 4;
		 final static byte  DX_TEM_LENGTH               = 4;
		 final static byte  DX_MOVING_LENGTH            = 4;
		 final static byte  DX_RWS_LENGTH               = 4;
		 final static byte  DX_VOLT_LENGTH              = 4;
		 final static byte  DX_LED_LENGTH               = 4;
		 final static byte  DX_TORQUE_LENGTH            = 4;
		 final static byte  DX_POS_LENGTH               = 4;
		 final static byte  DX_GOAL_LENGTH              = 5;
		 final static byte  DX_MT_LENGTH                = 5;
		 final static byte  DX_PUNCH_LENGTH             = 5;
		 final static byte  DX_SPEED_LENGTH             = 5;
		 final static byte  DX_GOAL_SP_LENGTH           = 7;
		 final static short  DX_ACTION_CHECKSUM			= 250;
		 final static short  BROADCAST_ID                = 254;
		 final static short  DX_START                    = 255;
		 final static short  DX_CCW_AL_L                 = 255;
		 final static byte  AX_CCW_AL_H                 = 3;
		 final static byte  TIME_OUT                    = 10;     // Este parametro depende de la velocidad de transmision
		 final static short  TX_DELAY_TIME				= 400;        // Este parametro depende de la velocidad de transmision - pero pueden ser cambiados para mayor velocidad.
		 final static byte  Tx_MODE                     = 1;
		 final static byte  Rx_MODE                     = 0;
		 final static byte  LOCK                        = 1;
	}
}
