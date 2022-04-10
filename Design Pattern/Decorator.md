# Decorator
* 기존 클래스 객체에 부가 기능을 수행하는 모듈을 추가하여 기존 기능을 업그레이드하는 패턴
    * 소스를 수정하지 않고, 모듈 클래스를 조합하여 새로운 기능의 클래스 객체 생성 가능
      ![7raW](https://user-images.githubusercontent.com/48702893/160821171-c40a3119-bfa6-4fc4-a443-b34f817120f1.gif)
* 가장 기본적인 소스가 되는 base 클래스와, 그 구현체, 부가 기능 모듈 클래스로 구성
* base 클래스는 부가 기능 모듈들을 장착하여 기능을 자유롭게 업그레이드 시킬 메서드를 추상메서드로 선언
* 모듈 클래스는 base 클래스를 상속받음과 동시에 base 클래스 타입 필드를 가지는 구조
    * 상속받은 base클래스의 추상메서드를 구현하며 부가 기능 로직 추가, 부가 기능 수행후 base 클래스 필드를 통해 원본메서드 호출(일종의 AOP 처럼 동작)
    * 다른 모듈 클래스를 자유롭게 추가 가능하나(recursive), 업그레이드 하지 않을 메서드도 구현해야하는 단점 존재

```java
import java.time.LocalDateTime;

//base 클래스
public abstract class BaseLogger {
  protected abstract String getLog(String msg);

  public void printLog(String log) {
    System.out.println(getLog(msg));
  }
}

//base 클래스 구현체
public class BaseLoggerImpl extends BaseLogger {
  protected String getLog(String msg) {
    return "[" + LogLevel.DEBUG + "]" + msg;
  }
}

//모듈 1
public class DateLogger extends BaseLogger {
  protected BaseLogger baseLogger;      //구현체 클래스 타입이 아닌, 추상 클래스 타입으로 선언하여 모듈 클래스를 느슨하게 결합 

  public DateLogger(BaseLogger baseLogger) {
    this.baseLogger = baseLogger;
  }

  protected String getLog(String msg) {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + baseLogger.getLog(msg);
  }
}

//모듈 2
public class ThreadLogger extends BaseLogger {
  protected BaseLogger baseLogger;

  public DateLogger(BaseLogger baseLogger) {
    this.baseLogger = baseLogger;
  }

  protected String getLog(String msg) {
    return Thread.currentThread().getName() + baseLogger.getLog(msg);
  }
}

public static void main(String[] args) {
    BaseLogger level1Logger = new BaseLoggerImpl();
    // 기능 추가 1
    BaseLogger level2Logger = new DateLogger(level1Logger);
    // 기능 추가 2
    BaseLogger level3Logger = new ThreadLogger(level2Logger);
    
    level3Logger.printLog("test log");
}
```

### use case
* java IO 패키지의 FileReader
```java
import java.io.LineNumberReader;

//base 클래스
public abstract class Reader implements Readable, Closeable {
  ...

  //업그레이드 대상 메서드
  abstract public int read(char cbuf[], int off, int len) throws IOException;

  abstract public void close() throws IOException;
  ...
}

//base 클래스 구현체
public class InputStreamReader extends Reader {
  ...

  public int read(char cbuf[], int offset, int length) throws IOException {
    return sd.read(cbuf, offset, length);
  }

  public void close() throws IOException {
    sd.close();
  }
  ...
}

// 모듈 클래스
public class BufferedReader extends Reader {
  private Reader in;

  public BufferedReader(Reader in, int sz) {
    super(in);
    ...
  }

  public int read(char cbuf[], int off, int len) throws IOException {
    synchronized (lock) {
      ...
      int n = read1(cbuf, off, len);
      ...
    }
  }

  private int read1(char[] cbuf, int off, int len) throws IOException {
    if (nextChar >= nChars) {
      if (len >= cb.length && markedChar <= UNMARKED && !skipLF) {
        return in.read(cbuf, off, len);
      }
      fill();
    }
    ...
  }
}

public static void main(String[] args) {
  Reader reader = new InputStreamReader(...);
  Reader bufferedReader = new BufferedReader(reader);
  Reader lineNumberReader = new LineNumberReader(bufferedReader);
}
```