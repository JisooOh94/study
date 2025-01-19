# Proxy
* 주체 클래스의 일부 기능을 대신 수행하는 프록시 클래스를 두어 주체클래스의 기능을 확장
  * 주체 클래스 기능 호출 전후에 별도의 보조 로직 추가
* 프록시 클래스는 주체 클래스의 인터페이스를 구현하여, 사용부에서 주체클래스 필드에 대신 주입되어 사용됨
    * 클라이언트는 기능이 프록시 객체에서 수행된건지, 주체 클래스 객체에서 수행된건지 관심을 가지지 않음
* 프록시 클래스는 주체 클래스 필드를 가지어, 대신 수행할 수 없는 기능 호출시, 주체 클래스에게 수행 위임

### Use-case
* 리소스 관리
    * 주체 클래스 객체 생성 비용이 클 경우, 프록시 클래스에서 대신 수행할 수 있는 기능을 수행하고, 주체 클래스에서만 수행할 수 있는 기능이 호출된 시점에 주체 클래스 객체를 생성하여 수행 위임
    * 주체 클래스 객체 lazy-initailizing 을 통해 어플리케이션 구동 비용 절감
* 원격 프록시
    * 분산 컴퓨팅 환경에서 타 장비에 로드되어있는 객체의 api 를 원격으로 호출해주는 프록시 클래스 (e.g. Java RMI)
* 접근제어
    * 실제 객체에 대한 접근을 제어(e.g. 접근 권한 확인)

### 예시

```java
class RealImage implements Image {
  private String filename;

  public RealImage(String filename) {
    this.filename = filename;
    loadFromDisk();
  }

  private void loadFromDisk() {
    System.out.println("Loading " + filename);
  }

  public void display() {
    System.out.println("Displaying " + filename);
  }
}

class ProxyImage implements Image {
  private RealImage realImage;
  private String filename;

  public ProxyImage(String filename) {
    this.filename = filename;
  }

  public void display() {
    if (realImage == null) {
      realImage = new RealImage(filename);
    }
    realImage.display();
  }
}


public static void main(String[] args) {
    Image image = new ProxyImage("test_image.jpg");
    ...
    image.display();  // 실제 이미지 로딩은 display() 호출 시에만 발생
}
```
