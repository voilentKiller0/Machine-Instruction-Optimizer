import java.io.*;
import java.util.*;

class Node{
    int num;
    Node left;
    ArrayList<Integer> right;
    Node(int num){
        this.num = num;
        left = null;
    }
}
class Pair<F, S> {
    private F first; //first member of pair
    private S second; //second member of pair

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}

class Dependency{
    void data_dependency(Node root, ArrayList<Integer> arrayList, Map<Integer, ArrayList<Integer>> dataDependency, int cond){
        if (root == null) {
            return;
        }else {
            arrayList.add(root.num);
            ArrayList<Integer> chk = new ArrayList<>();
            if (root.right != null)
                chk = root.right;
            for (int i = 0 ; i < chk.size() ; i++){
                for (int j = 0 ; j < arrayList.size() ; j++){
                    if (cond == 1){
                        System.out.println("Instruction "+chk.get(i)+" is data-dependent on "+arrayList.get(j));
                    }
                    ArrayList<Integer> temp = dataDependency.get(arrayList.get(j));
                    if (temp == null)
                        temp = new ArrayList<>();
                    temp.add(chk.get(i));
                    dataDependency.put(arrayList.get(j), temp);
                }
            }
            data_dependency(root.left, arrayList, dataDependency, cond & 1);
        }
    }
}


