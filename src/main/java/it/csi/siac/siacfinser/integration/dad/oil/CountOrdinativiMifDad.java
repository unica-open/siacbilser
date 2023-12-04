/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad.oil;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfin2ser.model.TipoOrdinativo;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CountOrdinativiMifDad extends BaseCountOrdinativiMifDad
{
	public int countOrdinativi(int idElaborazione)
	{
		TipoOrdinativo codiceTipo = getCodiceTipo(idElaborazione);
		
		switch (codiceTipo)
		{
		case INCASSO:
			return ordinativoMifDao.countOrdinativiEntrata(idElaborazione);
		case PAGAMENTO:
			return ordinativoMifDao.countOrdinativiSpesa(idElaborazione);
		}

		throw new IllegalArgumentException("Codice tipo errato: " + codiceTipo);
	}
}
