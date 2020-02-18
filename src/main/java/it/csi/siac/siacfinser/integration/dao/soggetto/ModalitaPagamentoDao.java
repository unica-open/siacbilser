/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;

public interface ModalitaPagamentoDao extends Dao<SiacTModpagFin, Integer>
{
	public Integer findModalitaPagamentoContoCorrente(Integer idSoggetto, String iban, Integer idEnte);

	public Integer insertModalitaPagamentoContoCorrente(Integer idSoggetto, String iban, Integer idEnte, String login);

	public Integer findModalitaPagamentoByAccreditoTipoId(Integer idSoggetto, Integer idEnte, Integer accreditoTipoId);

	public Integer findAccreditoTipoIdByGestioneLivello(Integer idEnte, String codiceGruppoAccredito);

	public Integer insertModalitaPagamentoContanti(Integer idSoggetto, String quietanzante,
			String codiceFiscaleQuietanzante, Integer idEnte, String login);
}
