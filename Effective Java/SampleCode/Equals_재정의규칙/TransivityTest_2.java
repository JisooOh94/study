import java.awt.*;
import java.sql.Timestamp;

public class TransivityTest_2 {
	public static class Point_v1 {
		private int x;
		public Point_v1(int x) { this.x = x; }

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Point_v1) {
				return ((Point_v1)obj).x == this.x;
			}
			return false;
		}
	}

	public static class ColorPoint_v1 extends Point_v1 {
		private final Color color;

		public ColorPoint_v1(int x, Color color) {
			super(x);
			this.color = color;
		}

		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof Point_v1)) {
				return false;
			}
			if(!(obj instanceof ColorPoint_v1)) {
				return obj.equals(this);
			}
			return super.equals(obj) && ((ColorPoint_v1)obj).color == color;
		}
	}

	public static class Point_v2 {
		protected int x;
		public Point_v2(int x) { this.x = x; }

		@Override
		public boolean equals(Object obj) {
			if(obj.getClass() == getClass()) {
				return ((Point_v2)obj).x == this.x;
			}
			return false;
		}
	}

	public static class ColorPoint_v2 extends Point_v2 {
		private final Color color;

		public ColorPoint_v2(int x, Color color) {
			super(x);
			this.color = color;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj.getClass() == getClass()){
				Point_v2 p_2 = (Point_v2)obj;
				return p_2.x == this.x;
			}
			return false;
		}
	}

	public static void main(String[] args) {

		Point_v1 p_1 = new Point_v1(0);
		ColorPoint_v1 colorPoint_1 = new ColorPoint_v1(0, Color.RED);

		Point_v2 p_2 = new Point_v2(0);
		ColorPoint_v2 colorPoint_2 = new ColorPoint_v2(0, Color.RED);

		System.out.println(colorPoint_1.equals(p_1));
		System.out.println(p_1.equals(colorPoint_1));
		System.out.println(p_1 instanceof ColorPoint_v1);

		System.out.println(colorPoint_2.equals(p_2));
		System.out.println(p_2.equals(colorPoint_2));
	}
}
