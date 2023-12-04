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
 * The Class TipoBeneCespiteEventoDecremento.
 *
 * @author Antonino
 */
public class TipoBeneCespiteCausaleEPDecrementoConverter extends BaseTipoBeneCespiteCausaleEPConverter {
	
	
	@Override
	protected void impostaSiacTCausaleEp(SiacDCespitiBeneTipo dest, int uid, String causaleEpCode, String causaleEpDescrizione) {
		SiacTCausaleEp siacTCausaleEp = new SiacTCausaleEp();
		siacTCausaleEp.setUid(uid);
		dest.setSiacTCausaleEpDecremento(siacTCausaleEp);
		dest.setEventoDecrementoCode(causaleEpCode);
		dest.setEventoDecrementoDesc(causaleEpDescrizione);
	}

	@Override
	protected CausaleEP getCausaleEP(TipoBeneCespite tipoBene) {
		return tipoBene.getCausaleDecremento();
	}

	@Override
	protected SiacTCausaleEp getSiacTCausaleEp(SiacDCespitiBeneTipo siacDCespitiBeneTipo) {
		return siacDCespitiBeneTipo.getSiacTCausaleEpDecremento();
	}

	@Override
	protected void impostaCausaleEP(TipoBeneCespite dest, int uid, String codice, String descrizione) {
		CausaleEP causale = new CausaleEP();
		causale.setUid(uid);
		causale.setCodice(codice);
		causale.setDescrizione(descrizione);
		dest.setCausaleDecremento(causale);
		
	}

}
