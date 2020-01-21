import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        //System.out.println(list.contains(caseIgnoreString));
        System.out.println(list.contains(normalStr));


        Timestamp timestamp = new Timestamp(12345);
        Date date = new Date(12345);

        System.out.println(timestamp.equals(date));
        System.out.println(date.equals(timestamp));
    }
}
