package edu;

class Attribute{
	private String key;
	private String[] value;
	
	Attribute(String k, String[] v){
		key = k;
		value = v;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String[] getValue() {
		return value;
	}
	public void setValue(String[] value) {
		this.value = value;
	}
	
	public boolean contains(String str) {
		boolean ret = false;
		for(int i=0; i<value.length && !ret; i++) {
			if(value[i].equals(str))
				ret = true;
		}
		return ret;
	}
	
	public int indexOf(String str) {
		int ret = 0;
		boolean find = false;
		for(int i=0; i<value.length && !find; i++) {
			if(value[i].equals(str)) {
				ret = i;
				find = true;
			}
		}
		return ret;
	}
}
