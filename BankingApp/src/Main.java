import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import static java.lang.System.exit;

public class Main {

    static int TRUE = 1;
    static int FALSE = 0;

    static Scanner keyboard_input = new Scanner(System.in);
    static Map<String, User> user_database = new HashMap<>(); //holds account numbers and users associated with account #
    static Map<String, String> transfer_database = new HashMap<>(); //Holds usernames of accounts associated with an account number

    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    static LocalDateTime today = LocalDateTime.now();

    public static void main(String[] args) {

        while(true) {

            System.out.println("\n#######################################################");
            System.out.println(" \t\t\tHello! Welcome to Bank of SWE.");
            System.out.println("#######################################################\n");

            char input;

            System.out.println("Are you a registered user? Press Y for yes or N for no...");
            input = (char) keyboard_input.next().charAt(0);

            User curr_user = null;

            if (yes_no_input(input) == FALSE) {
                curr_user = not_user();
                System.out.println("______________________________________________________________________________________________________");
                System.out.printf("Account Number: %s \tName: %s \tDate: %s", curr_user.get_account_num(), curr_user.person.get_first_name() + " " + curr_user.person.get_last_name(), dtf.format(today) + "\n");
            } else {
                curr_user = is_user();
            }

            input = ' ';

            while (input != '0') {

                System.out.println("\nPlease select an option:");
                System.out.println("""
                        1 -> View Balance
                        2 -> Deposit
                        3 -> Withdraw
                        4 -> View Account Details
                        5 -> Transfer
                        6 -> Transaction History
                        7 -> Sign Out
                        0 -> Exit
                        """);

                input = keyboard_input.next().charAt(0);
                double amount;

                switch (input) {
                    case '0' -> {
                        exit_program();
                    }

                    //get balance
                    case '1' -> {
                        if (curr_user != null) {
                            System.out.printf("Current balance: $%.2f\n", curr_user.account.get_balance());
                        }
                    }

                    //deposit
                    case '2' -> {
                        System.out.println("How much would you like to deposit?");
                        amount = keyboard_input.nextDouble();

                        if (curr_user != null) {
                            curr_user.account.deposit(amount, 1);
                        }
                    }

                    //withdraw
                    case '3' -> {
                        System.out.println("How much would you like to withdraw?");
                        amount = keyboard_input.nextDouble();

                        if (curr_user != null) {
                            curr_user.account.withdraw(amount);
                        }
                    }

                    //view all account details
                    case '4' -> {
                        if (curr_user != null) {
                            System.out.println(curr_user.person.toString() + curr_user.account.toString());
                        }
                    }

                    //transfer
                    case '5' ->{

                        System.out.println("Enter the username of the person you would like to transfer money to:");
                        String name = keyboard_input.next();

                        if(transfer_database.containsKey(name)){
                            System.out.printf("Enter the amount you would like to send to %s.\n", name);
                            amount = keyboard_input.nextDouble();

                            if(amount > 0 && curr_user != null){
                                curr_user.account.transfer(name, amount, transfer_database, user_database);
                            }
                        }
                    }

                    //transaction history
                    case '6' ->{
                        if(curr_user != null && !curr_user.account.transactions.isEmpty()){
                            System.out.println("Your transaction history...");
                            System.out.println(curr_user.account.print_transaction_details());
                        }else{
                            System.out.println("No transaction history.");
                        }
                    }

                    //sign out
                    case '7' -> {
                        input = '0';
                    }

                    //admin view
                    case '#' -> {
                        if(admin_view() == FALSE){
                            System.out.println("ERROR: No users present in database.");
                        }
                    }

                    default -> {
                        break;
                    }
                }
            }
        }
    }

    //handles input if user already has an account
    private static User is_user() {

        String digits_input;
        String email_input;
        User existing_user = null;

        System.out.println("To sign in, please enter your account number.");
        digits_input = keyboard_input.next();
        System.out.println("Please enter your email address.");
        email_input = keyboard_input.next();

        if(user_database.containsKey(digits_input) && (user_database.get(digits_input).person.get_email()).equalsIgnoreCase(email_input)){
            String first_name = user_database.get(digits_input).person.get_first_name();
            System.out.printf("Welcome back, %s.\n", first_name);
            existing_user = user_database.get(digits_input);
        }else{
            System.out.println("User does not exist or incorrect credentials were given.");
            exit_program();
        }

        return existing_user;
    }

    //handles input if user is a new user
    private static User not_user() {

        System.out.println("Would you like to sign up for Bank of SWE? Y or N...");
        int result = yes_no_input(keyboard_input.next().charAt(0));

        User new_user = new User();

        if(result == TRUE){
            new_user = create_user();
        }else{
            exit_program();
        }

        return new_user;
    }

    //handles input if user is new and wants to create an account
    private static User create_user() {

        String first_name, last_name, address, phone_number, email;
        System.out.println("\nPlease enter your details below.");

        System.out.println("First name: ");
        first_name = keyboard_input.next();

        System.out.println("Last name: ");
        last_name = keyboard_input.next();

        keyboard_input.nextLine();
        System.out.println("Address: ");
        address = keyboard_input.nextLine();

        System.out.println("Phone number: ");
        phone_number = keyboard_input.next();

        System.out.println("Email: ");
        email = keyboard_input.next();

        Person new_person = new Person(first_name, last_name, address, phone_number, email);
        User new_user = new User(new_person);

        //Ensures account number is available for use, if it isnt, a new account number will be generated
        //before being added to the TreeMap database
        while(user_database.containsKey(new_user.get_account_num())){
            new_user.gen_new_acc_num();
        }

        user_database.put(new_user.get_account_num(), new_user);

        String username = new_user.get_userid();
        transfer_database.put(username, new_user.get_account_num());

        System.out.printf("\nSuccess! Account created. Welcome %s to bank of SWE.\n", new_user.person.get_first_name());
        //work on view account details, print name etc, and account + routing numbers

        return new_user;
    }

    //handles yes or no prompts
    private static int yes_no_input(char input) {
        while(true) {
            switch (input) {
                case 'Y', 'y' -> {
                    return TRUE;
                }
                case 'N', 'n' -> {
                    return FALSE;
                }
                case '0' -> {
                    exit_program();
                }
                default -> {
                    System.out.println("Please enter Y for yes, N for no, or 0 to exit program.");
                    input = (char) keyboard_input.next().charAt(0);
                }
            }
        }
    }

    //displays admin view/internal database
    private static int admin_view(){

        if(user_database.isEmpty()){
            return FALSE;
        }

        System.out.println("********************************************************  ADMIN VIEW  ***************************************************************");
        System.out.println("=====================================================================================================================================");

        for(Map.Entry<String, User> users : user_database.entrySet()){
            User user_data = users.getValue();
            System.out.printf("|\t%s\t|\t%s\t|\t%s\t|\t%s\t|\t%s\t|\t%s\t|\t$%s\t|\n", user_data.get_account_num(), user_data.get_rout_num(), user_data.person.get_first_name(),
                                                                        user_data.person.get_last_name(), user_data.person.get_email(), user_data.person.get_phone_number(),
                                                                        user_data.account.get_balance());
                    //acctnumber, routingnum, firstname, lastname, email, phone, balance
        }

        System.out.println("=====================================================================================================================================");

        return 0;
    }

    //exits the program
    private static void exit_program() {
        System.out.println("Thank you, goodbye.");
        exit(0);
    }
}
