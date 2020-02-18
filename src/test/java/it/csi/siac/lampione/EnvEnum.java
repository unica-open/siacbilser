/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.lampione;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public enum EnvEnum {
	
	CONSIP("10267", "tst-jboss1-contabilia-consip.bilancio.csi.it",
			"10268", "tst-jboss2-contabilia-consip.bilancio.csi.it"), 
	TST_SKED("10146", "tst-sked1.bilancio.csi.it");

	private Map<String, String> envMap = new LinkedHashMap<String, String>();

	EnvEnum(String... args) {
		for (int i = 0; i < args.length;) {
			envMap.put(args[i++], args[i++]);
		}
	}
	
	public Set<Entry<String, String>> getHosts() {
		return envMap.entrySet();
	}
	
}
