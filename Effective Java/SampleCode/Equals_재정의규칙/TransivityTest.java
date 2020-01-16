import java.awt.*;

import org.junit.Assert;

public class TransivityTest {
	public static class Point {
		private int x;
		public Point(int x) { this.x = x; }

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Point) {
				return ((Point)obj).x == this.x;
			}
			return false;
		}
	}

	public static class ColorPoint extends Point {
		private final Color color;

		public ColorPoint(int x, Color color) {
			super(x);
			this.color = color;
		}

		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof Point)) {
				return false;
			}
			if(!(obj instanceof  ColorPoint)) {
				return obj.equals(this);
			}
			return super.equals(obj) && ((ColorPoint)obj).color == color;
		}
	}

	public static void main(String[] args) {
		Point p = new Point(0);
		ColorPoint colorPoint_1 = new ColorPoint(0, Color.RED);
		ColorPoint colorPoint_2 = new ColorPoint(0, Color.BLACK);

		System.out.println(colorPoint_1.equals(p));
		System.out.println(p.equals(colorPoint_2));
		System.out.println(colorPoint_1.equals(colorPoint_2));
	}
}
