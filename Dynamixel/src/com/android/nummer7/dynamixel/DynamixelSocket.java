package com.android.nummer7.dynamixel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

public class DynamixelSocket extends View{
	public UsbManager manager;
	public List<UsbSerialDriver> availableDrivers;
	public UsbDeviceConnection connection;
	public UsbSerialDriver driver;
	public UsbSerialPort port;
	public Context context;
	private PendingIntent mPermissionIntent;
	
	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (ACTION_USB_PERMISSION.equals(action)) {
	            synchronized (this) {
	                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
	                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
	                    if(device != null){
	                      //call method to set up device communication
	                   }
	                }
	                else {
	                    Log.d("STUFF", "permission denied for device " + device);
	                }
	            }
	        }
	    }
	};
	
	private static DynamixelPackageBuilder packageBuilder = new DynamixelPackageBuilder();
	
	public DynamixelSocket(Context context){
		super(context);
		this.context = context;
		manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		 mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		context.registerReceiver(mUsbReceiver, filter);
	}
	
	public void loadDriver(){
    	// Find all available drivers from attached devices.
    	manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    	availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
    	if (availableDrivers.isEmpty()){
    	  return;
    	}

    	// Open a connection to the first available driver.
    	driver = availableDrivers.get(0);
    	manager.requestPermission(driver.getDevice(), mPermissionIntent);
    	connection = manager.openDevice(driver.getDevice());
    	if (connection == null){
    	  return;
    	}

    	// Read some data! Most have just one port (port 0).
    	port = driver.getPorts().get(0);
	}
	
	public void openSerial(int baudrate) throws IOException{
		port.open(connection);
		//Log.d("STUFF", "mate: "+Integer.parseInt(dropdown_baudrate.getSelectedItem().toString()));
		port.setParameters(baudrate, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
	}
	
	public void closeSerial() throws IOException{
    		port.close();
	}
	
	public void continious_motion_left(byte id) throws Exception{
		writeByte(id, DynamixelConstants.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 0);
		writeWord(id, DynamixelConstants.INSTRUCTIONS.REGEEPROM.CCW_ANGLE_LIMIT_L, (short) 0);
		writeWord(id, DynamixelConstants.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) 500);
		writeWord(id, DynamixelConstants.INSTRUCTIONS.REGRAM.GOAL_POSITION_L, (short) 100);
		writeByte(id, DynamixelConstants.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 1);
	}
	
	public void continious_motion_right (byte id) throws Exception{
		writeByte(id, DynamixelConstants.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 0);
		writeWord(id, DynamixelConstants.INSTRUCTIONS.REGEEPROM.CCW_ANGLE_LIMIT_L, (short) 0);
		writeWord(id, DynamixelConstants.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) 1500);
		writeWord(id, DynamixelConstants.INSTRUCTIONS.REGRAM.GOAL_POSITION_L, (short) 100);
		writeByte(id, DynamixelConstants.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 1);
	}
	
	public void continious_motion_stop(byte id) throws Exception{
		writeByte(id, DynamixelConstants.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 0);
		writeWord(id, DynamixelConstants.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) 0);
	}
	
	public void continious_motion_start(byte id) throws Exception{
		writeByte(id, DynamixelConstants.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 0);
		writeWord(id, DynamixelConstants.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) 0);
	}
	
	public void writeWord(byte id, byte address, short value) throws Exception{
		port.write(packageBuilder.WriteWord(id, address, value), packageBuilder.packageLength);
		readStatusPackage();
	}
	
	public void writeByte(byte i, byte address, byte j) throws Exception{
		port.write(packageBuilder.WriteByte(i, address, j), packageBuilder.packageLength);
		readStatusPackage();
	}
	
	public void writeAngleAndSpeed(byte id, short angle, short speed) throws Exception{
		port.write(packageBuilder.WritePositionAndSpeed(id, (short) ((angle*1023)/300), speed),packageBuilder.packageLength);
		readStatusPackage();
	}
	/*
	public void writeAngleAndSpeedAction(byte id, short angle, short speed) throws Exception{
		port.write(packageBuilder.WritePositionAndSpeed(id, (short) ((angle*1023)/300), speed),packageBuilder.packageLength);
		readStatusPackage();
	}
	public void executeAction() throws Exception{
		port.write(packageBuilder.WriteAction(),packageBuilder.packageLength);
		readStatusPackage();
	}
	
	
	public void write6axes(short[] angle, short[] speed) throws Exception{
		angle[0] = (short) ((angle[0]*1023)/300);
		angle[1] = (short) ((angle[1]*1023)/300);
		angle[2] = (short) ((angle[2]*1023)/300);
		angle[3] = (short) ((angle[3]*1023)/300);
		angle[4] = (short) ((angle[4]*1023)/300);
		angle[5] = (short) ((angle[5]*1023)/300);
		port.write(packageBuilder.WritePositionAndSpeedAction_6axes(angle, speed),packageBuilder.packageLength);
		//readStatusPackage();
	}
	*/
	
	public byte[] readStatusPackage() throws Exception{
		byte[] a = new byte[1024];
		int length = 0;
		int f = 0;
		while(length == 0){
			length = port.read(a,5000);
			if (f++ > 100){
				throw new Exception("Read: Timeout");
			}
		}
		if (length == 0)
			throw new Exception("Byte buffer is empty.");
		byte[] b = packageBuilder.checkReadBytes(a, length);
		if (b[0] != (byte)-1){
			throw new Exception("Packet konnte nicht gelesen werden.");
		}
		return b;
	}
	
	public byte[] read(byte id, byte address, byte length) throws Exception{
		port.write(packageBuilder.Read(id, address, length), packageBuilder.packageLength);
		return readStatusPackage();
	}
	
	public byte readByte(byte id, byte address) throws Exception{
		byte[] a = read(id, address, (byte)1);
		return a[5];
	}
	
	public short readWord(byte id, byte address) throws Exception{
		byte[] a = read(id, address, (byte)2);
		return (short) ((short)(a[5] & 0xFF)+(a[6] << 8));
		//return (short)(a[6] << 8);
	}

}
