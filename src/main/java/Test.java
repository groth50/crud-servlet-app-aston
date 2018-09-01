import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {
    public static void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 10; i ++) {
            list.add (i);
            System.out.println(list.get(i - 1));
        }
    }

    public static void addList1(List<? super Number> list, ExtInt num) {
        list.add(num);
        list.add(new Integer("0"));
        //Number n = list.get(1);
        //list.add(n);

    }

    public static void addList2(List<? extends Number> list, ExtInt num) {
        Number n = list.get(1);

    }

//    public static void main(String[] args) {
//
//        addNumbers(new ArrayList<Object>());
//
//
//        new HashMap<String, String>().entrySet();
//    }

    static abstract class ExtInt extends Number {
    }
}
