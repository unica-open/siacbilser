/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.Periodo;


// TODO: Auto-generated Javadoc
/**
 * The Enum SiacTAttrEnum.
 */
@EnumEntity(entityName="SiacDPeriodoTipo", idPropertyName="periodoTipoId", codePropertyName="periodoTipoCode")
public enum SiacDPeriodoTipoEnum {
	
	
	
	ANNO("SY", Periodo.ANNO),
	
	//Mensile
	GENNAIO		("M01", Periodo.GENNAIO),
	FEBBRAIO	("M02", Periodo.FEBBRAIO),
	MARZO		("M03", Periodo.MARZO),
	APRILE		("M04", Periodo.APRILE),
	MAGGIO		("M05", Periodo.MAGGIO),
	GIUGNO		("M06", Periodo.GIUGNO),
	LUGLIO		("M07", Periodo.LUGLIO),
	AGOSTO		("M08", Periodo.AGOSTO),
	SETTEMBRE	("M09", Periodo.SETTEMBRE),
	OTTOBRE		("M10", Periodo.OTTOBRE),
	NOVEMBRE	("M11", Periodo.NOVEMBRE),
	DICEMBRE	("M12", Periodo.DICEMBRE),
	
	//Trimestrale
	GENNAIO_MARZO		("T01", Periodo.GENNAIO_MARZO),
	APRILE_GIUGNO		("T02", Periodo.APRILE_GIUGNO),
	LUGLIO_SETTEMBRE	("T03", Periodo.LUGLIO_SETTEMBRE),
	OTTOBRE_DICEMBRE	("T04", Periodo.OTTOBRE_DICEMBRE),
	
	//Quadrimestrale
	GENNAIO_APRILE		("Q01", Periodo.GENNAIO_APRILE),
	MAGGIO_AGOSTO		("Q02", Periodo.MAGGIO_AGOSTO),
	SETTEMBRE_DICEMBRE	("Q03", Periodo.SETTEMBRE_DICEMBRE),
	
	//Semestrale
	GENNAIO_GIUGNO	("S01", Periodo.GENNAIO_GIUGNO),
	LUGLIO_DICEMBRE	("S02", Periodo.LUGLIO_DICEMBRE),
	;
	
	
	
	/** The codice. */
	private final String codice;
	
	/** The model field name. */
	private final Periodo periodo;

	

	/**
	 * Instantiates a new siac t attr enum.
	 *
	 * @param codice the codice
	 * @param fieldType the field type
	 * @param modelFieldName the model field name
	 * @param tipologiaAttributo the tipologia attributo
	 */
	private SiacDPeriodoTipoEnum(String codice, Periodo periodo) {
		this.codice = codice;
		this.periodo = periodo;
		
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
	 * Restiuisce, se esiste, SiacDPeriodoTipoEnum corrispondente ad un codice passato in input
	 * 
	 * @param codice
	 * @return 
	 */
	public static SiacDPeriodoTipoEnum byCodice(String codice){
		for(SiacDPeriodoTipoEnum e : SiacDPeriodoTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDPeriodoTipoEnum");
	}
	
	
	/**
	 * Restiuisce, se esiste, SiacDPeriodoTipoEnum corrispondente ad un periodo passato in input
	 * 
	 * @param periodo
	 * @return 
	 */
	public static SiacDPeriodoTipoEnum byPeriodo(Periodo periodo){
		for(SiacDPeriodoTipoEnum e : SiacDPeriodoTipoEnum.values()){
			if(e.getPeriodo().equals(periodo)){
				return e;
			}
		}
		
		throw new IllegalArgumentException("Il tipo attributo "+ periodo + " non ha un mapping corrispondente in SiacDPeriodoTipoEnum");
	}
	
	
	/**
	 * Restiuisce, se esiste, SiacDPeriodoTipoEnum corrispondente ad un periodo passato in input.
	 * Restituisce null nel caso in cui il parametro periodo sia null
	 * 
	 * @param periodo
	 * @return
	 */
	public static SiacDPeriodoTipoEnum byPeriodoEvenNull(Periodo periodo){
		if(periodo==null){
			return null;
		}
		return byPeriodo(periodo);
		
	}

	/**
	 * @return the periodo
	 */
	public Periodo getPeriodo() {
		return periodo;
	}

	

}
