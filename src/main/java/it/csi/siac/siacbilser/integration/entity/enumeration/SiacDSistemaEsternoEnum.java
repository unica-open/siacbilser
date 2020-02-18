/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import it.csi.siac.siacbilser.integration.entity.SiacDSistemaEsterno;
import it.csi.siac.siacfin2ser.model.SistemaEsterno;


/**
 * Mapping tra l'entity {@link SiacDSistemaEsterno} e l'enum di modello {@link SistemaEsterno}.
 *
 * @author Domenico
 */
@EnumEntity(entityName="SiacDSistemaEsterno", idPropertyName="extsys_id", codePropertyName="extsysCode")
public enum SiacDSistemaEsternoEnum {
	
	FEL("FLUSSO_FEL", SistemaEsterno.FEL),
	ATTIAMM("FLUSSO_ATTIAMM", SistemaEsterno.ATTIAMM),
//	STIPE("FLUSSO_STIPE", SistemaEsterno.STIPE),
//	DOC("FLUSSO_DOC", SistemaEsterno.DOC);
	
	PCC("PCC", SistemaEsterno.PCC), //utilizzato per ricavare l'IdentificativoPCCAmministrazione per l'invio ai servizi di PCC.
	;

	private final String codice;
	private final SistemaEsterno sistemaEsterno;

	
	/**
	 * Instantiates a new siac d sistema esterno enum.
	 *
	 * @param codice the codice
	 * @param sistemaEsterno the sistema esterno
	 */
	SiacDSistemaEsternoEnum(String codice, SistemaEsterno sistemaEsterno){
		this.codice = codice;
		this.sistemaEsterno = sistemaEsterno;
	}
	
	/**
	 * By codice even null.
	 *
	 * @param codice the codice
	 * @return the siac d sistema esterno enum
	 */
	public static SiacDSistemaEsternoEnum byCodiceEvenNull(String codice){
		if(codice==null){
			return null;
		}
		return byCodice(codice);
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d atto amm tipo enum
	 */
	public static SiacDSistemaEsternoEnum byCodice(String codice){
		for(SiacDSistemaEsternoEnum e : SiacDSistemaEsternoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDSistemaEsternoEnum");
	}
	
	/**
	 * By sistema esterno.
	 *
	 * @param sistemaEsterno the sistema esterno
	 * @return the siac d sistema esterno enum
	 */
	public static SiacDSistemaEsternoEnum bySistemaEsterno(SistemaEsterno sistemaEsterno){
		for(SiacDSistemaEsternoEnum e : SiacDSistemaEsternoEnum.values()){
			if(e.getSistemaEsterno().equals(sistemaEsterno)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il sistema esterno "+ sistemaEsterno + " non ha un mapping corrispondente in SiacDSistemaEsternoEnum");
	}
	
	/**
	 * By sistemi esterni.
	 *
	 * @param stati the stati
	 * @return the enum set
	 */
	public static Set<SiacDSistemaEsternoEnum> bySistemiEsterni(Collection<SistemaEsterno> stati) {
		return bySistemiEsterni(stati!=null && !stati.isEmpty()?EnumSet.copyOf(stati):null);
	}
	
	/**
	 * By sistemi esterni.
	 *
	 * @param stati the stati
	 * @return the enum set
	 */
	public static Set<SiacDSistemaEsternoEnum> bySistemiEsterni(Set<SistemaEsterno> stati) {
		Set<SiacDSistemaEsternoEnum> enums = EnumSet.noneOf(SiacDSistemaEsternoEnum.class);
		if(stati!=null){
			for(SistemaEsterno stato: stati){
				enums.add(bySistemaEsterno(stato));
			}
		}
		
		return enums;
	}

	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * Gets the descrizione.
	 *
	 * @return the descrizione
	 */
	public SistemaEsterno getSistemaEsterno() {
		return sistemaEsterno;
	}
	
}