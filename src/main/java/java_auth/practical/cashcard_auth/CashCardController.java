package java_auth.practical.cashcard_auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

	private CashCardRepository cashCards;

	public CashCardController(CashCardRepository cashCards) {
		this.cashCards = cashCards;
	}

	@GetMapping("/{requestedId}")
	public ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
		return this.cashCards.findById(requestedId).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	private ResponseEntity<CashCard> createCashCard(@RequestBody CashCardRequest cashCardRequest,
			UriComponentsBuilder ucb, @CurrentOwner String owner) {
		CashCard savedCashCard = cashCards.save(new CashCard(cashCardRequest.amount(), owner));
		URI locationOfNewCashCard = ucb.path("cashcards/{id}").buildAndExpand(savedCashCard.getId()).toUri();
		return ResponseEntity.created(locationOfNewCashCard).body(savedCashCard);
	}

	@GetMapping
//    public ResponseEntity<Iterable<CashCard>> findAll(Authentication authentication) {
	public ResponseEntity<Iterable<CashCard>> findAll(@CurrentOwner String owner) {
		var result = this.cashCards.findByOwner(owner);
		return ResponseEntity.ok(result);
	}

}
