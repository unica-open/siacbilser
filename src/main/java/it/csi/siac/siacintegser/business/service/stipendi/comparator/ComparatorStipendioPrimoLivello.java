/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacintegser.business.service.stipendi.model.Stipendio;
import it.csi.siac.siacintegser.business.service.stipendi.model.TipoVoce;
/**
 * compare i capitoli degli capitoli di entrata
 * @author Nazha Ahmad
 *
 */
public final class ComparatorStipendioPrimoLivello implements Comparator<Stipendio>,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8003511215606463231L;

	/** The only accessible instance */
	public static final ComparatorStipendioPrimoLivello INSTANCE = new ComparatorStipendioPrimoLivello();

	/** Costruttore vuoto di default */
	private ComparatorStipendioPrimoLivello() {
		super();
	}
	
	@Override
	public int compare(Stipendio s1, Stipendio s2) {
		
		if(s1 == s2) {
			return 0;
		}
		// Check dei valori null. Un valore null e' considerato inferiore di un qualsiasi valore non-null
		if(s1 == null) {
			return -1;
		}
		if(s2 == null) {
			return 1;
		}
		
	   TipoVoce t1 = s1.getTipoVoce();
	   TipoVoce t2 = s2.getTipoVoce();
		if(t1 == t2) {
			return 0;
		}
		// Check dei valori null. Un valore null e' considerato inferiore di un qualsiasi valore non-null
		if(t1 == null) {
			return -1;
		}
		if(t2 == null) {
			return 1;
		}
       Capitolo<?, ?> c1=s1.getCapitolo();
       Capitolo<?, ?> c2=s2.getCapitolo();


		if(c1 == c2) {
			return 0;
		}
		// Check dei valori null. Un valore null e' considerato inferiore di un qualsiasi valore non-null
		if(c1 == null) {
			return -1;
		}
		if(c2 == null) {
			return 1;
		}
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(t1.getCodice(), t2.getCodice())
						.append(s1.isEntrata(), s2.isEntrata())
						.append(c1.getAnnoCapitolo(), c2.getAnnoCapitolo())
		  				.append(c1.getNumeroCapitolo(), c2.getNumeroCapitolo())
		  				.append(c1.getNumeroArticolo(),c2.getNumeroArticolo())
		  				.append(c1.getNumeroUEB(),c2.getNumeroUEB());
		if(c1.getStrutturaAmministrativoContabile() != null && c1.getStrutturaAmministrativoContabile().getCodice() != null && c2.getStrutturaAmministrativoContabile() != null && c2.getStrutturaAmministrativoContabile().getCodice() != null){
			compareToBuilder.append(c1.getStrutturaAmministrativoContabile().getCodice(), c2.getStrutturaAmministrativoContabile().getCodice());
		}
		
		if(c1.getTipoFinanziamento() != null && c1.getTipoFinanziamento().getUid()!=0 && c2.getTipoFinanziamento()!=null  && c2.getTipoFinanziamento().getUid() !=0){
	    	compareToBuilder.append(c1.getTipoFinanziamento().getUid(),c2.getTipoFinanziamento().getUid());
		}
		
		return compareToBuilder.toComparison(); 
	}

}
