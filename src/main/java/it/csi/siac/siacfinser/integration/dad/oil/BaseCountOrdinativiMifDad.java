/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad.oil;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siacfin2ser.model.TipoOrdinativo;
import it.csi.siac.siacfinser.integration.dao.oil.OrdinativoMifDao;

public abstract class BaseCountOrdinativiMifDad extends BaseDadImpl
{
	@Autowired
	protected OrdinativoMifDao ordinativoMifDao;

	protected TipoOrdinativo getCodiceTipo(int idElaborazione)
	{
		return ordinativoMifDao.getCodiceTipo(idElaborazione);
	}

}
