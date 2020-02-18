/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.sirfelser.model.ModalitaPagamentoFEL;

/**
 * The Enum SirfelDTipoDocumentoEnum.
 */
@EnumEntity(entityName="SirfelDModalitaPagamento", idPropertyName="doctipoId", codePropertyName="codice")
public enum SirfelDModalitaPagamentoEnum {
	
	CONTANTI("MP01", ModalitaPagamentoFEL.CONTANTI),
	ASSEGNO("MP02", ModalitaPagamentoFEL.ASSEGNO),
	ASSEGNO_CIRCOLARE("MP03",ModalitaPagamentoFEL.ASSEGNO_CIRCOLARE),
	CONTANTI_TESORERIA("MP04", ModalitaPagamentoFEL.CONTANTI_TESORERIA),
	BONIFICO("MP05", ModalitaPagamentoFEL.BONIFICO),
	VAGLIA_CAMBIARIO("MP06", ModalitaPagamentoFEL.VAGLIA_CAMBIARIO),
	BOLLETTINO_BANCARIO("MP07", ModalitaPagamentoFEL.BOLLETTINO_BANCARIO),
	CARTA_PAGAMENTO("MP08", ModalitaPagamentoFEL.CARTA_PAGAMENTO),
	RID("MP09", ModalitaPagamentoFEL.RID),
	RID_UTENZE("MP10", ModalitaPagamentoFEL.RID_UTENZE),
	RID_VELOCE("MP11", ModalitaPagamentoFEL.RID_VELOCE),
	RIBA("MP12", ModalitaPagamentoFEL.RIBA),
	MAV("MP13", ModalitaPagamentoFEL.MAV),
	QUIETANZA_ERARIO("MP14", ModalitaPagamentoFEL.QUIETANZA_ERARIO),
	GIROCONTO("MP15", ModalitaPagamentoFEL.GIROCONTO),
	DOMICILIAZIONE_BANCARIA("MP16", ModalitaPagamentoFEL.DOMICILIAZIONE_BANCARIA),
	DOMICILIAZIONE_POSTALE("MP17", ModalitaPagamentoFEL.DOMICILIAZIONE_POSTALE),
	BOLLETTINO_POSTALE("MP18", ModalitaPagamentoFEL.BOLLETTINO_POSTALE),
	SEPA("MP19", ModalitaPagamentoFEL.SEPA),
	SEPA_CORE("MP20", ModalitaPagamentoFEL.SEPA_CORE),
	SEPA_B2B("MP21", ModalitaPagamentoFEL.SEPA_B2B),
	TRATTENUTA_SOMME_RISCOSSE("MP22", ModalitaPagamentoFEL.TRATTENUTA_SOMME_RISCOSSE),
	;
	
	
	private final String codice;
	private final ModalitaPagamentoFEL mpf;
	
	/**
	 * Instantiates a new siac d subdoc iva stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SirfelDModalitaPagamentoEnum(String codice, ModalitaPagamentoFEL mpf){
		this.codice = codice;
		this.mpf = mpf;
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
	 * @return the mpf
	 */
	public ModalitaPagamentoFEL getMpf() {
		return mpf;
	}


	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return SirfelDModalitaPagamentoEnum
	 */
	public static SirfelDModalitaPagamentoEnum byCodice(String codice){
		for(SirfelDModalitaPagamentoEnum e : SirfelDModalitaPagamentoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SirfelDModalitaPagamentoEnum");
	}
	
	/**
	 * By ModalitaPagamentoFEL
	 *
	 * @param mpf ModalitaPagamentoFEL
	 * @returnSirfelDModalitaPagamentoEnum
	 */
	public static SirfelDModalitaPagamentoEnum byModalitaPagamentoFEL(ModalitaPagamentoFEL mpf){
		for(SirfelDModalitaPagamentoEnum e : SirfelDModalitaPagamentoEnum.values()){
			if(e.getMpf().getCodice().equals(mpf.getCodice())){
				return e;
			}
		}
		throw new IllegalArgumentException("La modalita' di pagamento "+ mpf + " non ha un mapping corrispondente in SirfelDModalitaPagamentoEnum");
	}
	

}
