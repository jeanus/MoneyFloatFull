package viking;

public class Bar {
	private double high;
	private double low ;
	private double open;
	private double close;	
	private double volume;
	private double change;
	

	public Bar(double high, double low, double open, double close,double volume,double change) {
		super();
		this.high = high;
		this.low = low;
		this.open = open;
		this.close = close;
		this.volume = volume;
		this.change = change;
	}
	public double getUpShadow(){
		return high - Math.max(open, close);
	}
	
	public double getDownShadow(){
		return Math.min(open, close)-low;
	}
	
	public double getBody(){
		return Math.abs(open-close);
	}
	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}
	boolean isYang(){
		return (close>open);
	}
	
	public double getVolume() {
		return volume;
	}


	public void setVolume(double volume) {
		this.volume = volume;
	}


	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	@Override
	public String toString() {
		return "Bar [high=" + high + ", low=" + low + ", open=" + open
				+ ", close=" + close + ", volume=" + volume + ", change="
				+ change + "]";
	}
	

}
