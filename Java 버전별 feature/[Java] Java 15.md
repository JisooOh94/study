# Java 15
```java
* Edwards-Curve Digital Signature Algorithm (EdDSA)
* Sealed Classes (Preview)
* Hidden Classes
* Remove the Nashorn JavaScript Engine
* Reimplement the Legacy DatagramSocket API
* Disable and Deprecate Biased Locking
* Pattern Matching for instanceof (Second Preview)
* ZGC: A Scalable Low-Latency Garbage Collector
* Text Blocks
* Shenandoah: A Low-Pause-Time Garbage Collector
* Remove the Solaris and SPARC Ports
* Foreign-Memory Access API (Second Incubator)
* Records (Second Preview)
* Deprecate RMI Activation for Removal
```

### Edwards-Curve Digital Signature Algorithm (EdDSA) [[JEP 339]](https://openjdk.java.net/jeps/339)
* 기존 암호화 알고리즘의 취약점과 성능을 개선한 Edwards-Curve 디지털 서명 알고리즘(EdDsa) 추가
```java
KeyFactory kf = KeyFactory.getInstance("EdDSA");
EdECPublicKeySpec pubSpec = new EdECPublicKeySpec(paramSpec, new EdPoint(xOdd, y));
PublicKey pubKey = kf.generatePublic(pubSpec);
```

### Hidden Classes [[ref]](https://www.tutorialspoint.com/java15/java15_hidden_classes.htm) [[ref]](https://www.baeldung.com/java-hidden-classes) [[JEP 371]](https://openjdk.java.net/jeps/371)
* 다른 클래스 바이트코드에서 직접 사용할 수 없는 클래스 (reflection 을 통해서도 불가능)
* 사용이 필요할때에만, runtime 중에 잠시 생성하여 사용 후 삭제
* 내부 구현을 완벽하게 은닉화 하기 위해 사용하는듯
```java
//define
Class<?> clazz = MyHiddenClass.class;
String className = clazz.getName();
String classAsPath = className.replace('.', '/') + ".class";
InputStream stream = clazz.getClassLoader().getResourceAsStream(classAsPath);
byte[] bytes = IOUtils.toByteArray();

Class<?> hiddenClass = lookup.defineHiddenClass(IOUtils.toByteArray(stream), true, ClassOption.NESTMATE).lookupClass();

//use
Object hiddenClassObject = hiddenClass.getConstructor().newInstance();
Method method = hiddenClassObject.getClass().getDeclaredMethod("myMethod", String.class);
Assertions.assertEquals("HELLO", method.invoke(hiddenClassObject, "expectedResult"));
```

### ZGC: A Scalable Low-Latency Garbage Collector [[ref]](http://cr.openjdk.java.net/~pliden/slides/ZGC-FOSDEM-2018.pdf) [[ref]](https://johngrib.github.io/wiki/java-gc-zgc/) [[JEP 377]](https://openjdk.java.net/jeps/377)
* 대용량 힙에 대한 GC 를 수행하면서 STW 시간을 최소화 하기 위한 차세대 GC
    * 최근 장비의 메모리 크기가 크게 증가하면서, 그만큼 가용한 Heap 크기도 늘어났고, 그에따라 큰 규모의 Heap 에 대해 고성능을 보장하는 GC 수요 증가
* G1GC 와 동일한 처리량을 보장하면서 STW 시간을 더욱 단축
    * marking 과정에서만 STW 로 처리, evacuation 은 concurrent로 수행
* STW 시간이 heap 크기, 또는 live 객체 크기에 상관없이 항상 균일

![image](https://user-images.githubusercontent.com/48702893/159253489-01410632-569d-4274-9f5e-bd590222821b.png)

![image](https://user-images.githubusercontent.com/48702893/159253521-cf0dfad8-a7d8-45ac-ac46-dbe4bfb4a484.png)

### Shenandoah: A Low-Pause-Time Garbage Collector [[ref]](https://velog.io/@recordsbeat/Low-Pause-Shenandoah-GC) [[ref]](https://wiki.openjdk.java.net/display/shenandoah/) [[JEP 379]](https://openjdk.java.net/jeps/379)
* ZGC 와 함께 차세대 GC 로서, ZGC 와 마찬가지로 STW 시간을 최소화 하기위한 GC
* 기존 GC 알고리즘들의 근간이었던 약한세대가설을 부정하고 단일 세대로만 동작함으로서 generational copy 비용 제거
    * generational copy : 세대 가설에 따라, 객체들을 나이에 해당하는 힙 공간으로 이주시키는 작업
  > "GC에 익숙한 사람이라면 모두 Generational GC가 최고라고 이야기합니다. 'generational hypothesis 는 대부분 상황에서 Young Object는 Old Object 보다 죽을 가능성이 클 것으로 내다본다.' 2008년에는 맞는 말이었죠. 하지만 지금은 설득력이 떨어집니다."
  Red Hat 개발자 Christine Flood - DevNation 2016 中
* G1GC 의 단점이었던 Compaction 과정의 오버헤드를 Concurrent Compaction 으로 감축
* 이를 통해 STW 시간을 최소화하면서, heap 크기에 상관없이 항상 균일한 성능 보장

![image](https://user-images.githubusercontent.com/48702893/159258729-0cfcaae6-6715-4d3b-946f-2fed0d7d368b.png)

![image](https://user-images.githubusercontent.com/48702893/159258166-e4052e2e-00b1-4f43-819f-33d2406c9731.png)

### textblock [[JEP 378]](https://openjdk.java.net/jeps/378)
* 기존에 여러 줄의 텍스트 사용시, 줄마다 + 개행 으로 연결해주던 방식을 “””을 통해 한번에 입력 가능
* AS-IS
```java
String html1 = "<html>\n" +
        "    <body>\n" +
        "        <p>Hello, world</p>\n" +
        "    </body>\n" +
        "</html>\n";
```
* TO-BE
```java
String html2 = """
          <html>
              <body>
                  <p>Hello, world</p>
              </body>
          </html>
          """;
```