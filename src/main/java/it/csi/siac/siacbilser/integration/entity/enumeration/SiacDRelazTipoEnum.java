/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoRelazione;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDRelazTipoEnum.
 */
@EnumEntity(entityName="SiacDRelazTipo", idPropertyName="relazTipoId", codePropertyName="relazTipoCode")
public enum SiacDRelazTipoEnum {
	
	Subdocumento("SUB", TipoRelazione.SUBORDINATO),
	NotaCredito("NCD", TipoRelazione.NOTA_CREDITO),
	NotaCreditoIva("NCDI", TipoRelazione.NOTA_CREDITO_IVA),
	QuotePerIvaDifferita("QPID", TipoRelazione.QUOTE_PER_IVA_DIFFERITA),
	ControregistrazioneIntrastat("CRI", TipoRelazione.CONTROREGISTRAZIONE_INTRASTAT),
	SedeSecondaria("SEDE_SECONDARIA", TipoRelazione.SEDE_SECONDARIA),
	
	SostituzioneOrdinativo("SOS_ORD", TipoRelazione.SOSTITUZIONE_ORDINATIVO),
	OrdinativoSubordinato("SUB_ORD", TipoRelazione.ORDINATIVO_SUBORDINATO),
	;
	
	private final String codice;
	private final TipoRelazione tipoRelazione;
	
	public static final String CodiceNotaCredito = "NCD"; 
	public static final String CodiceNotaCreditoIva = "NCDI";
	

	/**
	 * Instantiates a new siac d relaz tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoRelazione the tipo relazione
	 */
	SiacDRelazTipoEnum(String codice, TipoRelazione tipoRelazione){
		this.codice = codice;
		this.tipoRelazione = tipoRelazione;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d relaz tipo enum
	 */
	public static SiacDRelazTipoEnum byCodice(String codice){
		for(SiacDRelazTipoEnum e : SiacDRelazTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Tipo relazione "+ codice + " non ha un mapping corrispondente in SiacDRelazTipoEnum");
	}
	
	/**
	 * By tipo relazione.
	 *
	 * @param tipoRelazione the tipo relazione
	 * @return the siac d relaz tipo enum
	 */
	public static SiacDRelazTipoEnum byTipoRelazione(TipoRelazione tipoRelazione){
		for(SiacDRelazTipoEnum e : SiacDRelazTipoEnum.values()){
			if(e.getTipoRelazione().equals(tipoRelazione)){
				return e;
			}
		}
		throw new IllegalArgumentException("Tipo relazione "+ tipoRelazione + " non ha un mapping corrispondente in SiacDRelazTipoEnum");
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
	 * Gets the tipo relazione.
	 *
	 * @return the tipo relazione
	 */
	public TipoRelazione getTipoRelazione() {
		return tipoRelazione;
	}
	
}