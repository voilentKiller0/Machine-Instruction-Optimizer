import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Instruction_pattern {
    String operator;
    String operand1;
    String operand2;
    String operand2_dummy;
    String operand3;
    public String operand3_dummy;

    @Override
    public String toString() {
        return "Instruction_pattern{" +
                "operator='" + operator + '\'' +
                ", operand1='" + operand1 + '\'' +
                ", operand2='" + operand2 + '\'' +
                ", operand2_dummy='" + operand2_dummy + '\'' +
                ", operand3='" + operand3 + '\'' +
                ", operand3_dummy='" + operand3_dummy + '\'' +
                '}';
    }
}

public class Main {
    static ArrayList<Instruction_pattern> instruction_set;
    public static void main(String[] args) throws IOException{
        FileReader fr = new FileReader("./src/Testcase.asm");
        BufferedReader r = new BufferedReader(fr);
        ArrayList<String>asmcode = new ArrayList<>();
        String d;
        while((d = r.readLine()) != null)
        {
            //System.out.println(d);
            asmcode.add(d);
        }
        fr.close();
        Main obj = new Main();
        obj.Instruction_decode(asmcode);
        //instruction_set.stream().forEach(e -> System.out.println(e.toString()));
        obj.Data_dependency();
        obj.Control_dependency();
        obj.Structural_dependency();
    }

    public void Structural_dependency() {

        System.out.println("\nStructural Dependency:");
        for (int i = 0; i <= instruction_set.size() - 4; i++) {
            if(instruction_set.get(i).operator.equals("sw") || instruction_set.get(i).operator.equals("lw") ||
                    instruction_set.get(i).operator.equals("li") || instruction_set.get(i).operator.equals("lui") ||
                    instruction_set.get(i).operator.equals("mfhi") || instruction_set.get(i).operator.equals("mflo") ||
                    instruction_set.get(i).operator.equals("move"))
            {
                int tmp = i+3;
                System.out.println("I" + i + "->" + "I" + tmp);
            }
        }
    }

    public void Control_dependency() {

        int base_add = 100;
        System.out.println("\nControl Dependency:");
        for (int i = 0; i < instruction_set.size(); i++) {

            if(instruction_set.get(i).operator.startsWith("b"))
            {
                int l1 = Integer.parseInt(instruction_set.get(i).operand3);
                l1 = l1 - base_add;
                l1 = l1/4;
                if(l1 < i)
                {
                    while(l1 < instruction_set.size())
                    {
                        System.out.println("I" + i + "->" + "I" + l1);
                        l1++;
                    }
                }
                else if(l1 > i)
                {
                    int j = i+1;
                    while(j < l1 && j < instruction_set.size())
                    {
                        System.out.println("I" + i + "->" + "I" + j);
                        j++;
                    }
                }
            }

        }
    }
    public void Data_dependency() {
        System.out.println("Data Dependency:");
        for (int i = 0; i < instruction_set.size(); i++) {
            for (int j = i+1; j < instruction_set.size(); j++) {
                if((!instruction_set.get(i).operator.equals("sw") && !instruction_set.get(j).operator.equals("sw")) &&
                        (instruction_set.get(i).operand1.equals(instruction_set.get(j).operand2) ||
                                instruction_set.get(i).operand1.equals(instruction_set.get(j).operand3)||
                                instruction_set.get(i).operand1.equals(instruction_set.get(j).operand1) ||
                                instruction_set.get(i).operand2.equals(instruction_set.get(j).operand1) ||
                                instruction_set.get(i).operand3.equals(instruction_set.get(j).operand1)))
                {
                    System.out.println("I" + i + "->" + "I" + j);
                }
                else if((instruction_set.get(i).operator.equals("sw") && instruction_set.get(j).operator.equals("sw")) ||
                        instruction_set.get(i).operand1.equals(instruction_set.get(j).operand1))
                {
                    System.out.println("I" + i + "->" + "I" + j);
                }
                else if((instruction_set.get(i).operator.equals("sw") || instruction_set.get(j).operator.equals("sw")) &&
                        ((instruction_set.get(i).operand1.equalsIgnoreCase(instruction_set.get(j).operand1) ||
                                instruction_set.get(j).operand2.equalsIgnoreCase(instruction_set.get(i).operand1) ||
                                instruction_set.get(j).operand3.equalsIgnoreCase(instruction_set.get(i).operand1))))
                {
                    System.out.println("I" + i + "->" + "I" + j);
                }
            }
        }
    }
    public void Instruction_decode(ArrayList<String> asmcode)
    {
        instruction_set = new ArrayList<>();

        for (String s : asmcode) {
            Instruction_pattern inst = new Instruction_pattern();
            String[]tmp1;
            String[]tmp = s.split(" ");
            inst.operator = tmp[0];
            if (tmp[0].equals("j") || tmp[0].equals("jr") || tmp[0].equals("jal")) {
                inst.operand1 = tmp[1];
                inst.operand2 = "0";
                inst.operand2_dummy = "0";
                inst.operand3 = "0";
                inst.operand3_dummy = "0";
                instruction_set.add(inst);
                continue;
            }
            tmp = tmp[1].split(",");
            inst.operand1 = tmp[0];
            if (tmp.length >= 2) {
                if (tmp[1].contains("(")) {
                    tmp1 = tmp[1].split("\\(");
                    inst.operand2 = tmp1[1].substring(0, tmp1[1].length() - 1);
                    inst.operand2_dummy = tmp[1];
                } else {
                    inst.operand2 = tmp[1];
                }
            }
            if (tmp.length == 3) {
                if (tmp[1].contains("\\(")) {
                    tmp1 = tmp[2].split("\\(");
                    inst.operand3 = tmp1[2].substring(0, tmp1[1].length() - 1);
                    inst.operand3_dummy = tmp[2];
                } else {
                    inst.operand3 = tmp[2];
                }
            } else {
                inst.operand3 = "0";
            }
            if (inst.operator.equals("sw")) {
                String var = inst.operand1;
                inst.operand1 = inst.operand2_dummy;
                inst.operand2_dummy = var;
            }
            instruction_set.add(inst);
        }
    }
}