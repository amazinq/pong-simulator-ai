package ai;

public class LinearFunction {
	
	private double m;
	private double b;
	
	public LinearFunction(double m, double b) {
		this.setM(m);
		this.setB(b);
	}

	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}
	
	public double getY(double x) {
		return m*x+b;
	}

}
