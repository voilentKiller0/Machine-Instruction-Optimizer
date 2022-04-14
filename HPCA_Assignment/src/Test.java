import java.io.*;
import java.util.*;

public class Test {
    ArrayList<String> token (String line){
        String[]temp = line.split(" "); //operand
        if (temp.length == 1){
            ArrayList list = new ArrayList<String>(Arrays.asList(temp));
            return list;
        }
        String[]temp1 = temp[1].split(","); // operator and addresses
        for (int i = 0 ; i < temp1.length ; i++) {
            temp1[i] = temp1[i].replaceAll("[\\\\[\\\\](){}]","");
            temp1[1] = temp1[1].replaceFirst("^0+(?!$)", "");
        }
        ArrayList list = new ArrayList<String>(Arrays.asList(temp1));
        list.add(0, temp[0]);
        return list;
    }

    ArrayList<String> impToken (String line){
        String[]temp = line.split(" "); //operand
        String[]temp1 = temp[1].split(","); // operator and addresses
        ArrayList list = new ArrayList<String>(Arrays.asList(temp1));
        list.add(0, temp[0]);
        return list;
    }
    public static void main(String[]args) throws IOException{
        FileReader fr = new FileReader("src/Testcase.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        Test obj = new Test();
        while ((line = br.readLine()) != null){
            System.out.print(line+" : ");
            ArrayList<String> arrayList = obj.token(line);
            arrayList.stream().forEach(e -> System.out.print(e+" "));
            System.out.println();
        }
        fr.close();
    }
}