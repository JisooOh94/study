import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.junit.Test;
import com.sun.corba.se.impl.orb.ParserTable;

public class EnumTest {
	public enum Foo {
		foo("Foo"),		//public static final 생략
		bar("Bar");

		Foo(String name) {		//private 접근제한자 생략
			this.name = name;
		}

		private String name;
	}

	@Test
	public void test(){
		Set<Foo> set = new HashSet<Foo>();
		set = EnumSet.of(Foo.foo);

		if(set.contains(Foo.foo)) {
			System.out.println("contain");
		};
	}

	public class Bar {
		public Foo foo;
	}

	@Test
	public void test2() {
		EnumMap<Foo, Bar> map = new EnumMap<Foo, Bar>(Foo.class);
		for(Foo foo : Foo.values()) { map.put(foo, new Bar());}
		Bar bar = new Bar();
		bar.foo = Foo.foo;

		map.put(bar.foo, bar);

		Map<String, BigDecimal> mapp = new HashMap<>();
		mapp.put("A", new BigDecimal(1234.123456789));

		long val = MapUtils.getLong(mapp, "A");
		System.out.println(val);
	}
}
