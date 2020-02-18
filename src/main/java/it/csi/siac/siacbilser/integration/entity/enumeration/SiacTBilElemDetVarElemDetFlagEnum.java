/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacTBilElemDetVarElemDetFlagEnum.
 */
public enum SiacTBilElemDetVarElemDetFlagEnum {
		
	/** The Sorgente. */
	Sorgente("S"), 		//Indica che il capitolo è Sorgente di una variazione di Storno
	/** The Destinazione. */
	Destinazione("D"),	//Indica che il capitolo è Destinazione di una variazione di Storno
	/** The Da annullare. */
	DaAnnullare("A"), 	//Indica che il capitolo che rientra nella variazione è da annullare.
	/** The Nuovo. */
	 Nuovo("N");			//Indica che il capitolo che rientra nella variazione è nuovo.
	
	
	/** The codice. */
	private final String codice;
	
	
	/**
	 * Instantiates a new siac t bil elem det var elem det flag enum.
	 *
	 * @param codice the codice
	 */
	SiacTBilElemDetVarElemDetFlagEnum(String codice){		
		this.codice = codice;
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
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac t bil elem det var elem det flag enum
	 */
	public static SiacTBilElemDetVarElemDetFlagEnum byCodice(String codice){
		for(SiacTBilElemDetVarElemDetFlagEnum e : SiacTBilElemDetVarElemDetFlagEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacTBilElemDetVarElemDetFlagEnum");
	}
	
	/**
	 * By codice even null.
	 *
	 * @param codice the codice
	 * @return the siac t bil elem det var elem det flag enum
	 */
	public static SiacTBilElemDetVarElemDetFlagEnum byCodiceEvenNull(String codice){		
		if(codice==null){
			return null;
		}
		
		return byCodice(codice);
	}
	
	
	
	/**
	 * Evaluate flag.
	 *
	 * @param dettVic the dett vic
	 * @return the siac t bil elem det var elem det flag enum
	 */
	public static SiacTBilElemDetVarElemDetFlagEnum evaluateFlag(DettaglioVariazioneImportoCapitolo dettVic) {
		if(Boolean.TRUE.equals(dettVic.getFlagAnnullaCapitolo())){
			return SiacTBilElemDetVarElemDetFlagEnum.DaAnnullare;
		}
		
		if(Boolean.TRUE.equals(dettVic.getFlagNuovoCapitolo())){
			return SiacTBilElemDetVarElemDetFlagEnum.Nuovo;
		}
		
		return null;
	}
	
	
	/**
	 * Sets the flag.
	 *
	 * @param dettVic the dett vic
	 * @param codice the codice
	 */
	public static void setFlag(DettaglioVariazioneImportoCapitolo dettVic, String codice) {
		if(SiacTBilElemDetVarElemDetFlagEnum.DaAnnullare.getCodice().equals(codice)){
			dettVic.setFlagAnnullaCapitolo(Boolean.TRUE);
			return;
		}
		
		if(SiacTBilElemDetVarElemDetFlagEnum.Nuovo.getCodice().equals(codice)){
			dettVic.setFlagNuovoCapitolo(Boolean.TRUE);
			return;
		}
		
	}
	

}
