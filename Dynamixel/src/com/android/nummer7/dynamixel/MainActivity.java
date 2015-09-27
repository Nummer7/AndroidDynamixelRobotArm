package com.android.nummer7.dynamixel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.hoho.android.usbserial.*;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import android.widget.Switch;

public class MainActivity extends FragmentActivity {

	public UsbManager manager;
	public List<UsbSerialDriver> availableDrivers;
	public UsbDeviceConnection connection;
	public UsbSerialDriver driver;
	public UsbSerialPort port;
	
	public Spinner dropdown_baudrate;
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		manager = (UsbManager) getSystemService(Context.USB_SERVICE);
		final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
		final PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		registerReceiver(mUsbReceiver, filter);
		
		dropdown_baudrate = (Spinner) findViewById(R.id.spinner1);
        dropdown_baudrate.setSelection(1);
		
		((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Find all available drivers from attached devices.
            	manager = (UsbManager) getSystemService(Context.USB_SERVICE);
            	availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
            	if (availableDrivers.isEmpty()){
            	  return;
            	}
            	
            	Log.d("STUFF", "mate: 11 ");
				
            	// Open a connection to the first available driver.
            	driver = availableDrivers.get(0);
            	manager.requestPermission(driver.getDevice(), mPermissionIntent);
            	connection = manager.openDevice(driver.getDevice());
            	if (connection == null){
            	  return;
            	}
            	Log.d("STUFF", "mate: 22");
				
            	// Read some data! Most have just one port (port 0).
            	port = driver.getPorts().get(0);
            }
        });
        
        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
					port.open(connection);
					//Log.d("STUFF", "mate: "+Integer.parseInt(dropdown_baudrate.getSelectedItem().toString()));
					port.setParameters(Integer.parseInt(dropdown_baudrate.getSelectedItem().toString()), UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        ((Button) findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DynamixelInstructionPackage p = new DynamixelInstructionPackage();
        		try {
        			byte id = 1;
        			port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 0), p.packageLength);
					port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGEEPROM.CCW_ANGLE_LIMIT_L, (short) 0), p.packageLength);
					port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) 500), p.packageLength);
					port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGRAM.GOAL_POSITION_L, (short) 100), p.packageLength);
					port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 1), p.packageLength);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        ((Button) findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		try {
        			byte id = 1;
        			DynamixelInstructionPackage p = new DynamixelInstructionPackage();
        			port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 0), p.packageLength);
        			port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) 0), p.packageLength);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        ((Switch) findViewById(R.id.switch1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {	
            	DynamixelInstructionPackage p = new DynamixelInstructionPackage();
        		try {
        			byte id = 1;
	        		if (isChecked)
	        			 port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.LED,Dynamixel.ON), p.packageLength);
	        		else port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.LED,Dynamixel.OFF), p.packageLength);
        		} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        ((Button) findViewById(R.id.button5)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		port.close();
            	} catch (IOException e) {
            	}
            }
        });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
