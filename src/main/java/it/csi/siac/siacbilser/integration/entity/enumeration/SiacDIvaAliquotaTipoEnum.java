/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.AliquotaIvaTipo;

/**
 * The Enum SiacDIvaAliquotaTipoEnum.
 * 
 * @author Domenico
 */
@EnumEntity(entityName="SiacDIvaAliquotaTipo", idPropertyName="ivaaliquotaTipoId", codePropertyName="ivaaliquotaTipoCode")
public enum SiacDIvaAliquotaTipoEnum {
		
	
	ISTITUZIONALE("I", AliquotaIvaTipo.ISTITUZIONALE), 
	PROMISQUA("P", AliquotaIvaTipo.PROMISQUA), 
	COMMERCIALE("C", AliquotaIvaTipo.COMMERCIALE),
	;
	
	private final String codice;
	private final AliquotaIvaTipo aliquotaIvaTipo;
 
	private SiacDIvaAliquotaTipoEnum(String codice, AliquotaIvaTipo aliquotaIvaTipo){
		this.codice = codice;
		this.aliquotaIvaTipo = aliquotaIvaTipo;
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
	 * @return the aliquotaIvaTipo
	 */
	public AliquotaIvaTipo getAliquotaIvaTipo() {
		return aliquotaIvaTipo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDIvaAliquotaTipoEnum byCodice(String codice){
		for(SiacDIvaAliquotaTipoEnum e : SiacDIvaAliquotaTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDIvaAliquotaTipoEnum");
	}
	
	

	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d doc stato enum
	 */
	public static SiacDIvaAliquotaTipoEnum byAliquotaIvaTipo(AliquotaIvaTipo aliquotaIvaTipo){
		for(SiacDIvaAliquotaTipoEnum e : SiacDIvaAliquotaTipoEnum.values()){
			if(e.getAliquotaIvaTipo().equals(aliquotaIvaTipo)){
				return e;
			}
		}
		throw new IllegalArgumentException("L'aliquotaIvaTipo "+ aliquotaIvaTipo + " non ha un mapping corrispondente in SiacDIvaAliquotaTipoEnum");
	}
	
	public static SiacDIvaAliquotaTipoEnum byAliquotaIvaTipoEvenNull(AliquotaIvaTipo ambito){
		if(ambito == null){
			return null;
		}
		return byAliquotaIvaTipo(ambito);
	}
	
	
	

}
