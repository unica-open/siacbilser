/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacboser.integration.dao.limiteimpegnabile;

import java.math.BigDecimal;
import java.util.Map;

public interface LimiteImpegnabileDao
{
	public Integer leggiCapitolo(String numeroCapitolo, String numeroArticolo, String numeroUeb, String anno,
			Integer idEnte);

//	public Integer inserisciCapitolo(String numeroCapitolo, String numeroArticolo, String numeroUeb, String anno,
//			Integer idEnte, String loginOperazione);

	public Map<String, Object> leggiImportoCapitolo(Integer idCapitolo, String anno, String codiceTipoImporto, Integer idEnte);

	public void inserisciImportoCapitolo(Integer idCapitolo, BigDecimal importo, String anno, Integer idEnte,
			String loginOperazione);

	public void aggiornaImportoCapitolo(Integer idCapitolo, BigDecimal importo, String loginOperazione);
}
