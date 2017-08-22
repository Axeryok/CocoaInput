package com.Axeryok.CocoaInput;

public class Rect {
	
	private float x=0;
	private float y=0;
	private float height=0;
	private float width=0;
	
	public Rect(float x,float y,float width,float height){
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
	}
	
	public float getX(){
		return this.x;
	}
	
	public float getY(){
		return this.y;
	}
	
	public float getWidth(){
		return this.width;
	}
	
	public float getHeight(){
		return this.height;
	}
	
}
