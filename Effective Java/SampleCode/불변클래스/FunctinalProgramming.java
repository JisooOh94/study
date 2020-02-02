public class FunctinalProgramming {
	public final class Complex {
		private final double realNum;
		private final double imaginaryNum;

		public Complex(double re, double im) {
			this.realNum = re;
			this.imaginaryNum = im;
		}

		public double getRealNum() {
			return realNum;
		}

		public double getImaginaryNum() {
			return imaginaryNum;
		}

		public Complex plus(Complex c) {
			return new Complex(this.realNum + c.getRealNum(), this.imaginaryNum + c.imaginaryNum);
		}
	}
}
