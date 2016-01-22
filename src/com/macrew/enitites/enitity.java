package com.macrew.enitites;

import java.util.ArrayList;

public class enitity {
	
	public String NAME;
	public String  PHONE;
	 private boolean selected;
	
	public enitity(String name, String phone) {
	    super();
	    NAME = name;
	    PHONE = phone;
	}
	public String getName() {
	    return NAME;
	}
	public void setName(String name) {
	    NAME = name;
	}
	public String getPhone() {
	    return PHONE;
	}
	public void setPhone(String phone) {
	    PHONE = phone;
	}
	 public boolean isSelected() {
	        return selected;
	    }
	 
	    public void setSelected(boolean selected) {
	        this.selected = selected;
	    }
}
