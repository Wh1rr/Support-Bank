package training.supportbank;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.text.WordUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    static ArrayList<Person> people = new ArrayList<Person>();
    static Boolean running = true;

    public static void main(String[] args) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader("Transactions2014.csv"));
        System.out.println("Test!");
        List<String[]> csv = reader.readAll();
        csv = csv.toArray();
        System.out.println(Arrays.toString(csv));
        for (int i = 0; i <= csv.size(); i++){
            //System.out.println(Arrays.toString(csv.get(i)));
            if (i % 10 == 2){
                for (i = 0; i <= people.size()-1; i++){
                    if (Arrays.toString(csv.get(i)).equals(people.get(i).name)){
                        break;
                    } else if (i == people.size()-1){
                        people.add(new Person(Arrays.toString(csv.get(i))));
                    }
                }
            }
        }
        while (running = true){
            System.out.println("Ready for request");
            Scanner input = new Scanner(System.in);
            String text = input.nextLine();
            text = text.toUpperCase();
            if (text.equals("LIST ALL")){
                listAll();
            } else if (text.startsWith("LIST")){
                text = text.replace("LIST", "");
                text = text.toLowerCase();
                text = WordUtils.capitalizeFully(text);
                list(text);
            }
        }
    }
    public static void listAll(){
        for (int i = 0; i <= people.size(); i++){
            System.out.println(people.get(i-1).getTotal());
        }
    }
    public static void list(String account) {
        for (int i = 0; i <= people.size(); i++) {
            if (Objects.equals(people.get(i-1).name, account)){
                people.get(i-1).allTransactions();
            }
        }
    }
}
