package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.hibernate.collection.internal.PersistentSortedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

		System.out.println("Welcome");
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers

			LocalDateTime today= LocalDateTime.now();
			LocalDateTime yesterday= today.minusMonths(1);

			DateTimeFormatter todayFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			String formateyes= yesterday.format(todayFormat);
			String formattedDate = today.format(todayFormat);

			LocalDateTime tomorrow= today.plusDays(1);

			Client client1= new Client("Melba", "Morel","melba@mindhub.com", passwordEncoder.encode("1234"));
			Client client2= new Client("Carlos", "Valle","holamundo@mindhub.com", passwordEncoder.encode("12345"));
			Client client3= new Client("Federico", "Estrada", "fede@hotmail.com", passwordEncoder.encode("123456"));
			Client clientAd= new Client("admin", "admin", "admin", passwordEncoder.encode("admin"));

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			clientRepository.save(clientAd);


			Account account1= new Account("Caja de Ahorro","VIN001",today,5233.45 , true, client1);
			Account account2= new Account("Cuenta Corriente","VIN002",tomorrow,7500.35, true, client1);
			Account account3= new Account("Cuenta Corriente","VIN003",today,6454.20, true,client2);
			Account account4= new Account("Caja de Ahorro","VIN004",tomorrow,15001.45, true,client2);
			Account account5= new Account("Cuenta Corriente","VIN005",tomorrow,20001.45, true,client3);
			//client1.addAccount(account1);
			//client1.addAccount(account2);
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);


			TransactionType myTypeC= TransactionType.CREDIT;
			TransactionType myTypeD= TransactionType.DEBIT;

			Transaction transaction1= new Transaction( myTypeC, 200,"Pago honorarios", LocalDate.now().minusMonths(1), account1.getBalance() + 200 ,account1);
			Transaction transaction2= new Transaction( myTypeD, -785.34,"Pago de servicio CV", LocalDate.now(), account1.getBalance() - 785.34 ,account1);
			Transaction transaction3= new Transaction( myTypeD, -560.25,"Pago de servicio", LocalDate.now(), account2.getBalance() -560.25,account2);
			Transaction transaction4= new Transaction( myTypeD, -1000.25,"xbox live", LocalDate.now(),account3.getBalance() - 1000.25,account3);
			Transaction transaction5= new Transaction( myTypeC, 750.25,"spotify",LocalDate.now(), account4.getBalance() +750.25,account4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);


			var cuotas = List.of(12,24,36,48,60);
			Loan loan1 = new Loan("Hipotecario", 500000, 15.33,cuotas);
			Loan loan2 = new Loan("Personal", 100000, 22,List.of(6,12,24));
			Loan loan3 = new Loan("Automotriz", 300000, 33.5, List.of(6, 12, 24, 36));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1= new ClientLoan(400000, 60, client1, loan1);
			ClientLoan clientLoan2= new ClientLoan(10000, 6, client1, loan2);
			ClientLoan clientLoan3= new ClientLoan(50000, 12, client1, loan2);
			ClientLoan clientLoan4= new ClientLoan(100000, 24, client2, loan2);
			ClientLoan clientLoan5= new ClientLoan(200000, 36, client2, loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);
			clientLoanRepository.save(clientLoan5);

			LocalDate todayCard= LocalDate.now();

			DateTimeFormatter todayFormatCard = DateTimeFormatter.ofPattern("MM-yy");
			String formattedDateCard = todayCard.format(todayFormatCard);
			String dueDate = todayCard.plusYears(5).format(todayFormatCard);

			Card card1 = new Card(CardType.DEBIT, CardColor.GOLD, "2345294874657837", 233, formattedDateCard, dueDate, client1);
			Card card2 = new Card(CardType.CREDIT, CardColor.TITANIUM, "4545294859457837", 777, formattedDateCard, dueDate, client1);
			Card card3 = new Card(CardType.CREDIT, CardColor.SILVER, "4545294824389883", 234, formattedDateCard, dueDate, client2);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);


		};
	}


}
