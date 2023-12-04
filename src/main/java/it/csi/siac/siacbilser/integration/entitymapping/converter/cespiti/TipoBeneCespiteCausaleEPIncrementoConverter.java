/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * The Class TipoBeneCespiteEventoIncremento.
 *
 * @author Antonino
 */
public class TipoBeneCespiteCausaleEPIncrementoConverter extends BaseTipoBeneCespiteCausaleEPConverter {
	
	
	@Override
	protected void impostaSiacTCausaleEp(SiacDCespitiBeneTipo dest, int uid, String causaleEpCode, String causaleEpDescrizione) {
		SiacTCausaleEp siacTCausaleEp = new SiacTCausaleEp();
		siacTCausaleEp.setUid(uid);
		dest.setSiacTCausaleEpIncr(siacTCausaleEp);
		dest.setEventoIncrementoCode(causaleEpCode);
		dest.setEventoIncrementoDesc(causaleEpDescrizione);
	}

	@Override
	protected CausaleEP getCausaleEP(TipoBeneCespite tipoBene) {
		return tipoBene.getCausaleIncremento();
	}

	@Override
	protected SiacTCausaleEp getSiacTCausaleEp(SiacDCespitiBeneTipo siacDCespitiBeneTipo) {
		return siacDCespitiBeneTipo.getSiacTCausaleEpIncr();
	}

	@Override
	protected void impostaCausaleEP(TipoBeneCespite dest, int uid, String codice, String descrizione) {
		CausaleEP causale = new CausaleEP();
		causale.setUid(uid);
		causale.setCodice(codice);
		causale.setDescrizione(descrizione);
		dest.setCausaleIncremento(causale);
		
	}

}
