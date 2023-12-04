/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDOrdinativoStato;
import it.csi.siac.siacbilser.integration.entity.SiacROrdinativoQuietanza;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativo;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOrdinativoStatoEnum;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siaccommon.util.collections.Filter;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTOrdinativoHelper extends SiacTEnteBaseHelper {

	public SiacTSoggetto getSiacTSoggetto(SiacTOrdinativo o1) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(o1.getSiacROrdinativoSoggettos()).getSiacTSoggetto());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public SiacTAttoAmm getSiacTAttoAmm(SiacTOrdinativo o1) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(o1.getSiacROrdinativoAttoAmms()).getSiacTAttoAmm());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public SiacDOrdinativoStato getSiacDOrdinativoStato(SiacTOrdinativo o1) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(o1.getSiacROrdinativoStatos()).getSiacDOrdinativoStato());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public SiacROrdinativoQuietanza getSiacROrdinativoQuietanza(SiacTOrdinativo o1) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(o1.getSiacROrdinativoQuietanzas()));
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public ValidNotAnnullatoSiacTOrdinativoFilter getValidNotAnnullatoSiacTOrdinativoFilter() {
		return new ValidNotAnnullatoSiacTOrdinativoFilter();
	}
	
	private class ValidNotAnnullatoSiacTOrdinativoFilter implements Filter<SiacTOrdinativo> {

		@Override
		public boolean isAcceptable(SiacTOrdinativo source) {
			return 
				EntityUtil.isValid(source) && 
				!SiacDOrdinativoStatoEnum.Annullato.getCodice().equals(getSiacDOrdinativoStato(source).getOrdinativoStatoCode());
		}
	}

}
