package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		Set<Esame> parziale = new HashSet<>();
		
		// conviene qui resettare soluzione migliore e la media, ogni volta che clicchiamo il bottone
		this.mediaSoluzioneMigliore = 0;
		this.soluzioneMigliore = new HashSet<>();
		
		
		// funzione ricorsiva
			// stupida
		this.cerca(parziale, 0, numeroCrediti);
		
			// migliore
		//this.cerca2(parziale, 0, numeroCrediti);
	
		return this.soluzioneMigliore;	
	}

	// METODO STUPIDO. N! COMPLESSITA, NON CONVERGE CON N GRANDE
	// abbiamo N casi al livello 0, N-1 al livelo 1 ecc
	private void cerca(Set<Esame> parziale, int L, int m) {
		
		// CASI TERMINALI
		
			// -->	se la somma dei crediti aggiunti è >= m ci fermiamo
		
		int crediti = this.sommaCrediti(parziale);
		
				// se maggiore di m scarto parziale ed esco
		if(crediti > m) {
			return;
		}
		
				// se uguale  a m potrebbe essere soluzione ottima
		if(crediti == m) {
			double media = this.calcolaMedia(parziale);
			
					// la media di parziale è più alta di quella trovata, aggiorno la soluzione
			if(media > this.mediaSoluzioneMigliore) {
						// IMPORTANTE CREARE NUOVO OGGETTO SE NO COPIA SOLO IL RIFERIMENTO!!!
				soluzioneMigliore = new HashSet<>(parziale);
				this.mediaSoluzioneMigliore = media;
			}
			
			return;  // esco subito dalla ricorsione perché ho raggiunto max crediti
		}
		
			// sicuro i crediti sono < m, controllo altro caso terminale
			// -->  L = N, non ci sono più esami da aggiungere
		if(L==this.partenza.size()) {
			return;
		}
		
		// GENERARE I SOTTO-PROBLEMI
		
		/* 1 metodo che però considera e1, e2 ma anche e2,e1.. non li vede uguali
		for (Esame e : this.partenza) {
			
			if(!parziale.contains(e)) {
				parziale.add(e);
				this.cerca(parziale, L+1, m);
				// backtracking
				parziale.remove(e);
			}
		}
		*/
		
		// 2 metodo migliore ma non ci salva da tutti i problemi... aggiungiamo controllo i >= L
		for(int i = 0; i < partenza.size(); i ++) {
			
			if(!parziale.contains(partenza.get(i)) && i >= L) {
				parziale.add(partenza.get(i));
				this.cerca(parziale, L+1, m);
				parziale.remove(partenza.get(i));
			}
		}
		
		
	}

	// METODO INTELLIGENTE. 2^N COMPLESSITA'
	private void cerca2(Set<Esame> parziale, int L, int m) {
			
		// CASI TERMINALI
			
			// -->	se la somma dei crediti aggiunti è >= m ci fermiamo
		
		int crediti = this.sommaCrediti(parziale);
			
				// se maggiore di m scarto parziale ed esco
		if(crediti > m) {
			return;
		}
			
				// se uguale  a m potrebbe essere soluzione ottima
		if(crediti == m) {
			double media = this.calcolaMedia(parziale);
				
					// la media di parziale è più alta di quella trovata, aggiorno la soluzione
			if(media > this.mediaSoluzioneMigliore) {
						// IMPORTANTE CREARE NUOVO OGGETTO SE NO COPIA SOLO IL RIFERIMENTO!!!
				soluzioneMigliore = new HashSet<>(parziale);
				this.mediaSoluzioneMigliore = media;
			}
				
			return;  // esco subito dalla ricorsione perché ho raggiunto max crediti
		}
			
			// sicuro i crediti sono < m, controllo altro caso terminale
			// -->  L = N, non ci sono più esami da aggiungere
		if(L==this.partenza.size()) {
			return;
		}
		
		// GENERAZIONE SOTTOPROBLEMI
		
			// dobbiamo capire se aggiungere o meno PARTENZA[L], proviamo entrambe le cose
		
			// qui aggiungiamo elemento
		parziale.add(this.partenza.get(L));
		this.cerca2(parziale, L+1, m);
		
			// qui NON aggiungiamo elemento
				parziale.remove(this.partenza.get(L));
				this.cerca2(parziale, L+1, m);
	}
	
	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
