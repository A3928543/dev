package com.ixe.ods.sica.rmds.feed.util;

public enum FID {
	
	BID(22),
	ASK(25);
	
	short dif;
	
	public short getDif() {
		return dif;
	}

	public void setDif(short dif) {
		this.dif = dif;
	}

	private FID(Integer fid){
		this.dif = fid.shortValue();
	}

}