class Scheduling{
    ArrayList<String> token (String line){
        String[]temp = line.split(" "); //operand
        if (temp.length == 1){
            return new ArrayList<>(Arrays.asList(temp));
        }
        String[]temp1 = temp[1].split(","); // operator and addresses
        for (int i = 0 ; i < temp1.length ; i++) {
            temp1[i] = temp1[i].replaceAll("[\\\\[\\\\](){}]","");
        }
        if (temp1.length > 1)
            temp1[1] = temp1[1].replaceFirst("^0+(?!$)", "");
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
    String unschedule(Map<String, ArrayList<Pair<String, Integer>>> stall, Map<Integer, ArrayList<Integer>> dataDependency, String path) throws IOException {

        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String line;
        File file = new File("Unscheduled.txt");
        file.createNewFile();
        FileOutputStream oFile = new FileOutputStream(file, false);
        line = br.readLine();
        oFile.write((line+"\n").getBytes());
        int prev_ins = 1;
        line = line.toUpperCase();
        String[]temp = line.split(" "); //operand
        String[]temp1 = temp[1].split(","); // operator and addresses
        for (int i = 0 ; i < temp1.length ; i++) temp1[i] = temp1[i].replaceAll("\\p{P}","");
        String prev = temp[0];
        int curr_ins = 0;
        String curr;
        while ((line = br.readLine()) != null){
            curr_ins = prev_ins+1;
            line = line.toUpperCase();
            ArrayList<String> g = token(line);
            curr = g.get(0);
            int dep = 0;
            ArrayList<Integer> test = new ArrayList<>();
            if (dataDependency.containsKey(prev_ins)){
                test = dataDependency.get(prev_ins);
                for (int i = 0 ; i < test.size() ; i++){
                    if (test.get(i) == curr_ins){
                        dep = 1;
                        break;
                    }
                }
            }
            if (dep == 1){
                int n = 0;
                if (stall.containsKey(prev)){
                    ArrayList<Pair<String, Integer>> src = new ArrayList<>();
                    src = stall.get(prev);
                    for (int i = 0 ; i < src.size() ; i++){
                        if (Objects.equals(src.get(i).getFirst(), curr)){
                            n = src.get(i).getSecond();
                            break;
                        }
                    }
                }
                while (n-- >= 0){
                    oFile.write("stall\n".getBytes());
                }
            }
            oFile.write((line+"\n").getBytes());
            prev = curr;
            prev_ins = curr_ins;
        }
        fr.close();
        oFile.close();
        return "Unscheduled.txt";
    }
    String unscheduling_unroll(String path) throws IOException {
        ArrayList<String> ins = new ArrayList<>();
        ArrayList<String> oth = new ArrayList<>();
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String line;
        Set<String> set = new HashSet<>();
        int chk = 0;
        while ((line = br.readLine()) != null){
            if (line.isEmpty()){
                continue;
            }
            line = line.toUpperCase();
            ArrayList<String> ans;
            ans = token(line);
            if (Objects.equals(ans.get(0), "ADD.I") || Objects.equals(ans.get(0), "BNE") || chk == 1){
                oth.add(line);
                chk = 1;
            }else {
                ins.add(line);
            }
            for (int i = 0 ; i < ans.size() ; i++){
                if (ans.get(i).charAt(0) == 'F'){
                    set.add(ans.get(i));
                }
            }
        }
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 33 ; i = i+2){
            arrayList.add("F"+String.valueOf(i));
        }
        int n = 4;
        int curr = 0;
        ArrayList<String> v = new ArrayList<>();
        v.addAll(set);
        Collections.sort(v);
        File file = new File("Unrolled_Unscheduled.txt");
        file.createNewFile();
        FileOutputStream oFile = new FileOutputStream(file, false);
        int register = 8;
        while (n-- > 0){
            register -= 8;
            Map<String , String> map = new HashMap<>();
            for (int i = 0 ; i < v.size() ; i++){
                map.put(v.get(i), arrayList.get(curr++));
            }
            for (int i = 0; i < ins.size() ; i++){
                if (Objects.equals(ins.get(i), "STALL")){
                    oFile.write("stall\n".getBytes());
                    continue;
                }
                ArrayList<String> ans = impToken(ins.get(i));
                for (int j = 0 ; j < ans.size() ; j++){
                    if (ans.get(j).charAt(0) == 'F'){
                        ans.set(j,map.get(ans.get(j)));
                    }
                    if (ans.get(j).contains("(")){
                        String str = ans.get(j).substring(ans.get(j).indexOf('('));
                        ans.set(j, String.valueOf(register)+str);
                    }
                }
                String res = "";
//                res = ans.get(0)+" ";
//                res = res + ans.get(1);
                for (int j = 0 ; j < ans.size() ; j++){
                    if (j != 0)
                        res = res + ", ";
                    res = res + ans.get(j);
                }
                oFile.write((res+"\n").getBytes());
            }
        }
        register -= 8;
        for (int i = 0 ; i < oth.size() ; i++){
            if (Objects.equals(oth.get(i), "STALL")){
                oFile.write("stall\n".getBytes());
                continue;
            }
            ArrayList<String> ans = impToken(oth.get(i));
            for (int j = 0 ; j < ans.size() ; j++){
                if (ans.get(j).contains("#")){
                    ans.set(j, "#"+register);
                }
            }
            String res = "";
            //res = ans.get(0) + " ";
            //res = res + ans.get(1);
            for (int j = 0 ; j < ans.size() ; j++){
                if (j != 0)
                    res = res + ", ";
                res = res + ans.get(j);
            }
            oFile.write((res+"\n").getBytes());
        }
        fr.close();
        oFile.close();
        return "Unrolled_Unscheduled.txt";
    }
    void temp_stall (ArrayList<String> res, Map<String, ArrayList<Pair<String, Integer>>> stall, Map<Integer, ArrayList<Integer>> dd, String path) throws IOException {
        String line;
        File file = new File(path);
        file.createNewFile();
        FileOutputStream oFile = new FileOutputStream(file, false);
        line = res.get(0);
        oFile.write(line.getBytes());
        int prev_ins = 1;
        line = line.toUpperCase();
        ArrayList<String> ans = token(line);
        String prev = ans.get(0);
        int curr_ins = 0;
        String curr;
        for (int i = 0  ; i < res.size() ; i++){
            line = res.get(i);
            curr_ins = prev_ins+1;
            line = line.toUpperCase();
            ArrayList<String> g = token(line);
            curr = g.get(0);
            int dep = 0;
            ArrayList<Integer> test = new ArrayList<>();
            if (dd.containsKey(prev_ins)){
                test = dd.get(prev_ins);
                for (i = 0 ; i < test.size() ; i++){
                    if (test.get(i) == curr_ins){
                        dep = 1;
                        break;
                    }
                }
            }
            if (dep == 1){
                int n = 0;
                if (stall.containsKey(prev)){
                    ArrayList<Pair<String, Integer>> src;
                    src = stall.get(prev);
                    for (i = 0 ; i < src.size() ; i++){
                        if (src.get(i).getFirst() == curr){
                            n = src.get(i).getSecond();
                            break;
                        }
                    }
                }
                while (n-- > 0){
                    oFile.write("stall\n".getBytes());
                }
            }
            oFile.write((line+"\n").getBytes());
            prev = curr;
            prev_ins = curr_ins;
        }
    }
    String scheduled (String path, String savePath, Map<String, ArrayList<Pair<String, Integer>>> stall, String path1) throws IOException {
        FileReader fr = new FileReader(path1);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int count = 0;
        Map<Integer, ArrayList<String>> map = new HashMap<>();
        while ((line = br.readLine()) != null){
            if (line.isEmpty())
                continue;
            line = line.toUpperCase();
            ArrayList<String> ans = token(line);
            if (!Objects.equals(ans.get(0), "ADD.I") && !Objects.equals(ans.get(0), "BNE")){
                count++;
            }
        }
        fr.close();
        fr = new FileReader(path);
        br = new BufferedReader(fr);
        int i = 0;
        ArrayList<String> oth = new ArrayList<>();
        while ((line = br.readLine()) != null){
            line = line.toUpperCase();
            ArrayList<String> ans = token(line);
            if (Objects.equals(ans.get(0), "STALL") || Objects.equals(ans.get(0), "")){
                continue;
            } else if (ans.get(0).contains("ADD.I") || Objects.equals(ans.get(0), "BNE")) {
                oth.add(line);
            } else {
                ArrayList<String> temp = new ArrayList<>();
                if (map.get(i%count) != null)
                    temp.addAll(map.get((i%count)));
                temp.add(line);
                map.put(i%count, temp);
                i++;
            }
        }
        ArrayList<String> res = new ArrayList<>();
        for(int j=0 ; j<count ; j++){
            if (map.get(j) == null)
                continue;
            for(i = 0 ; i < map.get(j).size() ; i++){
                res.add(map.get(j).get(i));
            }
        }
        for (i = 0 ; i < oth.size() ; i++){
            if (oth.get(i).contains("ADD.I")){
                for (int j = res.size() - 1 ; j >= 0 ; j--){
                    if (res.get(j).contains("ADD.I")){
                        res.set(j + 1, oth.get(i));
                        break;
                    }
                }
            }else {
                res.add(oth.get(i));
            }
        }
        int l = res.size()-1;
        int tsum = 0;
        String s = "0";
        if (l >= 0 && ! res.get(l).contains("LOOP"))
            s = res.get(l).substring(res.get(l).indexOf('#')+1);
        try {
            tsum = Integer.parseInt(s);
        }catch (Exception e){
            tsum = 0;
        }

        for (int h = l+1 ; h < res.size() ; h++){
            if (res.get(h).contains("L.") || res.get(h).contains("S.")){
                ArrayList<String> ans = impToken(res.get(h));
                for (int j = 0 ; j < ans.size() ; j++){
                    if (ans.get(j).contains("(")){
                        String ret = ans.get(j).substring(0, ans.get(j).indexOf('('));
                        int num = Integer.parseInt(ret);
                        num = num-tsum;
                        ans.set(j, String.valueOf(num)+ans.get(j).substring(ans.get(j).indexOf('(')));
                    }
                }
                String conv = "";
                conv = ans.get(0)+" ";
                conv = conv+ans.get(l);
                for (int j = 2 ; j < ans.size() ; j++){
                    conv = conv + ", ";
                    conv = conv + ans.get(i);
                }
                res.set(h, conv);
            }
        }
        Map<String, Pair<Node, Node>> vp = new HashMap<>();
        ArrayList<Pair<String, ArrayList<String>>> store = new ArrayList<>();
        for (i = 0 ;i < res.size() ; i++){
            String line1 = res.get(i);
            line1 = line1.toUpperCase();
            ArrayList<String> ans = token(line1);
            if(Objects.equals(ans.get(0), "L.D") || Objects.equals(ans.get(0), "L.S") || Objects.equals(ans.get(0), "ADD.I") || Objects.equals(ans.get(0), "SUB.I")){
                store.add(new Pair<>(ans.get(1), new ArrayList<>(Arrays.asList(ans.get(2)))));
            }
            else if(Objects.equals(ans.get(0), "ADD.D") || Objects.equals(ans.get(0), "ADD.S") || Objects.equals(ans.get(0), "ADD.U") || Objects.equals(ans.get(0), "SUB.U")){
                store.add(new Pair<>(ans.get(1), new ArrayList<>(Arrays.asList(ans.get(2), ans.get(3)))));
            }
            else if(Objects.equals(ans.get(0), "S.D") || Objects.equals(ans.get(0), "S.S") || Objects.equals(ans.get(0), "BNE")){
                store.add(new Pair<>("", new ArrayList<>(Arrays.asList(ans.get(1), ans.get(1)))));
            }
            else if(Objects.equals(ans.get(0), "BEQZ")){
                store.add(new Pair<>("", new ArrayList<>(Arrays.asList(ans.get(1)))));
            }
            else if(Objects.equals(ans.get(0), "LW")){
                store.add(new Pair<>(ans.get(1), new ArrayList<>(Arrays.asList(""))));
            }
        }
        for (i = 0 ; i < store.size() ; i++){
            String out = store.get(i).getFirst();
            ArrayList<String> v = store.get(i).getSecond();
            for (int  j = 0 ; j < v.size() ; j++){
                if (vp.containsKey(v.get(j))){
                    Node curr = vp.get(v.get(j)).getSecond();
                    if (curr.right == null)
                        curr.right = new ArrayList<>();
                    curr.right.add((i+1));
                }
            }
            if (!Objects.equals(out, "")){
                if (! vp.containsKey(out)){
                    Node temp = new Node(i+1);
                    vp.put(out, new Pair<>(temp, temp));
                }else {
                    Node temp = new Node(i+1);
                    Node curr = vp.get(out).getSecond();
                    curr.left = temp;
                    curr = curr.left;
                    vp.get(out).setSecond(curr);
                }
            }
        }
        Map<Integer, ArrayList<Integer>> dd = new HashMap<>();
        Dependency dependency = new Dependency();
        for (Map.Entry<String, Pair<Node, Node>> entry : vp.entrySet()){
            ArrayList<Integer>  arrayList = new ArrayList<>();
            dependency.data_dependency(entry.getValue().getFirst(), arrayList, dd, 0);
        }
        if (res.size() > 0)
            temp_stall(res, stall, dd, savePath);
        return path;
    }
}

