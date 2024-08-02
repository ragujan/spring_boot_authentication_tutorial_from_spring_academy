package java_auth.practical.cashcard_auth;

import org.springframework.data.repository.CrudRepository;
public interface CashCardRepository extends CrudRepository<CashCard, Long> {
	 Iterable<CashCard> findByOwner(String owner);
}