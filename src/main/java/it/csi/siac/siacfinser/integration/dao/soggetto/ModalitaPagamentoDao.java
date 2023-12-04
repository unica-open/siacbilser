/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;

public interface ModalitaPagamentoDao extends Dao<SiacTModpagFin, Integer>
{
	Integer findModalitaPagamentoByAccreditoTipoId(Integer idSoggetto, Integer idEnte, Integer accreditoTipoId);

	Integer findModalitaPagamentoContoCorrente(Integer idSoggetto, String iban, Integer idEnte);

	Integer findAccreditoTipoIdByGestioneLivello(Integer idEnte, String codiceGruppoAccredito);

	Integer findModalitaPagamentoGiroFondi(Integer idSoggetto, String numeroConto, Integer idEnte,
			Integer accreditoTipoId);

	Integer insertModalitaPagamentoGiroFondi(Integer idSoggetto, String numeroConto, Integer idEnte, String login);

	Integer insertModalitaPagamentoContoCorrente(Integer idSoggetto, String iban, Integer idEnte, String login);

	Integer insertModalitaPagamentoContanti(Integer idSoggetto, String quietanzante,
			String codiceFiscaleQuietanzante, Integer idEnte, String login);

}
