package dto;

public class Cctv {

	private String wlobscd;
	
	private String name;

	@Override
	public String toString() {
		return super.toString();
	}

	public String getWlobscd() {
		return wlobscd;
	}

	public void setWlobscd(String wlobscd) {
		this.wlobscd = wlobscd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Cctv(String wlobscd, String name) {
		super();
		this.wlobscd = wlobscd;
		this.name = name;
	}
}
