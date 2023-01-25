import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class Transactions extends Account{

    Transaction_type curr_type;
    public String tr_time;
    public Double amount;

    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    static LocalDateTime today = LocalDateTime.now();

    public Transactions(Transaction_type type, Double amount){
        curr_type = type;
        this.amount = amount;
        tr_time = dtf.format(today);
    }

    public String get_transaction_details(){
        return curr_type.toString() + " " + " $" + this.amount.toString() + " " + this.tr_time + "\n";
    }

}
