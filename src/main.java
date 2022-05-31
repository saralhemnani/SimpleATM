//author->saral hemnani && jalak patel
//date->4/10/19
//terminal based banking application using java
import java.text.NumberFormat;
import java.util.Scanner;
import java.util.Vector;
import java.io.*;
class PasswordField {
   public static String readPassword (String prompt) {
      EraserThread et = new EraserThread(prompt);
      Thread mask = new Thread(et);
      mask.start();

      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      String password = "";
      try {
          password = in.readLine();
      } catch (IOException ioe) {
          ioe.printStackTrace();
      }
      et.stopMasking();
      return password;
   }
}   

class EraserThread implements Runnable {
   private boolean stop;

   public EraserThread(String prompt) {
       System.out.print(prompt);
   }

   public void run () {
      while (!stop){
         System.out.print("\010*");
         try {
            Thread.currentThread().sleep(1);
         } catch(InterruptedException ie) {
            ie.printStackTrace();
         }
      }
   }

   public void stopMasking() {
      this.stop = true;
   }
}
class ATM {
	public static void main(String[] args) {
		int bank_rate=4;
		Vector	<Account> v = new Vector<Account>();
		Account current=new Account();
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		Scanner in = new Scanner(System.in); 
		boolean flag = true,flag2=false;
		while(flag){
			System.out.println("****************WELCOME TO THE BANK********************");
			System.out.print("\nBANK Menu:\n \n"
								+ "1. Existing User \n"
								+ "2. New User \n"
								+ "3. End Session\n \n"
								+ "Enter your choice: ");
			int choice=in.nextInt();
			switch(choice){
				case 1:
					System.out.print("Enter your Account Number: ");
					int num=in.nextInt();
					String password2 = PasswordField.readPassword("Enter your PIN: ");
					int pin=Integer.parseInt(password2);
					if((new Account()).find(v,num)==-1){
						(new Account()).Error();	
						System.out.println("Account does not exist! Exiting....");
					}
					else if(v.get((new Account()).find(v,num)).getPin()!=pin){
						(new Account()).Error();
						System.out.print("Invalid Pin! Exiting....");
						flag2=false;
					}else{
						current=v.get((new Account()).find(v,num));
						flag2=true;
					}
					break;
				case 2:
					System.out.println("***********Account Created***********");
					System.out.print("Enter your name: ");
					String str=in.nextLine();
					str=in.nextLine();
					System.out.println("Account Number:  " + (v.size()+1+100));
					String password = PasswordField.readPassword("Set Pin:");
					int pi=Integer.parseInt(password);
					Account obj=new Account(v.size()+1,pi,str,0.0,bank_rate);
					v.add(obj);
					current=obj;
					break;
				case 3:
					flag=false;
					flag2=false;
					break;
				default:
					System.out.println("Invalid Selection,try again!");

			}
			while (flag2) {
			System.out.println("WELCOME: " + current.getName());
			System.out.print("\nATM Menu: \n \n"
							 + "1. Deposit Money \n"
							 + "2. Withdraw Money \n"
							 + "3. Change PIN \n"
							 + "4. Check Account Balance\n"
							 + "5. Fund Transfer\n "
							 + "6. End Session\n \n"
							 + "Enter your choice: ");
			int selection1 = in.nextInt(); 
					switch (selection1) {
						case 1:
								System.out.println("\nYour current Account balance is: " + formatter.format(current.getBalance()) + "\n");
								System.out.println("How much money would you like to deposit?");
								double deposit = in.nextDouble();
								current.deposit(deposit);
								System.out.println("\nYour Account balance is now: " + formatter.format(current.getBalance()) + "\n");
							break;
						case 2:
								System.out.println("\nYour current Account balance is: " + formatter.format(current.getBalance()) + "\n");
								System.out.println("How much money would you like to withdraw?");
								double withdraw = in.nextDouble();
								if(current.withdraw(withdraw)){
									System.out.println("\nYour Account balance is now: " + formatter.format(current.getBalance()) + "\n");
								}
							break;
						case 3:	
								boolean flag3=true;
							while(flag3){
								String pin = PasswordField.readPassword("New Pin:");
								int new_pin=Integer.parseInt(pin);
								String pin2= PasswordField.readPassword("Confirm Pin:");
								int new_pin_confirm=Integer.parseInt(pin2);
								if(new_pin==new_pin_confirm){
									current.changePin(new_pin);
									System.out.println("Your Pin has been changed,Please log in again!");
									flag2=false;
									flag3=!flag3;
								}else{
									System.out.println("Please try changing again!");
								}
							}
							break;
						case 4:
							System.out.println("Account Balance: " + formatter.format(current.getBalance()) + "\n");
							System.out.println();
							break;
						case 5:
							System.out.println("Fund Transfer initiating......************");
							System.out.print("Enter the Account Number to transfer: ");
							int to_num=in.nextInt();
							if((new Account()).find(v,to_num)==-1){
								current.Error();
								System.out.print("The Account does not exist,try Again!");
							}else{
								//we found the account implement
								System.out.print("Enter the amount to be transferred: ");
								double amount=in.nextDouble();
								if(current.withdraw(amount)){
									v.get((new Account()).find(v,to_num)).deposit(amount);
								}
								System.out.println("\nYour Account balance is now: " + formatter.format(current.getBalance()) + "\n");
							}
						case 6:
							flag2 = false;
							break;
						default:
						System.out.println("Invalid Selection,try again!");

				}				 	
			}
		}
	System.out.println("\nThank you for banking with us!\n");
	}
}
class Account {
	private int number;
	private int pin;
	private String name;
	private double balance;
	private double rate;
	void setBalance(double accBal) {
		balance = accBal;
	}

	void setRate(double accRate) {
		rate = accRate;
	}
	void deposit(double dep) {
		balance += (dep+= ((dep*rate)/100));
	}
	boolean withdraw(double wit) {
		if(balance - wit >=0 ){
			balance = balance-wit;
			return true;
		}else{
			Error();
			System.out.println("Insufficient Funds,please deposit enough Money!");
			return false;
		}
	}
	int getNumber(){
		return number;
	}
	int getPin(){
		return pin;
	}
	String getName() {
		return name;
	}
	double getBalance() {
		return balance;
	}
	double getRate() {
		return rate;
	}
	void changePin(int pin){
		this.pin=pin;
		return;
	}
	Account(){

	}
	Account(int n,int p,String str,double bal,double r){
			number=n;
			name=str;
			pin=p;
			balance=bal;
			rate=r;

	}
	void Error(){
		System.out.println(" ");
		System.out.println("Error-*********************************************");
	}
	int find(Vector <Account> vtr,int acc_number){
			for(int i=0;i<vtr.size();i++){
				Account temp=vtr.get(i);
				if(temp.getNumber()==acc_number){
					return i;
				}
			}
			return -1;
		}
}