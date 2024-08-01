package java_auth.practical.cashcard_auth;

import jakarta.persistence.Id;

public record CashCard(@Id Long id, Double amount, String owner) {
	public CashCard(Double amount, String owner) {
		this(null, amount, owner);
	}
}