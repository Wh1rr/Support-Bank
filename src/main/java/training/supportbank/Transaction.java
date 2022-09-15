package training.supportbank;
import java.math.BigDecimal;
import java.time.*;

public class Transaction{
    BigDecimal amount;
    String reason;
    LocalDate date;
    public Transaction(BigDecimal amount, String reason, LocalDate date){
        this.amount = amount;
        this.reason = reason;
        this.date = date;
    }
}
