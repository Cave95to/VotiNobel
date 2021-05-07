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
		cerca(parziale, 0, numeroCrediti);
	
		return this.soluzioneMigliore;	
	}

	
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
		
		for (Esame e : this.partenza) {
			
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca(parziale, L+1, m);
				// backtracking
				parziale.remove(e);
			}
		}
				
		
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
