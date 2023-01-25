import java.util.Random;

public class User {

    public Person person;
    public Account account;
    private String account_num;
    private String routing_num;
    private String user_id;
    private final String acct_prefix = "4744";
    private final String rout_prefix = "0";

    public User(){
    }

    public User(Person new_person){
        this.person = new_person;
        this.account_num = gen_new_acc_num();
        this.routing_num = gen_new_rout_num();
        this.user_id = this.person.get_first_name() + this.person.get_last_name();
        this.account = new Account(this);
    }

    public String get_userid(){
        return this.user_id;
    }

    public String get_account_num(){
        return this.account_num;
    }

    public String get_rout_num(){
        return this.routing_num;
    }

    private String gen_new_rout_num(){
        Random rand = new Random(System.currentTimeMillis());
        routing_num = rout_prefix + Integer.toString(rand.nextInt(99999999));
        return routing_num;
    }

    public String gen_new_acc_num(){
        Random rand = new Random(System.currentTimeMillis());
        account_num = acct_prefix + Integer.toString(rand.nextInt(99999999));
        return account_num;
    }

}


