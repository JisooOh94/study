package Equals_재정의규칙;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class symmetryTest {
    public static class CaseIgnoreString {
        String str;

        public CaseIgnoreString(String str) { this.str = str; }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof CaseIgnoreString) {
                return str.equalsIgnoreCase((((CaseIgnoreString) obj).str));
            }
            if(obj instanceof String) {
                return str.equalsIgnoreCase((String)obj);
            }
            return false;
        }
    }

    public static void main(String[] args) {
        CaseIgnoreString caseIgnoreString = new CaseIgnoreString ("A");
        String normalStr = "A";
        //Assert.assertEquals(caseIgnoreString.equals(normalStr), normalStr.equals(caseIgnoreString )); //false

        List<CaseIgnoreString> list = new ArrayList<>();
        list.add(caseIgnoreString);
        System.out.println(list.contains(normalStr));

    }
}
