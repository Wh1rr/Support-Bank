package training.supportbank;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.text.WordUtils;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    static ArrayList<Person> people = new ArrayList<>();
    static Boolean running = true;
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, CsvException {
        LOGGER.info("Program Launched");
        CSVReader reader = new CSVReader(new FileReader("Transactions2014.csv"));
        LOGGER.info("Opening Transactions2014.csv");
        CSVReader read2 = new CSVReader(new FileReader("DodgyTransactions2015.csv"));
        LOGGER.info("Opening DogdyTransactions2015.csv");
        List<String[]> dodgeCsv = read2.readAll();
        List<String[]> normCsv = reader.readAll();
        List<String[]> csv = Stream.concat(normCsv.stream(), dodgeCsv.stream()).collect(Collectors.toList());
        for (int i = 1; i <= csv.size()-1; i++) {
            String record = Arrays.toString(csv.get(i));
            String[] splitRecord = record.split(",", 5);
            String name = splitRecord[1].substring(1);
            if (name.equals("From")){
                LOGGER.warn("Header parsed!");
            } else if (people.size() == 0) {
                people.add(new Person(name));
            } else {
                for (int j = 0; j <= people.size(); j++) {
                    if (name.equals(people.get(j).name)) {
                        break;
                    } else if (j == people.size()-1) {
                        people.add(new Person(name));
                        break;
                    }
                }
            }
        }
        while (running = true){
            System.out.println("\nReady for request");
            Scanner input = new Scanner(System.in);
            String text = input.nextLine();
            text = text.toUpperCase();
            if (text.equals("LIST ALL")){
                listAll();
            } else if (text.startsWith("LIST")){
                text = text.replace("LIST", "");
                text = text.toLowerCase();
                text = WordUtils.capitalizeFully(text).substring(1);
                System.out.println(text);
                list(text);
            }
        }
    }
    public static void listAll(){
        for (int i = 0; i <= people.size()-1; i++){
            System.out.println(people.get(i).name + " - " + people.get(i).getTotal());
        }
    }
    public static void list(String account) {
        for (int i = 0; i <= people.size()-1; i++) {
            if (Objects.equals(people.get(i).name, account)){
                people.get(i).allTransactions();
            }
        }
    }
}
