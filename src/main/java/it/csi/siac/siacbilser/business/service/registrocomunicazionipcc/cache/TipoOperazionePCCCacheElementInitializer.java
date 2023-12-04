/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.cache;

import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC.Value;

public class TipoOperazionePCCCacheElementInitializer implements CacheElementInitializer<TipoOperazionePCC.Value, TipoOperazionePCC> {

	private final RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	
	public TipoOperazionePCCCacheElementInitializer(RegistroComunicazioniPCCDad registroComunicazioniPCCDad) {
		this.registroComunicazioniPCCDad = registroComunicazioniPCCDad;
	}

	@Override
	public TipoOperazionePCC initialize(Value key) {
		TipoOperazionePCC tipoOperazionePCC = registroComunicazioniPCCDad.findTipoOperazionePCCByValue(key);
		if(tipoOperazionePCC == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Tipo operazione PCC", "codice " + key.name()));
		}
		return tipoOperazionePCC;
	}

}
