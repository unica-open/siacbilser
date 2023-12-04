/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;


/**
 * Descrive il mapping dell codifiche.
 * 
 * @author Valentina
 */
@EnumEntity(entityName="SiacDEventoTipo", idPropertyName="eventoTipoId", codePropertyName="eventoTipoCode")
public enum SiacDEventoTipoEnum {
	
	Impegno("I", SiacDEventoFamTipoEnum.Spesa),
	Accertamento("A", SiacDEventoFamTipoEnum.Entrata),
	Liquidazione("L", SiacDEventoFamTipoEnum.Spesa ),
	DocumentoEntrata("DE", SiacDEventoFamTipoEnum.Entrata),
	DocumentoSpesa("DS", SiacDEventoFamTipoEnum.Spesa),
	OrdinativoPagamento("OP", SiacDEventoFamTipoEnum.Spesa),
	OrdinativoIncasso("OI", SiacDEventoFamTipoEnum.Entrata),
	
	PrimoNota("P", null),
	Epilogativo("EP", null),
	ContoOrdine("CO", null),
	Giroconti("GI", null),
	Ammortamenti("AM", null),
	Riscontri("RI", null),
	Ratei("RA", null),
	Riscontri1("RS", null),
	Ratei1("RT", null),

	
	Sconti("ST", null),
	
	RichiestaEconomale("EC",SiacDEventoFamTipoEnum.Spesa),
	// SIAC-5344
	Extr("EXTR", null),
	// Cespiti
	Inventario2ContabilitaGenerale("INV-COGE", null),
	ContabilitaGenerale2Inventario("COGE-INV", null),

	;
	
	private static LogSrvUtil log = new LogSrvUtil(SiacDEventoTipoEnum.class);
	private final String eventoTipoCode;
	private final SiacDEventoFamTipoEnum siacDEventoFamTipoEnum;
	
	
	
	/**
	 * 
	 * @param modelClass Classe di Model
	 * @param entityClass Classe di Entitty (es: SiacD*.class)
	 * @param idColumnName nome jpql della colonna contenente l'id
	 * @param mapId mapId per il mapping di Dozer
	 */
	private SiacDEventoTipoEnum(String eventoTipoCode, SiacDEventoFamTipoEnum siacDEventoFamTipoEnum) {
		this.eventoTipoCode = eventoTipoCode;
		this.siacDEventoFamTipoEnum = siacDEventoFamTipoEnum;
	}
	
	
	public static SiacDEventoTipoEnum byEventoTipoCodeEvenNull(String eventoTipoCode){
		final String methodName = "byEventoTipoCodeEvenNull";
		if(eventoTipoCode==null){
			return null;
		}
		try{
			return byEventoTipoCode(eventoTipoCode);
		} catch (IllegalArgumentException iae) {
			log.warn(methodName, iae.getMessage() + " Returning null.");
			return null;
		}
	}
	
	
	public static SiacDEventoTipoEnum byEventoTipoCode(String eventoTipoCode){
		for(SiacDEventoTipoEnum ce : SiacDEventoTipoEnum.values()){
			if(ce.getEventoTipoCode().equals(eventoTipoCode)){
				return ce;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping per il codice: "+ eventoTipoCode + " in SiacDEventoTipoEnum.");
	}
	
	/**
	 * By evento tipo codes.
	 *
	 * @param eventoTipoCodes the evento tipo codes
	 * @return the list
	 */
	public static List<SiacDEventoTipoEnum> byEventoTipoCodes(String... eventoTipoCodes) {
		List<SiacDEventoTipoEnum> siacDEventoTipoEnums = new ArrayList<SiacDEventoTipoEnum>();
		for (String eventoTipoCode : eventoTipoCodes) {
			siacDEventoTipoEnums.add(byEventoTipoCode(eventoTipoCode));
		}
		return siacDEventoTipoEnums;
		
	}
	
	/**
	 * @return the eventoTipoCode
	 */
	public String getCodice() {
		return eventoTipoCode;
	}
	
	/**
	 * @return the eventoTipoCode
	 */
	public String getEventoTipoCode() {
		return eventoTipoCode;
	}

	/**
	 * @return the siacDEventoFamTipoEnum
	 */
	public SiacDEventoFamTipoEnum getSiacDEventoFamTipoEnum() {
		return siacDEventoFamTipoEnum;
	}


}
