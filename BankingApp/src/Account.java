import java.util.LinkedList;
import java.util.Map;

public class Account {

    User add_user;
    private double balance = 0;
    private double deposit;
    private double withdraw;

    LinkedList<Transactions> transactions = new LinkedList<>();

    public Account(){
    }

    public Account(User add_user){
        this.add_user = add_user;
    }

    public double get_balance(){
        return balance;
    }

    public int withdraw(double withdraw_amt){
        if(withdraw_amt > get_balance()){
            System.out.printf("Insufficient Funds. Your current balance is $%.2f\n", get_balance());
            return 0;
        }else{
            balance -= withdraw_amt;
            transactions.add(new Transactions(Transaction_type.WITHDRAWAL, withdraw_amt));
            System.out.printf("Withdrawal of $%.2f was successful.\n", withdraw_amt);
            System.out.printf("Your current balance is %.2f\n", get_balance());
            return 1;
        }
    }

    public void deposit(double deposit_amt, int flag){

        int print = 1;

        //print message if using internal deposit
        if(flag == print){
            if(deposit_amt < 1){
                System.out.println("Deposit an amount greater than $1.");
            }else{
                balance += deposit_amt;
                transactions.add(new Transactions(Transaction_type.DEPOSIT, deposit_amt));
                System.out.printf("Deposit of $%.2f was successful.\n", balance);
                System.out.printf("Your current balance is $%.2f\n", get_balance());
            }
        }else{ //do not print message if transfer deposit
            balance += deposit_amt;
            transactions.add(new Transactions(Transaction_type.INCOMING_TRANSFER, deposit_amt));
        }
    }

    public int transfer(String dest_username, double amount, Map<String, String> transfer_database, Map<String, User> user_database){

        if(transfer_database.containsKey(dest_username)){
            String dest_acct = transfer_database.get(dest_username);

            User dest_user = user_database.get(dest_acct);

            if(amount > 0){
                this.balance -= amount;
                dest_user.account.deposit(amount, 0);
                System.out.printf("Transfer of $%s to %s was successful.", amount, dest_username);
                transactions.add(new Transactions(Transaction_type.OUTGOING_TRANSFER, amount));
            }
            return 1;
        }
        return 0;
    }

    public StringBuilder print_transaction_details(){

        StringBuilder tr_details = new StringBuilder();

        for(Transactions tr_element : transactions){
            tr_details.append(tr_element.get_transaction_details());
        }
        return tr_details;
    }

    @Override
    public String toString(){

        return "\nAccount number:\t" + add_user.get_account_num() + "\n" +
                "Routing number:\t" + add_user.get_rout_num() + "\n" +
                "Balance:\t\t$" + get_balance();
    }

}

enum Transaction_type{
    DEPOSIT,
    WITHDRAWAL,
    INCOMING_TRANSFER,
    OUTGOING_TRANSFER;
}