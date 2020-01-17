import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class Ex_1 {
	public class Point {
		private int xPos;
		public Point(int xPos) { this.xPos = xPos; }

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Point) {
				return ((Point)obj).xPos == this.xPos;
			}
			return false;
		}
	}

	public class PointH {
		private int xPos;
		public PointH(int xPos) { this.xPos = xPos; }

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof PointH) {
				return ((PointH)obj).xPos == this.xPos;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return 31 * 17 + xPos;
		}
	}

	@Test
	public void withOutHash() {
		Point p1 = new Point(10);
		Point p2 = new Point(10);

		Assert.assertTrue(p1.equals(p2));		//true

		Map<Point, String> map = new HashMap<>();
		map.put(p1, "Point 1");

		Assert.assertTrue(map.containsKey(p2));		//false
	}

	@Test
	public void withHash() {
		PointH p1 = new PointH(10);
		PointH p2 = new PointH(10);

		Assert.assertTrue(p1.equals(p2));		//true

		Map<PointH, String> map = new HashMap<>();
		map.put(p1, "Point 1");

		Assert.assertTrue(map.containsKey(p2));		//true
	}
}
