# Facade
* according to GOF - 복잡하게 얽혀있는것을 정리해서 높은 레벨의 인터페이스, 단순한 인터페이스를 외부에 제공하는 패턴
* In my opinion - 클래스의 덩치가 커져 api 가 많아졋을때, 함께 호출되는 api 들을 묶어 별도 caller 클래스로 분리함으로서 클래스와 호출부의 결합도와 복잡도를 낮춰 유지보수에 용이하게 만들어주는 패턴

### 장점
* 사용부 입장에서 기존에 호출해야했던 여러개의 api 에서, caller 클래스의 api 하나만 호출하면 되므로 사용하기 더 쉬워지고 버그 발생 확률도 줄어듬
* 사용부에서 기존에 호출하던 여러개의 api를, 동일한 기능을 하기 위해 caller 클래스의 api 하나만 호출하면 되므로, 결합이 느슨해지고 의존성이 약해져 유지보수에 용이해짐

### 예시

```java
class Amplifier {
  public void on() {
    System.out.println("Amplifier on");
  }

  public void setVolume(int level) {
    System.out.println("Setting volume to " + level);
  }
}

class DvdPlayer {
  public void on() {
    System.out.println("DVD Player on");
  }

  public void play(String movie) {
    System.out.println("Playing movie: " + movie);
  }
}

class Projector {
  public void on() {
    System.out.println("Projector on");
  }

  public void wideScreenMode() {
    System.out.println("Setting projector to wide screen mode");
  }
}

//AS-IS
public void watchMovie(String movie) {
    System.out.println("Get ready to watch a movie...");
    Projector projector = new Projector();
    projector.on();
    projector.wideScreenMode();
    
    Amplifier amp = new Amplifier();
    amp.on();
    amp.setVolume(5);
    
    DvdPlayer dvd = new DvdPlayer();
    dvd.on();
    dvd.play(movie);
}

//TO-BE
class HomeTheaterFacade {
  private Amplifier amp;
  private DvdPlayer dvd;
  private Projector projector;

  public HomeTheaterFacade(Amplifier amp, DvdPlayer dvd, Projector projector) {
    this.amp = amp;
    this.dvd = dvd;
    this.projector = projector;
  }

  public void watchMovie(String movie) {
    System.out.println("Get ready to watch a movie...");
    projector.on();
    projector.wideScreenMode();
    amp.on();
    amp.setVolume(5);
    dvd.on();
    dvd.play(movie);
  }
}

public void watchMovie(String movie) {
    HomeTheaterFacade homeTheater = new HomeTheaterFacade(new Amplifier(), new DvdPlayer(), new Projector());
    homeTheater.watchMovie("Inception");
}
```
