package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;





public class AtmInterface {

	
	public static void main(String[] args) 
		throws NumberFormatException, IOException, SQLException, ClassNotFoundException, ParseException {
			

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("================================================================================");
			System.out.println("=========================WELCOME TO SBI ATM =============================");
			System.out.println("==============================================================================");
			
			
			
			System.out.println("==============================================================================");
			System.out.println("===========================    LOGIN DETAILS  ================================");
			System.out.println("==============================================================================");

				System.out.print("\t Enter your username:");
				String userName = br.readLine();
				System.out.print("\t Enter your pin:");
				String userPin = br.readLine();
				
				
				try {
					Connection conn = Mysqlconnector.getConnection();
					PreparedStatement ps = conn.prepareStatement("select accPin from accounts where accUsername=?");
					ps.setString(1, userName);
					ResultSet result = ps.executeQuery();
					String pin = null;
					boolean login = false;
					while (result.next()) {
						pin = result.getString("accPin");
						login = true;
					}

			
					if (pin.equals(userPin)) {
						System.out.println("==============================================================================");
						System.out.println("===========================   Login successful ================================");
						System.out.println("==============================================================================");
						
						

						String status = "Y";
						do {

							System.out.println("\t\t  1 --> Deposit Amount");
							System.out.println("\t\t  2 --> Withdraw Amount");
							System.out.println("\t\t  3 --> Balance Check");
							System.out.println("\t\t  4 --> Change Pin");
							System.out.println("\t\t  5 -->Mini Statement");
							System.out.println("\t\t  6 --> Exit/Logout");
							System.out.println("==============================================================================");
							
							System.out.print("Enter your choice:");
							int operationCode = Integer.parseInt(br.readLine());
							ResultSet res;
							switch (operationCode) {
							
							case 1:
								System.out.println("Enter the deposit amount:");
								double depositAmount = Double.parseDouble(br.readLine());

								ps = conn.prepareStatement("select * from accounts where accUsername=?");
								ps.setString(1, userName);
								res = ps.executeQuery();
								double existingBalance = 0.0;
								long accId = 0;
								while (res.next()) {
									existingBalance = res.getDouble("accBalance");
									accId = res.getLong("accId");
								}

								existingBalance = existingBalance + depositAmount;

								ps = conn.prepareStatement("update accounts set accBalance=? where accUsername=?");
								ps.setDouble(1, existingBalance);
								ps.setString(2, userName);

								if (ps.executeUpdate() > 0) {
									ps = conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
									Timestamp timestamp = new Timestamp(System.currentTimeMillis());
									long transactionId = timestamp.getTime();

									ps.setLong(1, transactionId);
									ps.setDate(2, new Date(System.currentTimeMillis()));
									ps.setDouble(3, existingBalance);
									ps.setString(4, "deposit");
									ps.setLong(5, accId);
									ps.setLong(6, accId);

									ps.executeUpdate();

									System.out.println("==============================================================================");
									System.out.println("Balance Updated!!");
									System.out.println("New account balance is :" + existingBalance);
									System.out.println("==============================================================================");

								}

								System.out.println("Do you want to continue?(Y/N)");
								status = br.readLine();

								if (status.equals("n") || status.equals("N")) {
									login = false;
								}

								break;
							case 2:
								System.out.println("Enter the withdraw amount:");
								double withdrawAmount = Double.parseDouble(br.readLine());

								ps = conn.prepareStatement("select * from accounts where accUsername=?");
								ps.setString(1, userName);
								res = ps.executeQuery();
								existingBalance = 0.0;
								accId = 0;
								while (res.next()) {
									existingBalance = res.getDouble("accBalance");
									accId = res.getLong("accId");
								}

								existingBalance = existingBalance - withdrawAmount;

								ps = conn.prepareStatement("update accounts set accBalance=? where accUsername=?");
								ps.setDouble(1, existingBalance);
								ps.setString(2, userName);

								if (ps.executeUpdate() > 0) {
									ps = conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
									Timestamp timestamp = new Timestamp(System.currentTimeMillis());
									long transactionId = timestamp.getTime();

									ps.setLong(1, transactionId);
									ps.setDate(2, new Date(System.currentTimeMillis()));
									ps.setDouble(3, existingBalance);
									ps.setString(4, "withdraw");
									ps.setLong(5, accId);
									ps.setLong(6, accId);

									ps.executeUpdate();

									System.out.println("==============================================================================");
									System.out.println("Balance Updated!!");
									System.out.println("New account balance is :" + existingBalance);
									System.out.println("==============================================================================");

								}

								System.out.println("Do you want to continue?(Y/N)");
								status = br.readLine();

								if (status.equals("n") || status.equals("N")) {
									login = false;
								}

								break;

							case 3:
								ps = conn.prepareStatement("select * from accounts where accUsername=?");
								ps.setString(1, userName);
								res = ps.executeQuery();
								double balance = 0.0;
								while (res.next()) {
									balance = res.getDouble("accBalance");

								}
								System.out.println("==============================================================================");
								System.out.println("Current account balance is :" + balance);
								System.out.println("==============================================================================");
								System.out.println("Do you want to continue?(Y/N)");
								status = br.readLine();

								if (status.equals("n") || status.equals("N")) {
									login = false;
								}

								break;

							
							
							
							
							case 4:
								System.out.println("Enter the old pin:");
								String oldPin = br.readLine();

								System.out.println("Enter the new pin:");
								String newPin = br.readLine();

								System.out.println("Re-enter the new pin:");
								String rePin = br.readLine();

								ps = conn.prepareStatement("select * from accounts where accUsername=?");
								ps.setString(1, userName);

								res = ps.executeQuery();
								String existingpin = null;
								while (res.next()) {
									existingpin = res.getString("accPin");

								}

								if (existingpin.equals(oldPin)) {
									if (newPin.equals(rePin)) {
										ps = conn.prepareStatement("update accounts set accPin=? where accUsername=?");
										ps.setString(1, newPin);
										ps.setString(2, userName);

										if (ps.executeUpdate() > 0) {
											System.out.println("==============================================================================");
											System.out.println("Pin changed successfully!!");
											System.out.println("==============================================================================");

										} else {
											System.out.println("==============================================================================");
											System.out.println("Problem in pinchanged!!");
											System.out.println("==============================================================================");

										}

									} else {
										System.out.println("==============================================================================");
										System.out.println("New pin and retype password must be same!!");
										System.out.println("==============================================================================");

									}
								} else {
									System.out.println("==============================================================================");
									System.out.println("Old pin is wrong!!");
									System.out.println("==============================================================================");

								}
								System.out.println("Do you want to continue?(Y/N)");
								status = br.readLine();

								if (status.equals("n") || status.equals("N")) {
									login = false;
								}
								break;


								
								
								case 5:
									System.out.println("Transaction id\t Date \t Amount \t Type");
									
									ps=conn.prepareStatement("select * from transactions");
									ResultSet transactions = ps.executeQuery();
									
									while(transactions.next()) {
										System.out.println(transactions.getLong("transactionId") +"\t"
										+transactions.getDate("transactionDate")
										
										+"\t" +transactions.getDouble("transactionAmount") + "\t"
										+ transactions.getString("transactionType"));
										
									}
									System.out.println("Do you want to continue?(Y/N)");
									status = br.readLine();
									
									if (status.equals("n") || status.equals("N")){
										login = false;
									}
									break;	
										
									
							case 6:
								login = false;
								break;

							default:
								System.out.println("Wrong Choice!!");
								break;

							}

						} while (login);
						System.out.println("==============================================================================");
						System.out.println("Bye. Have a nice day!!");
						System.out.println("==============================================================================");

					} else {
						System.out.println("==============================================================================");
						System.out.println("================================  Wrong pin  ============================");
						System.out.println("==============================================================================");
					}
				} catch (Exception e) {
					System.out.println(e);
					System.out.println("==============================================================================");
					System.out.println("===========================  Wrong username/pin  ========================");
					System.out.println("==============================================================================");

				}

	}
}