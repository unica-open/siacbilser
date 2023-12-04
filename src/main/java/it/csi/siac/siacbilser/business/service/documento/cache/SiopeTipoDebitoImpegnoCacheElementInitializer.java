/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento.cache;

import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

public class SiopeTipoDebitoImpegnoCacheElementInitializer implements CacheElementInitializer<String, SiopeTipoDebito> {
	
	private final ImpegnoBilDad impegnoBilDad;
	
	public SiopeTipoDebitoImpegnoCacheElementInitializer(ImpegnoBilDad impegnoBilDad) {
		this.impegnoBilDad = impegnoBilDad;
	}
	
	@Override
	public SiopeTipoDebito initialize(String key) {
		// La chiave e' del tipo <I|SI>_uid, ove I = impegno, SI = subimpegno
		Impegno i = null;
		SubImpegno si = null;
		
		String[] chunks = key.split("_");
		int uid = Integer.parseInt(chunks[1]);
		
		if("I".equals(chunks[0])) {
			i = new Impegno();
			i.setUid(uid);
		} else if("SI".equals(chunks[0])) {
			si = new SubImpegno();
			si.setUid(uid);
		}
		
		return impegnoBilDad.findSiopeTipoDebito(i, si);
	}

}
