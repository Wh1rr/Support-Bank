package training.supportbank;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.text.WordUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    static ArrayList<Person> people = new ArrayList<>();
    static Boolean running = true;
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
    static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    static Boolean openCheck = false;
    public static void main(String[] args) {
        try {
            LOGGER.info("Program Launched");
            CsvParser.readFile("", openCheck, people);
            JsonParser.readFile("", openCheck, people);
            openCheck = true;
        } catch (Exception e) {
            LOGGER.error("Processing data - " + e + "- on line number " + e.getStackTrace()[1].getLineNumber());
        }
        try {
            while (running = true) {
                printBanner();
                showPrompt();
                Scanner input = new Scanner(System.in);
                String text = input.nextLine();
                text = text.toUpperCase();
                if (text.equals("LIST ALL")) {
                    listAll();
                } else if (text.startsWith("LIST")) {
                    text = text.replace("LIST", "");
                    text = text.toLowerCase();
                    text = WordUtils.capitalizeFully(text).substring(1);
                    System.out.println(text);
                    list(text);
                } else if (text.startsWith("IMPORT")) {
                    text = text.replace("IMPORT", "");
                    text = text.toLowerCase();
                    text = WordUtils.capitalize(text);
                    fileImport(text);
                }
            }
        }catch(Exception e){
                LOGGER.error("Processing data during request - " + e + "- on line number " + e.getStackTrace()[3].getLineNumber());
            }
        }
        public static void listAll() {
            for (int i = 0; i <= people.size() - 1; i++) {
                System.out.println(people.get(i).name + " - " + people.get(i).getTotal());
            }
        }
        public static void list(String account){
            for (int i = 0; i <= people.size() - 1; i++) {
                if (Objects.equals(people.get(i).name, account)) {
                    people.get(i).allTransactions();
                }
            }
        }

        private static void fileImport(String filename) throws IOException, CsvException {
            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            if (extension.equals("json")) {
                JsonParser.readFile(filename, openCheck, people);
            } else if (extension.equals("csv")) {
                CsvParser.readFile(filename, openCheck, people);
            } else {
                System.out.println("File type not supported");
            }
        }
        private static void printBanner() {
            System.out.println("\nWelcome to SupportBank!");
            System.out.println("=======================");
            System.out.println();
            System.out.println("Available commands:");
            System.out.println("  List All - list all account balances");
            System.out.println("  List [Account] - list transactions for the specified account");
            System.out.println("  Import File [Filename] - import transactions from the specified file");
            System.out.println();
        }

        private static void showPrompt() {
            System.out.print("Enter command > ");
        }
    }
