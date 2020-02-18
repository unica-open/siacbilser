/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacgenser.model.Operazione;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.OperazioneTipoImporto;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoConto;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoImporto;

/**
 * The Enum SiacDOperazioneEpEnum.
 */
/*
 * select oper_ep_tipo_code || oper_ep_code, d.oper_ep_desc from siac_d_operazione_ep d, siac_d_operazione_ep_tipo dt
 * where d.oper_ep_tipo_id = dt.oper_ep_tipo_id
 * and d.ente_proprietario_id = 1
 * order by d.oper_ep_id
 */
//"SELECT "+idPropertyName+" FROM "+entityName+" WHERE "+codePropertyName+" = :code AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ", Integer.class);
@EnumEntity(entityName = "SiacDOperazioneEp", idPropertyName = "operEpId", codePropertyName = "operEpCode", codePropertyNameJpql = "CONCAT(siacDOperazioneEpTipo.operEpTipoCode, '_', operEpCode)")
public enum SiacDOperazioneEpEnum {
	
	SegnoContoDare					("SEGNOCONTO_DARE","Dare", OperazioneSegnoConto.DARE),
	SegnoContoAvere					("SEGNOCONTO_AVERE","Avere", OperazioneSegnoConto.AVERE),
	TipoImportoLordo				("TIPOIMPORTO_LORDO","Lordo", OperazioneTipoImporto.LORDO),
	TipoImportoImposta				("TIPOIMPORTO_IMPOSTA","Imposta", OperazioneTipoImporto.IMPOSTA),
	TipoImportoImponibile			("TIPOIMPORTO_IMPONIBILE","Imponibile", OperazioneTipoImporto.IMPONIBILE),
	UtilizzoContoProposto			("UTILIZZOCONTO_PROPOSTO","Proposto", OperazioneUtilizzoConto.PROPOSTO),
	UtilizzoContoObbligatorio		("UTILIZZOCONTO_OBBLIGATORIO","Obbligatorio", OperazioneUtilizzoConto.OBBLIGATORIO),
	UtilizzoImportoProposto			("UTILIZZOIMPORTO_PROPOSTO","Proposto", OperazioneUtilizzoImporto.PROPOSTO),
	UtilizzoImportoNonModificabile	("UTILIZZOIMPORTO_NONMODIFICABILE","Non modificabile", OperazioneUtilizzoImporto.NON_MODIFICABILE),
	

	;
	
	private final String codice;
	private final String descrizione;
	private final Operazione operazione;

	

	private SiacDOperazioneEpEnum(String codice, String descrizione, Operazione operazione) {
		this.codice = codice;
		this.descrizione = descrizione;
		this.operazione = operazione;
	}
	
	public static SiacDOperazioneEpEnum byOperazione(Operazione operazione) {
		for (SiacDOperazioneEpEnum e : SiacDOperazioneEpEnum.values()) {
			if (e.getOperazione().equals(operazione)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Operazione " + operazione + " non ha un mapping corrispondente in SiacDOperazioneEpEnum");
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d atto legge tipo enum
	 */
	public static SiacDOperazioneEpEnum byCodice(String codice) {
		for (SiacDOperazioneEpEnum e : SiacDOperazioneEpEnum.values()) {
			if (e.getCodice().equals(codice)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Codice " + codice + " non ha un mapping corrispondente in SiacDOperazioneEpEnum");
	}
	
	/**
	 * By descrizione.
	 *
	 * @param descrizione the descrizione
	 * @return the siac d atto legge tipo enum
	 */
	public static SiacDOperazioneEpEnum byDescrizione(String descrizione) {
		for (SiacDOperazioneEpEnum e : SiacDOperazioneEpEnum.values()) {
			if (e.getDescrizione().equals(descrizione)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Descrizione " + descrizione + " non ha un mapping corrispondente in SiacDOperazioneEpEnum");
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
	public String getDescrizione() {
		return descrizione;
	}
	
	/**
	 * @return the operazione
	 */
	public Operazione getOperazione() {
		return operazione;
	}



}