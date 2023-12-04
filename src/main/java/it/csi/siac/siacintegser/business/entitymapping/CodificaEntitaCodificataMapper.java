/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.entitymapping;

import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siacintegser.model.base.EntitaCodificataBase;

public class CodificaEntitaCodificataMapper  {

	public static <C extends Codifica, EC extends EntitaCodificataBase> C map(EC entitaCodificata, Class<C> cls) {
		if (entitaCodificata == null) {
			return null;
		}
		
		C codifica = ReflectionUtil.silentlyBuildInstance(cls);
		codifica.setCodice(entitaCodificata.getCodice());
		codifica.setDescrizione(entitaCodificata.getDescrizione());
		return codifica;
	}
}
