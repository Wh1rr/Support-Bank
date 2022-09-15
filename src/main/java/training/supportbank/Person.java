package training.supportbank;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Person {
    String name;
    Float total;
    ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);

    public Person(String name) throws IOException, CsvException {
        this.name = name;
        CSVReader reader = new CSVReader(new FileReader("Transactions2014.csv"));
        List<String[]> csv = reader.readAll();
        for (int i = 0; i <= csv.size(); i++) {
            if (i % 10 == 2) {
                if (!Objects.equals(Arrays.toString(csv.get(i - 1)), this.name)) {
                    transactions.add(new Transaction(new BigDecimal(Arrays.toString(csv.get(i+3))),
                            Arrays.toString(csv.get(i+2)), LocalDate.parse(Arrays.toString(csv.get(i-1)), this.format)));
                }
            } else if (i % 10 == 3) {
                BigDecimal negative = new BigDecimal(Arrays.toString(csv.get(i+3)));
                negative = negative.negate();
                if (!Objects.equals(Arrays.toString(csv.get(i - 1)), this.name)) {
                    transactions.add(new Transaction(negative, Arrays.toString(csv.get(i+2)),
                            LocalDate.parse(Arrays.toString(csv.get(i-1)), this.format)));
                }
            }
        }
    }

    public void allTransactions(){
        transactions.forEach(System.out::println);
    }
    public Float getTotal(){return this.total;}
}
