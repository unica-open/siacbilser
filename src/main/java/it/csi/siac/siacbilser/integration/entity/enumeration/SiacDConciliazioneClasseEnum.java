/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * The Enum SiacDConciliazioneClasseEnum.
 */
@EnumEntity(entityName="SiacDConciliazioneClasse", idPropertyName="concclaId", codePropertyName="concclaCode")
public enum SiacDConciliazioneClasseEnum {
	
	Costi("COSTI", ClasseDiConciliazione.COSTI),
	Ricavi("RICAVI", ClasseDiConciliazione.RICAVI),
	Crediti("CREDITI", ClasseDiConciliazione.CREDITI),
	Debiti("DEBITI", ClasseDiConciliazione.DEBITI),
	Conti("CONTI", ClasseDiConciliazione.CONTI),
	;
	
	private final String codice;
	private final ClasseDiConciliazione classeDiConciliazione;
	
	private SiacDConciliazioneClasseEnum(String codice, ClasseDiConciliazione classeDiConciliazione){
		this.codice = codice;
		this.classeDiConciliazione = classeDiConciliazione;
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
	 * @return the classeDiConciliazione
	 */
	public ClasseDiConciliazione getClasseDiConciliazione() {
		return classeDiConciliazione;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDConciliazioneClasseEnum byCodice(String codice){
		for(SiacDConciliazioneClasseEnum e : SiacDConciliazioneClasseEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDAmbitoEnum");
	}
	
	

	/**
	 * By classe di conciliazione
	 *
	 * @param classeDiConciliazione the classe di conciliazione
	 * @return the siac d conciliazioneClasseEnum
	 */
	public static SiacDConciliazioneClasseEnum byClasseDiConciliazione(ClasseDiConciliazione classeDiConciliazione){
		for(SiacDConciliazioneClasseEnum e : SiacDConciliazioneClasseEnum.values()){
			if(e.getClasseDiConciliazione().equals(classeDiConciliazione)){
				return e;
			}
		}
		throw new IllegalArgumentException("La classe di conciliazione "+ classeDiConciliazione + " non ha un mapping corrispondente in SiacDConciliazioneClasseEnum");
	}
	
	public static SiacDConciliazioneClasseEnum byClasseDiConciliazioneEvenNull(ClasseDiConciliazione classeDiConciliazione){
		if(classeDiConciliazione == null){
			return null;
		}
		return byClasseDiConciliazione(classeDiConciliazione);
	}
	
	
	

}
