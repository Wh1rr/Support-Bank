package training.supportbank;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Person {
    String name;
    BigDecimal total= BigDecimal.ZERO;
    ArrayList<Transaction> transactions = new ArrayList<>();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);

    public Person(String name, Logger LOGGER) throws IOException, CsvException {
        System.out.println("\nCreating person..."+ name);
        this.name = name;
        CSVReader reader = new CSVReader(new FileReader("Transactions2014.csv"));
        CSVReader read2 = new CSVReader(new FileReader("DodgyTransactions2015.csv"));
        List<String[]> dodgeCsv = read2.readAll();
        List<String[]> csv = reader.readAll();
        for (int j =1; j <= dodgeCsv.size()-1; j++){
            csv.add(dodgeCsv.get(j));
        }
        for (int i = 1; i <= csv.size()-1; i++) {
            try {
                String record = Arrays.toString(csv.get(i));
                String[] splitRecord = record.split(",", 5);
                LocalDate date = LocalDate.parse(splitRecord[0].substring(1), format);
                String from = splitRecord[1].substring(1);
                String to = splitRecord[2].substring(1);
                String reason = splitRecord[3].substring(1);
                String amount = splitRecord[4].substring(1).substring(0, splitRecord[4].length() - 2);
                BigDecimal tAmount = new BigDecimal(amount);
                if (from.equals(this.name)) {
                    transactions.add(new Transaction(tAmount, reason, date, to));
                    this.total = this.total.add(tAmount);
                } else if (to.equals(this.name)) {
                    transactions.add(new Transaction(tAmount, reason, date, from));
                    this.total = this.total.subtract(tAmount);
                }
            } catch(Exception e){
                LOGGER.error("Creating transaction - " + e);
                transactions.remove(transactions.size()-1);
            }
        }
        System.out.println("\n" + this.name + " - Person created");
    }

    public void allTransactions(){
        this.transactions.stream().map(Transaction::getAll).forEach(System.out::println);
    }
    public BigDecimal getTotal(){return this.total;}
}
