import java.util.Collection;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

public class HashSetTest {
	public class HashSet {
		private Set<Integer> set;

		public boolean add(int elem) { return set.add(elem);
		}

		public boolean addAll(Collection<Integer> c) {
			if(CollectionUtils.isEmpty(c)) {
				return false;
			}
			for(Integer value : c) {
				this.add(value);
			}
			return true;
		}
	}

	public class AdvancedHashSet extends HashSet {
		private int addCount = 0;

		@Override
		public boolean add(int elem) {
			addCount++;
			return super.add(elem);
		}

		@Override
		public boolean addAll(Collection<Integer> c) {
			addCount += c.size();	//HashSet의 addAll 메서드는 add 메서드를 다시 호출하여 처리함
			return super.addAll(c);			//자식클래스의 @Override된 add가 호출되어 addCount 에 c.size() *2가 더해짐
		}
	}
}
