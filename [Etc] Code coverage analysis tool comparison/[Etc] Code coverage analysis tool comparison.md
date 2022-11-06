# Jacoco vs OpenClover
### Coverage Metrics
* jacoco : Method, linear block, Line, Branch, Instruction, Global coverage, Per-test coverage
* openClover : Method, Statement, linear block, Branch, Global coverage, Per-test coverage

> cf) Instruction coverage provides information about the amount of byte code that has been executed or missed.

> cf) Statement and line metrics are roughly similar in terms of their granularity (i.e. code has roughly one statement per line). Statement coverage has huge advantage over line coverage in case when language uses many short statements in a single line (a good example is Java8 stream with several map() and filter() calls) - it's more precise as it can detect partially covered lines.

### Report type
* jacoco :  HTML, XML, CSV
* openClover : HTML, PDF, XML, JSON, TEXT

### Supported languages
* jacoco : java, groovy
* openClover : java, groovy, aspectj

### Supported JDK
* jacoco : jdk 5 ~ 8
* openClover : jdk 6 ~ 8

### Integration
* jacoco : jenkins, sonar
* openClover : jenkins, sonar, jira

<img width="362" alt="image" src="https://user-images.githubusercontent.com/48702893/200164064-18350016-6b30-43bd-af8d-ce906960e638.png">

### Advantages
* jacoco
  * Very easy to integrate thanks to the on-the-fly byte code instrumentation. You can measure coverage without having the source code
  * Lots of references
* openClover
  * 

### Disadvantages
* jacoco
  * Classes must be compiled with debug option.
* openClover
  * Due to a fact that Clover is based on source code instrumentation, integration requires a build - it's necessary to recompile code with Clover.
    
> Reference
> * https://openclover.org/doc/manual/4.2.0/general--comparison-of-code-coverage-tools.html#Comparisonofcodecoveragetools-legend
> * https://www.eclemma.org/jacoco/trunk/doc/counters.html
> * https://www.baeldung.com/jacoco

<br>

# Demo
### Sample Code
```java
public class SampleClass {
  public void sampleMethod(String param, List<Integer> list) {
    if("First branch".equals(param)) {
      System.out.println("Entered first branch");
      list.stream().filter(val -> val > 5).map(String::valueOf).forEach(System.out::print);
    } else if("Second branch".equals(param)) {
      System.out.println("Entered second branch");
    } else {
      throw new InvalidParameterException();
    }
  }
}
```

### Test Code
```java
@RunWith(MockitoJUnitRunner.class)
public class SampleClassTest {
  private SampleClass sampleClass = new SampleClass();

  @Test
  public void sampleMethod() {
    sampleClass.sampleMethod("First branch", Arrays.asList(1, 2, 3, 4));
  }
}
```

### Jacoco
<img width="950" alt="image" src="https://user-images.githubusercontent.com/48702893/200164040-8c87dd18-69ca-4cc9-9887-caae468f987e.png">

<img width="950" alt="image" src="https://user-images.githubusercontent.com/48702893/200164085-5cdcdfd1-b8db-420a-b1b1-7146926622d9.png">

<br>

### OpenClover
<img width="1725" alt="image" src="https://user-images.githubusercontent.com/48702893/200165055-f8965073-fe72-48a8-85a2-c0fefbdbaa5f.png">

<img width="1725" alt="image" src="https://user-images.githubusercontent.com/48702893/200165071-bae2dd1d-5cca-4bc6-be1b-23d242ac0584.png">

<br>

***
> Reference
> * https://djcho.github.io/springboot/spring-boot-chapter7-5/
> * https://programmerclick.com/article/7238828355/