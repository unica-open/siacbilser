/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.model;

import it.csi.siac.siacbilser.business.service.allegatoatto.CompletaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciAmmortamentoMassivoCespiteService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiIncassoDaElencoService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiPagamentoDaElencoService;

public enum ElabThresholdKey {
	
	EMETTE_ORDINATIVI_DI_PAGAMENTO_DA_ELENCO(EmetteOrdinativiDiPagamentoDaElencoService.class),
	EMETTE_ORDINATIVI_DI_INCASSO_DA_ELENCO(EmetteOrdinativiDiIncassoDaElencoService.class),
	COMPLETA_ALLEGATO_ATTO(CompletaAllegatoAttoService.class),
	INSERISCI_AMMORTAMENTO_MASSIVO_CESPITE(InserisciAmmortamentoMassivoCespiteService.class),
	;
	
	private final Class<?> elabClass;
	private final String elabClassName;
	
	private ElabThresholdKey(Class<?> elabClass) {
		this.elabClass = elabClass;
		this.elabClassName = elabClass.getName();
	}

	/**
	 * @return the elabClass
	 */
	public Class<?> getElabClass() {
		return this.elabClass;
	}
	
	/**
	 * Ottiene la chiave di soglia via la classe
	 * @param elabClass la classe da elaborare
	 * @return la chiave di soglia
	 */
	public static ElabThresholdKey byClass(Class<?> elabClass) {
		if(elabClass == null) {
			throw new IllegalArgumentException("The class to search for must not be null");
		}
		String name = elabClass.getName();
		// CGLIB
		int cglibIndex = name.indexOf("$$EnhancerByCGLIB$$");
		if(cglibIndex > -1) {
			name = name.substring(0, cglibIndex);
		}
		
		for(ElabThresholdKey etk : ElabThresholdKey.values()) {
			// Confronto via name e non via class per evitare problemi dovuti a classi caricate da differenti ClassLoader
			if(etk.elabClassName.equals(name)) {
				return etk;
			}
		}
		return null;
	}

}
