package com.android.nummer7.dynamixel;

import java.io.IOException;
import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

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
	public Spinner dropdown_id;
	public byte id;
	
	private float[] getMatrix(float alpha, float[] offset3D){
			float[] a = {	(float) Math.cos(alpha), (float) -Math.sin(alpha), 		0, 				offset3D[0], 
	              			(float) Math.sin(alpha), (float) Math.cos(alpha), 		0, 				offset3D[1],
	              								0, 					0, 				1, 				offset3D[2],
	              								0, 					0, 				0, 				1};
			return a;
	}
	
	
	
	
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
        dropdown_baudrate.setSelection(12);
		
        dropdown_id = (Spinner) findViewById(R.id.dropdown_id);
        dropdown_id.setSelection(0);
        //byte id = (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());

		((Button) findViewById(R.id.loaddriveperm)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Find all available drivers from attached devices.
            	manager = (UsbManager) getSystemService(Context.USB_SERVICE);
            	availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
            	if (availableDrivers.isEmpty()){
            	  return;
            	}
            	
            	//Log.d("STUFF", "mate: 11 ");
				
            	// Open a connection to the first available driver.
            	driver = availableDrivers.get(0);
            	manager.requestPermission(driver.getDevice(), mPermissionIntent);
            	connection = manager.openDevice(driver.getDevice());
            	if (connection == null){
            	  return;
            	}
            	//Log.d("STUFF", "mate: 22");
				
            	// Read some data! Most have just one port (port 0).
            	port = driver.getPorts().get(0);
            }
        });
        
        ((Button) findViewById(R.id.openserial)).setOnClickListener(new View.OnClickListener() {
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
        
        ((Button) findViewById(R.id.closeserial)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		port.close();
            	} catch (IOException e) {
            	}
            }
        });
        
        ((Button) findViewById(R.id.starttestmotionleft)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DynamixelInstructionPackage p = new DynamixelInstructionPackage();
        		try {
        			byte id = (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());
        			Log.d("STUFF", "using id"+id);
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
        
        ((Button) findViewById(R.id.starttestmotionright)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DynamixelInstructionPackage p = new DynamixelInstructionPackage();
        		try {
        			byte id = (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());
        			Log.d("STUFF", "using id"+id);
        			port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 0), p.packageLength);
					port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGEEPROM.CCW_ANGLE_LIMIT_L, (short) 0), p.packageLength);
					port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) 1500), p.packageLength);
					port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGRAM.GOAL_POSITION_L, (short) 100), p.packageLength);
					port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 1), p.packageLength);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        ((Button) findViewById(R.id.stoptestmotion)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		try {
        			byte id = (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());
        			Log.d("STUFF", "using id"+id);
        			DynamixelInstructionPackage p = new DynamixelInstructionPackage();
        			port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 0), p.packageLength);
        			port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) 0), p.packageLength);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        final byte[] addresses = {Dynamixel.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, 
        			Dynamixel.INSTRUCTIONS.REGRAM.GOAL_POSITION_L,
        			Dynamixel.INSTRUCTIONS.REGRAM.TORQUE_ENABLE,
        			Dynamixel.INSTRUCTIONS.REGEEPROM.CCW_ANGLE_LIMIT_L
        			};
        short[] limits = {(short) 65353, (short) 65353, (short)255, (short)65353};
        String[] descs = {"Speed", "Position", "Torque", "FreeRunning"};
        
        LinearLayout a = (LinearLayout) findViewById(R.id.linlay);
        rwDynamixelAddress n;
        for (int i=0; i<4; i++){
        	n = new rwDynamixelAddress(this);
        	n.dropdown_id = dropdown_id;
        	n.setDesc(descs[i]);
        	n.setAddress(addresses[i]);
        	n.setMinValue(0);
        	n.setMaxValue(limits[i]);
        	n.setValue(1);
        	n.initListeners();
        	a.addView(n);
        }

	    //port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.LED,Dynamixel.ON), p.packageLength);
	    //port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.LED,Dynamixel.OFF), p.packageLength);

        ((Button) findViewById(R.id.button7)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		try {
        			byte id = (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());
        			Log.d("STUFF", "using id"+id);
        			DynamixelInstructionPackage p = new DynamixelInstructionPackage();
        			
        			port.write(p.ReadByte(id, Dynamixel.INSTRUCTIONS.REGRAM.LED), p.packageLength);
        			byte[] a = new byte[128];
        			port.read(a,128);
        			p.analysePackage(a);
        			//port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) 0), p.packageLength);
        			
        		} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        
        ((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		try {
        			byte id = (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());
        			Log.d("STUFF", "using id"+id);
        			DynamixelInstructionPackage p = new DynamixelInstructionPackage();
        			
        			port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGEEPROM.ID, (byte)3), p.packageLength);
        			//byte[] a = new byte[128];
        			//port.read(a,128);
        			//p.analysePackage(a);
        			//port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) 0), p.packageLength);
        			
        		} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        
        ((SeekBar) findViewById(R.id.seekBar1)).setProgress(150);
		((SeekBar) findViewById(R.id.seekBar1)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				((TextView)  findViewById(R.id.textView3)).setText((progress-150)+"°");
				setPos(Integer.parseInt(dropdown_id.getSelectedItem().toString()),(progress*1022)/300);
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
        
		((SeekBar) findViewById(R.id.seekBar2)).setProgress(200);
		((SeekBar) findViewById(R.id.seekBar2)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				((TextView)  findViewById(R.id.textView5)).setText( ((int)((progress+1)/1022)*114)+" rpm");
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
        
		((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	float[] alpha = {0,45,45,0,0,0};
        		
        		float[] one = {	1,0,0,0,
        						0,1,0,0,
        						0,0,1,0,
        						0,0,0,1};
        		Matrix.rotateM(one, 0, alpha[0], (float) 0, (float) 0, (float) 1);
        		Matrix.translateM(one, 0, 0, 0, 5);
        		
        		Matrix.rotateM(one, 0, 90, (float) 1, (float) 0, (float) 0);
        		Matrix.rotateM(one, 0, alpha[1], (float) 1, (float) 1, (float) 0);
        		
        		Matrix.translateM(one, 0, 0, 0, 11);
        		Matrix.rotateM(one, 0, alpha[2], (float) 1, (float) 1, (float) 0);

        		Matrix.translateM(one, 0, 0, 0, 11);
        		
        		/*
        		float[] vec = {1,0,0,1};
        		float[] vecres = {0,0,0,0};
        		
        		Matrix.multiplyMV(vecres, 0, one, 0, vec, 0);
        		*/
        		String g = "\n";
        		for(int i=0; i<16; i++){
        			g += ((int) one[i])+" ";
        			if ((i % 4) == 3)
        				g += "\n";
        		}
        		g += "\n";
        		Log.d("STUFF", "Matrix: "+g);
        		
        		for(int i=0; i<6; i++){
        			setPos(i,alpha[i]);
        		}
            }
        });
		
		((Button) findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	//setPos(1,150);
            	//setPos(2,150);
            	setPos(3,150);
            	//setPos(4,150);
            	//setPos(5,150);
            	
        		//for(int i=1; i<6; i++)
        		//	setPos(i,150);
            }
        });


        ((Button) findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		try {
        			byte id = (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());
        			DynamixelInstructionPackage p = new DynamixelInstructionPackage();
        			
        			Log.d("STUFF", "using id"+id);
        			port.write(p.Read(id, Dynamixel.INSTRUCTIONS.REGEEPROM.CW_ANGLE_LIMIT_L, (byte)2), p.packageLength);
        			byte[] a = new byte[128];
        			port.read(a,128);
        			Log.d("STUFF", "word: "+p.getWord(a));
        			//p.analysePackage(a);
        			
        			Log.d("STUFF", "mopit");
        			port.write(p.Read(id, Dynamixel.INSTRUCTIONS.REGEEPROM.CCW_ANGLE_LIMIT_L, (byte)2), p.packageLength);
        			a = new byte[128];
        			port.read(a,128);
        			Log.d("STUFF", "word: "+p.getWord(a));
        			//p.analysePackage(a);
        			
        			//port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) 0), p.packageLength);
        			
        		} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        
		
		/*
		g = "\n";
		for(int i=0; i<4; i++){
			g += ((int) vecres[i])+" ";
		}
		g += "\n";
		Log.d("STUFF", "Matrix: "+g);
		*/
	}
	
	public void setPos(int id2, float angle){
		byte id = (byte) id2;
		
		short pos = (short) ((angle*1020)/300);
		Log.d("STUFF", angle+" angle: "+pos);
		try {
			DynamixelInstructionPackage p = new DynamixelInstructionPackage();
			port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 0), p.packageLength);
			port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGRAM.GOAL_SPEED_L, (short) (((SeekBar) findViewById(R.id.seekBar2)).getProgress()+1)), p.packageLength);
			port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGEEPROM.CCW_ANGLE_LIMIT_L, (short) 0x03FF), p.packageLength);
			port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGRAM.GOAL_POSITION_L, pos), p.packageLength);
			port.write(p.WriteByte(id, Dynamixel.INSTRUCTIONS.REGRAM.TORQUE_ENABLE, (byte) 1), p.packageLength);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//port.write(p.WriteWord(id, Dynamixel.INSTRUCTIONS.REGEEPROM.CCW_ANGLE_LIMIT_L, (short) 0), p.packageLength);
		
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
