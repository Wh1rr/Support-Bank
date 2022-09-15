package training.supportbank;
import java.math.BigDecimal;
import java.time.*;

public class Transaction{
    BigDecimal amount;
    String reason;
    LocalDate date;
    String person;
    public Transaction(BigDecimal amount, String reason, LocalDate date, String person){
        this.amount = amount;
        this.reason = reason;
        this.date = date;
        this.person = person;
    }

    public BigDecimal getAmount(){return this.amount;}

    public String getAll(){
        return String.format("To/From - %s\nDate - %s\nReason - %s\nAmount - %s\n\n", this.person, this.date, this.reason, this.amount);
    }
}
