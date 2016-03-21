package com.android.nummer7.dynamixel;

import java.io.IOException;
import java.util.List;

























import android.content.Context;
import android.content.Intent;
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

public class MainActivity extends FragmentActivity {

	public byte id;
	public Spinner dropdown_baudrate;
	public Spinner dropdown_id;
	public DynamixelSocket control;
	public RobotArm robot;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		control = new DynamixelSocket(this);

		try {
			//robot = new RobotArm(control);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		dropdown_baudrate = (Spinner) findViewById(R.id.spinner1);
        dropdown_baudrate.setSelection(12);
		
        dropdown_id = (Spinner) findViewById(R.id.dropdown_id);
        dropdown_id.setSelection(0);
        //byte id = (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());

		((Button) findViewById(R.id.loaddriveperm)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	control.loadDriver();
            }
        });
        
        ((Button) findViewById(R.id.openserial)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
					control.openSerial(Integer.parseInt(dropdown_baudrate.getSelectedItem().toString()));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("STUFF", "exception: " + e.getMessage()); 
				}
            }
        });
        
        ((Button) findViewById(R.id.closeserial)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
					control.closeSerial();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("STUFF", "exception: " + e.getMessage()); 
				}
            }
        });
        
        ((Button) findViewById(R.id.starttestmotionleft)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
					control.continious_motion_left(getId());
				}catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("STUFF", "exception: " + e.getMessage()); 
				}
            }
        });
        
        ((Button) findViewById(R.id.starttestmotionright)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
					control.continious_motion_right(getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("STUFF", "exception: " + e.getMessage()); 
				}
            }
        });
        
        ((Button) findViewById(R.id.stoptestmotion)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
					control.continious_motion_stop(getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("STUFF", "exception: " + e.getMessage()); 
				}
            }
        });
        
        ((SeekBar) findViewById(R.id.seekBar1)).setProgress(150);
		((SeekBar) findViewById(R.id.seekBar1)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				((TextView)  findViewById(R.id.textView3)).setText((progress-150)+"°");
				
				try {
					
					byte id = (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());
					short speed = (short)(((SeekBar) findViewById(R.id.seekBar2)).getProgress()+1);
					//Log.d("STUFF", "ID: "+id+" winkel: "+progress+ " speed:"+speed);
					control.writeWord(id, DynamixelConstants.INSTRUCTIONS.REGEEPROM.CW_ANGLE_LIMIT_L, (short) 0);
					control.writeWord(id, DynamixelConstants.INSTRUCTIONS.REGEEPROM.CCW_ANGLE_LIMIT_L, (short) 1023);
					control.writeAngleAndSpeed(id, (short)progress, speed);
					
					//control.Read((byte) Integer.parseInt(dropdown_id.getSelectedItem().toString()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("STUFF", "exception: " + e.getMessage()); 
				}
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
				((TextView)  findViewById(R.id.textView5)).setText( ((int)(((float)(progress+1))/1022)*114)+" rpm");
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
        
		
		((Button) findViewById(R.id.winkelBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		
            		for (double i=0; i<1; i+=0.2){
            			for (double j=0; j<1; j+=0.2){
            				for (double k=0; k<1; k+=0.2){
            					Log.e("STUFF", "x/y/z - "+i+"/"+j+"/"+k); 
            					goTo(i, j, k);
            				}
            			}
            		}
            		/*
            		Log.e("STUFF", "goTo(0, 1, 1);: "); 
            		goTo(0, 1, 1);
            		Log.e("STUFF", "goTo(1, 0, 1);: "); 
            		goTo(1, 0, 1);
            		Log.e("STUFF", "goTo(1, 1, 0);: "); 
            		goTo(1, 1, 0);
            		
            		Log.e("STUFF", "goTo(1, 0, 0);: "); 
            		goTo(1,0,0);
            		Log.e("STUFF", "goTo(0, 1, 0);: "); 
            		goTo(0,1,0);
            		Log.e("STUFF", "goTo(0, 0, 1);: "); 
            		goTo(0,0,1);
            		
            		Log.e("STUFF", "");
            		*/
            		/*
            		DynamixelAX12 m1 = new DynamixelAX12((byte)1,control);
            		m1.setServoMode();
            		m1.resetTorque();
            		m1.setAngle((short) ((SeekBar)findViewById(R.id.seekBar1)).getProgress());
            		m1.setSpeed((short) ((SeekBar)findViewById(R.id.seekBar2)).getProgress());
            		m1.setTorque();
            		Log.d("STUFF", " iddd:"+getId())
            		*/
            		/*
					control.writeAngleAndSpeedAction((byte)1, (short)150, (short)100);
					control.writeAngleAndSpeedAction((byte)2, (short)150, (short)100);
					control.writeAngleAndSpeedAction((byte)3, (short)150, (short)100);
					control.writeAngleAndSpeedAction((byte)4, (short)150, (short)100);
					*/
            		//short[] angle = {150,150,150,150,150,150};
            		//short[] speed = {150,150,150,150,150,150};
            		//control.write6axes(angle,speed);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
					Log.e("STUFF", "exception: " + e.getMessage()); 
				}
            }
        });
		
		((Button) findViewById(R.id.zuruecksetzenBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		DynamixelAX12 m1 = new DynamixelAX12((byte)1,control);
            		m1.setContinuousMode();
            		m1.resetTorque();
            		m1.continuousMoveLeft();
            		m1.setTorque();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("STUFF", "exception: " + e.getMessage()); 
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
	
	public byte getId(){
    	return (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());
    }
	
	double ro(double in){
		return Math.round(in * 100.0) / 100.0;
	}
	
	void goTo(double x, double y, double z){
		double c = 180/Math.PI;
		double a1=0.07, a2=0.11, a3=0.11;
		double al1=Math.PI/2, al2=0, al3=0;
		double d1=0,d2=0,d3=0;
		double v1=0, v1_1=0, v1_2=0, v2=0, v2_1=0, v2_2=0, v2_3=0, v2_4=0, v3=0, v3_1=0, v3_2=0;
		
		
		double c3 = (Math.pow(x, x)+Math.pow(y, y)+Math.pow(z, z)-Math.pow(a2, a2)-Math.pow(a3, a3))/(2*a2*a3);
		double s3 = Math.sqrt(1-Math.pow(c3,c3));
		double s3m = -1*Math.sqrt(1-Math.pow(c3,c3));
		v3_1 = Math.atan2(s3, c3);
		v3_2 = Math.atan2(s3m, c3);
		
		
		double c2 = (Math.sqrt(Math.pow(x,x)+Math.pow(y,y))*(a2+a3*c3)+z*a3*s3)/(Math.pow(a2,a2)+Math.pow(a3,a3)+2*a2*a3*c3);
		double s2 = (z*(a2+a3*c3)+Math.sqrt(Math.pow(x,x)+Math.pow(y,y)*a3*s3))/(Math.pow(a2,a2)+Math.pow(a3,a3)+2*a3*c3);
		
		v2_1 = Math.atan2((a2+a3*c3)*z-a3*s3*Math.sqrt(Math.pow(x,x)+Math.pow(y,y)), (a2+a3*c3)*Math.sqrt(Math.pow(x,x)+Math.pow(y,y))+a3*s3*z );
		v2_2 = Math.atan2((a2+a3*c3)*z+a3*s3*Math.sqrt(Math.pow(x,x)+Math.pow(y,y)), -(a2+a3*c3)*Math.sqrt(Math.pow(x,x)+Math.pow(y,y))+a3*s3*z );
		v2_3 = Math.atan2((a2+a3*c3)*z-a3*s3m*Math.sqrt(Math.pow(x,x)+Math.pow(y,y)), (a2+a3*c3)*Math.sqrt(Math.pow(x,x)+Math.pow(y,y))+a3*s3m*z );
		v2_2 = Math.atan2((a2+a3*c3)*z+a3*s3m*Math.sqrt(Math.pow(x,x)+Math.pow(y,y)), -(a2+a3*c3)*Math.sqrt(Math.pow(x,x)+Math.pow(y,y))+a3*s3m*z );
		
		
		v1_1 = Math.atan2(y, x);
		v1_2 = Math.atan2(-y, -x);
		// "v1_1:"+ro(v1_1*c)+" v1_2:"+ro(v1_2*c) + " --- "+
		Log.d("STUFF","v2_1:" + ro(v2_1*c)+ " v2_2:" + ro(v2_2*c) + " v2_3:" + ro(v2_3*c) + " v2_4:" + ro(v2_4*c)+ " ---  v3_1:" + ro(v3_1*c)+ " v3_2:"+ro(v3_2*c));
	}
}
