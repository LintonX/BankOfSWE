public class Person {

    private String first_name, last_name, address, phone_number, email;

    public Person(String first_name, String last_name, String address, String phone_number, String email){
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.phone_number = phone_number;
        this.email = email;
    }

    public String get_first_name(){
        return this.first_name;
    }

    public String get_last_name(){
        return this.last_name;
    }

    public String get_address(){
        return this.address;
    }

    public String get_phone_number(){
        return this.phone_number;
    }

    public String get_email(){
        return this.email;
    }

    @Override
    public String toString(){
        return "First name: \t" + get_first_name() + " \nLast name: \t\t" +
                get_last_name() + " \nAddress: \t\t" + get_address() + " \nPhone number: \t" + get_phone_number()
                + " \nEmail: \t\t\t" + get_email();
    }
}
