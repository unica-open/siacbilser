/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.threadlocal;

import it.csi.siac.siaccommon.util.cache.Cache;
import it.csi.siac.siaccommon.util.cache.MapCache;
import it.csi.siac.siacfinser.model.Impegno;

public class ImpegnoCacheThreadLocal extends ThreadLocal<Cache<Integer, Impegno>> {
	
	@Override
	protected Cache<Integer, Impegno> initialValue() {
		return new MapCache<Integer, Impegno>();
	}
	
}
