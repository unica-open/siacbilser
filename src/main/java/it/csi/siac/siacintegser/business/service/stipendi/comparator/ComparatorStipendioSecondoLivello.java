/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacintegser.business.service.stipendi.model.Stipendio;

/**
 * compare i capitoli degli stipendi di spesa
 * @author Nazha Ahmad
 *
 */
public final class ComparatorStipendioSecondoLivello implements Comparator<Stipendio>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3527431966414057333L;
	/** The only accessible instance */
	public static final ComparatorStipendioSecondoLivello INSTANCE = new ComparatorStipendioSecondoLivello();

	/** Costruttore vuoto di default */
	private ComparatorStipendioSecondoLivello() {
		super();
	}
	
	@Override
	public int compare(Stipendio s1, Stipendio s2) {
		Accertamento a1 = s1.getAccertamento();
		Accertamento a2 = s2.getAccertamento();
		
		SubAccertamento sa1 = s1.getSubAccertamento();
		SubAccertamento sa2 = s2.getSubAccertamento();
		
		Impegno i1 = s1.getImpegno();
		Impegno i2 = s2.getImpegno();
		
		SubImpegno si1 = s1.getSubImpegno();
		SubImpegno si2 = s2.getSubImpegno();
		
		/*
		 * Errore riscontrato da csi in ambiente di test:  Errore di runtime nell'esecuzione del Servizio.: java.lang.IllegalArgumentException: Comparison method violates its general contract!
		 * La relazione non e' una relazione d'ordine, tenuto commentato come da accordi con CSI per eventuale patch urgente:
		 *  "Visto che quell'ordinamento è così da sempre senza aver mai dato questo errore, visto che il problema si  è verificato solo su forn1 e che ad oggi sembra essere una "combinazione sfortunata", 
		 *  io ti proporrei quanto segue: io inserisco un commento nel codice con la modifica, in modo che se capitasse in produzione basterebbe fare una patch velocissima scommentando quel pezzo" 
		 */
		/*
		Accertamento a1 = s1.getAccertamento()!= null && s1.getAccertamento().getAnnoMovimento() != 0 && s1.getAccertamento().getNumero() != null?  s1.getAccertamento() : null;
		Accertamento a2 = s2.getAccertamento()!= null && s2.getAccertamento().getAnnoMovimento() != 0 && s2.getAccertamento().getNumero() != null?  s2.getAccertamento() : null;
		
		SubAccertamento sa1 = s1.getSubAccertamento()!= null && s1.getSubAccertamento().getAnnoMovimento() != 0 && BigDecimal.ZERO.compareTo(s1.getSubAccertamento().getNumero()) != 0?  s1.getSubAccertamento() : null;
		SubAccertamento sa2 = s2.getSubAccertamento()!= null && s2.getSubAccertamento().getNumero() != null && BigDecimal.ZERO.compareTo(s2.getSubAccertamento().getNumero()) !=0?  s2.getSubAccertamento() : null;
		
		Impegno i1 = s1.getImpegno()!= null && s1.getImpegno().getAnnoMovimento() != 0 && s1.getImpegno().getNumero() != null?  s1.getImpegno() : null;
		Impegno i2 = s2.getImpegno()!= null && s2.getImpegno().getAnnoMovimento() != 0 && s2.getImpegno().getNumero() != null?  s2.getImpegno() : null;
		
		SubImpegno si1 = s1.getSubImpegno()!= null && s1.getSubImpegno().getAnnoMovimento() != 0 && BigDecimal.ZERO.compareTo(s1.getSubImpegno().getNumero()) != 0?  s1.getSubImpegno() : null;
		SubImpegno si2 = s2.getSubImpegno()!= null && s2.getSubImpegno().getNumero() != null && BigDecimal.ZERO.compareTo(s2.getSubImpegno().getNumero()) !=0?  s2.getSubImpegno() : null;
		*/
		
		if(a1 == null || i1 == null || a2 == null || i2 == null) {
			return compareBySingoloMovgest(a1, i1, sa1, si1, a2, i2, sa2, si2);
		}
		int compareValue = compareBySingoloMovgest(a1, null, sa1, null, a2, null, sa2, null);
		if(compareValue != 0) {
			return compareValue;
		}
		return compareBySingoloMovgest(null, i1, null, si1, null, i2, null, si2);
	}

	private int compareBySingoloMovgest(Accertamento a1, Impegno i1, SubAccertamento sa1, SubImpegno si1, Accertamento a2, Impegno i2, SubAccertamento sa2, SubImpegno si2) {
		MovimentoGestione m1 = a1 != null ? a1 : i1;
		MovimentoGestione m2 = a2 != null ? a2 : i2;
		
		MovimentoGestione sm1 = sa1 != null ? sa1 : si1;
		MovimentoGestione sm2 = sa2 != null ? sa2 : si2;		
		
		if(m1 ==m2) {
			return 0;
		}
		// Check dei valori null. Un valore null e' considerato inferiore di un qualsiasi valore non-null
		if(m1 == null) {
			return -1;
		}
		if(m2 == null) {
			return 1;
		}

		StringBuilder sb = new StringBuilder()
				.append("m1 : ")
				.append(m1.getAnnoMovimento())
				.append(" / ")
				.append(m1.getNumero() != null? m1.getNumero() : "null")
				.append(" e m2 : ")
				.append(m2.getAnnoMovimento())
				.append(" / ")
				.append(m2.getNumero() != null? m2.getNumero() : "null");
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(m1.getAnnoMovimento(), m2.getAnnoMovimento())
		  				.append(m1.getNumero(),m2.getNumero());
		int comparison = compareToBuilder.toComparison();
		if(comparison!=0) {
			return comparison;
		}
		
		if(sm1 ==sm2) {
			return 0;
		}
		// Check dei valori null. Un valore null e' considerato inferiore di un qualsiasi valore non-null
		if(sm1 == null) {
			return -1;
		}
		if(sm2 == null) {
			return 1;
		}
		
		return compareToBuilder.append(sm1.getNumero(),sm2.getNumero())
				.toComparison();
	}
}
