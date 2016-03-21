package com.android.nummer7.dynamixel;

import java.io.IOException;
import java.util.regex.Pattern;

import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.usb.UsbManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class rwDynamixelAddress extends LinearLayout{

	String desc;
	byte address;
	short value;
	short minValue;
	short maxValue;
	boolean writable;
	
	DynamixelPackageBuilder p;
	public UsbSerialPort port;
	public Spinner dropdown_id;
	
	
	public void initListeners(){
		((Button) findViewById(R.id.writeBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		p = new DynamixelPackageBuilder();
            		byte id = (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());
            		Log.d("STUFF", "\n"+id+" "+address+" "+value+" --"+maxValue);
            		if (maxValue > 255)
            				port.write(p.WriteWord(id, address, value), p.packageLength);
            		else 	port.write(p.WriteByte( id, address, (byte) value), p.packageLength);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
		
		((Button) findViewById(R.id.readBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		p = new DynamixelPackageBuilder();
            		byte id = (byte) Integer.parseInt(dropdown_id.getSelectedItem().toString());
            		Log.d("STUFF", "\n"+id+" "+address+" "+value+" --"+maxValue);
            		
            		if (maxValue > 255)
        					port.write(p.Read(id, address, (byte)2), p.packageLength);
            		else	port.write(p.ReadByte(id, address), p.packageLength); 	
            		byte[] a = new byte[128];
        			port.read(a,128);
        			//p.analysePackage(a);
        			
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
		
	}
	
	
	public rwDynamixelAddress(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.rwdynamixeladdress, this, true);
	    initListeners();
    }
 
    public rwDynamixelAddress(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.rwdynamixeladdress, this, true);
        init(context, attrs);
        initListeners();
    }
 
    public rwDynamixelAddress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.rwdynamixeladdress, this, true);
        init(context,attrs);
        initListeners();
    }
    
    public void setDesc(String str){
    	if (!this.isInEditMode())
	    	  ((TextView) findViewById(R.id.descText)).setText(desc);
    }
    
    public void setAddress(int arg){
    	address = (byte) arg;
    }
    
    public void setMaxValue(int arg){
    	maxValue = (short) arg;
    }
    
    public void setMinValue(int arg){
    	minValue = (short) arg;
    }
    
    public void setValue(int arg){
    	value = (short) arg;
    	
    	if (!this.isInEditMode())
    		((EditText) findViewById(R.id.valueEdit)).setText(String.valueOf(arg));
    }
    
    public void setWritable(boolean arg){
    	writable = arg;
    	if (!this.isInEditMode()){
    	((Button) findViewById(R.id.writeBtn)).setEnabled(arg);
    	((EditText) findViewById(R.id.valueEdit)).setEnabled(arg);
    	((EditText) findViewById(R.id.valueEdit)).setInputType(InputType.TYPE_CLASS_NUMBER);
    	((EditText) findViewById(R.id.valueEdit)).setFocusable(arg);
    	
    		if (arg){
    			((Button) findViewById(R.id.writeBtn)).setEnabled(arg);
    			((EditText) findViewById(R.id.valueEdit)).setEnabled(arg);
    			((EditText) findViewById(R.id.valueEdit)).setInputType(InputType.TYPE_NULL);
    			((EditText) findViewById(R.id.valueEdit)).setFocusable(arg);
	    	}
    	}
    }
    
	public void init(Context context,AttributeSet attrs) {
	    p = new DynamixelPackageBuilder();
	    
	    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.rwDynamixelAddress );
		for (int i = 0; i < a.getIndexCount(); ++i)
		{
		   int attr = a.getIndex(i);
		   switch (attr)
		   {
		      case R.styleable.rwDynamixelAddress_address:
		    	  setAddress(a.getInteger(i, 0));
			      break;
		      case R.styleable.rwDynamixelAddress_desc:
			      setDesc(a.getString(attr));
			      break;
		      case R.styleable.rwDynamixelAddress_maxValue:
		    	  setMaxValue(a.getInteger(i, 1023));
			      break;
		      case R.styleable.rwDynamixelAddress_minValue:
		    	  setMinValue(a.getInteger(i, 0));
			      break;
		      case R.styleable.rwDynamixelAddress_value:
		    	  setValue(a.getInteger(i, 0));
			      break;
		      case R.styleable.rwDynamixelAddress_readonly:
		    	  setWritable(a.getInteger(i, 0)==0);
			      break;
		   }
		}
		a.recycle();
		
		/*
	    
		((Button) findViewById(R.id.writeBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Log.d("STUFF", "write");
        	    p.WriteByte((byte) 1, address, (byte) value);
            }
        });
		
		((Button) findViewById(R.id.readBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Log.d("STUFF", "read");
            	p.ReadByte((byte) 1, address, (byte) value);
            }
        });
		
		*/
		/*
		((Button) findViewById(R.id.readBtn)).addTextChangedListener(new TextWatcher(){
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				//String text = s.toString();
			      //int length = text.length();
		      
			      if(!Pattern.matches("[0-9]", s)) {
		    	  Log.d("STUFF", "mymatey11: omg");
		    	  }
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});*/
		
		
		//((Activity) context).getLayoutInflater().inflate(R.layout.rwdynamixeladdress, this, true);
		
		/*
		TextView desc = new TextView(context);
		desc.setText("read");
		desc.setPadding(0, 0, 20, 0);
		
		Button writeBtn = new Button(context); 
		//writeBtn.setText(getResources().getString(R.string.a));
		writeBtn.setTextAlignment(Button.TEXT_ALIGNMENT_CENTER);
		writeBtn.setText("W");
		Button readBtn = new Button(context);
		readBtn.setText("R");
		
		
		readBtn.setLayoutParams(new LinearLayout.LayoutParams(70, 70));
		writeBtn.setLayoutParams(new LinearLayout.LayoutParams(70, 70));
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) readBtn.getLayoutParams();
		//btnParams.leftMargin = 0;
		//btnParams.weight = 0;
		//params.width = 20;
		//params.height = 20;
		//readBtn.setLayoutParams(params);
		//readBtn.setLayoutParams(btnParams);

		EditText valueEdit = new EditText(context);
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		valueEdit.setLayoutParams(params);
		valueEdit.setEnabled(false);

		LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) valueEdit.getLayoutParams();
		//p.gravity = LayoutParams.FILL_PARENT;
		//p.weight = 0;
		p.width = LayoutParams.FILL_PARENT;
		valueEdit.setLayoutParams(p);
		
		this.setOrientation(LinearLayout.HORIZONTAL);
		
		this.addView(desc);
		this.addView(writeBtn);
		this.addView(readBtn);
		this.addView(valueEdit);
		*/
	}
	
	
}
