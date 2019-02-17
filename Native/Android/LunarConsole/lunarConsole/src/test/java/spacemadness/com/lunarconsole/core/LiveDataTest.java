package spacemadness.com.lunarconsole.core;

import org.junit.Test;

import spacemadness.com.lunarconsole.TestCase;
import spacemadness.com.lunarconsole.concurrent.ImmediateDispatchQueue;

public class LiveDataTest extends TestCase {
	@Test
	public void testObservers() {
		MutableLiveData<String> data = new MockLiveData<>("a");
		Disposable disposable1 = data.observe(new MockObserver<String>("1"));
		assertResults("1: a");

		Disposable disposable2 = data.observe(new MockObserver<String>("2"));
		Disposable disposable3 = data.observe(new MockObserver<String>("3"));
		assertResults("2: a", "3: a");

		data.setValue("b");
		assertResults("1: b", "2: b", "3: b");

		data.setValue("c");
		assertResults("1: c", "2: c", "3: c");

		disposable1.dispose();

		data.setValue("d");
		assertResults("2: d", "3: d");

		disposable3.dispose();

		data.setValue("e");
		assertResults("2: e");

		disposable2.dispose();

		data.setValue("f");
		assertResults();
	}

	class MockObserver<T> implements Observer<T> {

		private final String name;

		MockObserver(String name) {
			this.name = name;
		}

		@Override
		public void onChanged(T t) {
			addResult(name + ": " + t);
		}
	}

	static class MockLiveData<T> extends MutableLiveData<T> {
		MockLiveData(T value) {
			super(value, new ImmediateDispatchQueue());
		}
	}
}