public class HPCA {
    public static void main(String[] args) throws IOException {
        String path;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Assembly code file path : ");
        path = sc.nextLine();
        FileReader fr = new FileReader(path);
        BufferedReader r = new BufferedReader(fr);
        ArrayList<String>asmcode = new ArrayList<>();
        String d;

        Map<Integer, String> instruction = new HashMap<>();
        ArrayList<Pair<String, ArrayList<String>>> value_store = new ArrayList<>();
        Map<String, Pair<Node, Node>> mp = new HashMap<>();
        int k = 1;
        Scheduling scheduling = new Scheduling();
        while((d = r.readLine()) != null)
        {
            instruction.put(k++,d);
            d = d.toUpperCase();
            ArrayList<String> temp = scheduling.token(d);

            if (Objects.equals(temp.get(0), "L.D") || Objects.equals(temp.get(0), "L.S") || Objects.equals(temp.get(0), "ADD.I") || Objects.equals(temp.get(0), "SUB.I")){
                value_store.add(new Pair<>(temp.get(1), new ArrayList<>(Arrays.asList(temp.get(2)))));
            } else if(Objects.equals(temp.get(0), "ADD.D") || Objects.equals(temp.get(0), "ADD.S") || Objects.equals(temp.get(0), "ADD.U") || Objects.equals(temp.get(0), "SUB.U")){
                value_store.add(new Pair<>(temp.get(1), new ArrayList<>(Arrays.asList(temp.get(2), temp.get(3)))));
            } else if (Objects.equals(temp.get(0), "S.D") || Objects.equals(temp.get(0), "S.S") || Objects.equals(temp.get(0), "BNE")) {
                value_store.add(new Pair<>("", new ArrayList<>(Arrays.asList(temp.get(1), temp.get(2)))));
            } else if (Objects.equals(temp.get(0), "BEQZ")) {
                value_store.add(new Pair<>("", new ArrayList<>(Arrays.asList(temp.get(1)))));
            } else if (Objects.equals(temp.get(0), "LW")) {
                value_store.add(new Pair<>(temp.get(1), null));
            }
        }
        fr.close();
        for (int i = 0 ; i < value_store.size() ; i++){
            String out = value_store.get(i).getFirst();
            ArrayList<String> arrayList = value_store.get(i).getSecond();
            for (int j=0 ; j<arrayList.size() ; j++){
                if (mp.containsKey(arrayList.get(j))){
                    Node curr = mp.get(arrayList.get(j)).getSecond();
                    if (curr.right == null)
                        curr.right = new ArrayList<>();
                    curr.right.add(i+1);
                }
            }
            if (!Objects.equals(out, "")){
                if (! mp.containsKey(out)){
                    Node temp = new Node(i+1);
                    mp.put(out, new Pair<>(temp, temp));
                }else {
                    Node temp = new Node(i+1);
                    Node curr = mp.get(out).getSecond();
                    curr.left = temp;
                    curr = curr .left;
                    mp.put(out, new Pair<>(mp.get(out).getFirst(), curr));
                }
            }
        }
        System.out.println("--------------------------------------------------------");
        System.out.println("                    Instruction");
        System.out.println("--------------------------------------------------------");
        for (Map.Entry entry : instruction.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("--------------------------------------------------------");
        System.out.println("                    Data Dependency");
        System.out.println("--------------------------------------------------------");

        Map<Integer, ArrayList<Integer>> dataDependency = new HashMap<>();

        Dependency dependency = new Dependency();


        for (Map.Entry<String, Pair<Node, Node>> entry : mp.entrySet()){
            ArrayList<Integer> arrayList = new ArrayList<>();
            Pair<Node, Node> pair = entry.getValue();
            dependency.data_dependency(pair.getFirst() ,arrayList, dataDependency, 1);
        }

        Map<String, ArrayList<Pair<String, Integer>>> stall = new HashMap<>();

        ArrayList<Pair<String, Integer>> pairArrayList = new ArrayList<>();
        pairArrayList.add(new Pair<>("ADD.D", 3));
        stall.put("ADD.D", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("S.D", 2)));
        stall.put("ADD.D", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("L.D", 1)));
        stall.put("ADD.D", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("ADD.I", 3)));
        stall.put("ADD.D", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("BNE", 1)));
        stall.put("ADD.D", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("S.D", 4)));
        stall.put("L.D", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("ADD.D", 1)));
        stall.put("L.D", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("ADD.I", 1)));
        stall.put("L.D", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("BNE", 1)));
        stall.put("ADD.I", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("ADD.D", 3)));
        stall.put("ADD.I", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("ADD.I", 3)));
        stall.put("ADD.I", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("S.D", 2)));
        stall.put("ADD.I", pairArrayList);

        pairArrayList.clear();
        pairArrayList.add((new Pair<>("L.D", 1)));
        stall.put("ADD.I", pairArrayList);

        String path1 = scheduling.unschedule(stall, dataDependency, path);
        String path2 = scheduling.unscheduling_unroll(path1);
        String path3 = scheduling.scheduled(path1, "Scheduled.txt", stall, path);
        String path4 = scheduling.scheduled(path2, "unrolled_scheduled.txt", stall, path);


        System.out.println("\n\n\nFour output text files have been generated using");
        System.out.println("(i) Unscheduled strategy  ---> \t\t\t\t\t    File name : Unscheduled.txt");
        System.out.println("(ii) Scheduled strategy --->  \t\t\t\t\t\tFile name : Scheduled.txt");
        System.out.println("(iii) Loop Unrolling and Unscheduled strategy --->  File name : Unrolled_Unscheduled.txt");
        System.out.println("(iv) Loop Unrolling and Scheduled strategy  --->    File name : unrolled_scheduled.txt");
    }
}
