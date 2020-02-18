/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * The Class TipoBeneCespitiCategoriaCespitiConverter.
 *
 * @author Anto
 */
public abstract class BaseTipoBeneCespiteCausaleEPConverter extends ExtendedDozerConverter<TipoBeneCespite, SiacDCespitiBeneTipo > {
	
	/**
	 * Instantiates a new base tipo bene cespite CausaleEP patrimoniale converter.
	 */
	public BaseTipoBeneCespiteCausaleEPConverter() {
		super(TipoBeneCespite.class, SiacDCespitiBeneTipo.class);
	}

	@Override
	public TipoBeneCespite convertFrom(SiacDCespitiBeneTipo src, TipoBeneCespite dest) {
		SiacTCausaleEp siacTCausaleEP = getSiacTCausaleEp(src);
		if(siacTCausaleEP == null) {
			//BOH
			return dest;
		}
		impostaCausaleEP(dest, siacTCausaleEP.getUid(),siacTCausaleEP.getCausaleEpCode(), siacTCausaleEP.getCausaleEpDesc());
		return dest;
		
	}

	@Override
	public SiacDCespitiBeneTipo convertTo(TipoBeneCespite src, SiacDCespitiBeneTipo dest) {
		CausaleEP causaleEP = getCausaleEP(src);
		if(causaleEP == null || causaleEP.getUid() == 0) {
			return dest;
		}
		
		impostaSiacTCausaleEp(dest, causaleEP.getUid(), causaleEP.getCodice(), causaleEP.getDescrizione() );
		
		return dest;
	}

	
	protected abstract void impostaSiacTCausaleEp(SiacDCespitiBeneTipo dest, int uid, String causaleEpCode, String descausaleEpCoderizione);

	protected abstract CausaleEP getCausaleEP(TipoBeneCespite tipoBene);
	
	protected abstract SiacTCausaleEp getSiacTCausaleEp(SiacDCespitiBeneTipo siacDCespitiBeneTipo);
	
	protected abstract void impostaCausaleEP(TipoBeneCespite dest, int uid, String codice, String descrizione);

}
