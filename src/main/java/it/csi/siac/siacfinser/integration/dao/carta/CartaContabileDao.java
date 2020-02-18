/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.carta;

import java.util.List;

import javax.persistence.Query;

import it.csi.siac.siaccommonser.integration.dao.base.BaseDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaCartaContabileParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontFin;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;

public interface CartaContabileDao extends BaseDao {
	public List<SiacTCartacontFin> ricercaCarteContabili(RicercaCartaContabileParamDto params, DatiOperazioneDto datiOperazione, int numeroPagina, int numeroRisultatiPerPagina);
	public Query creaQueryRicercaCarteContabili(RicercaCartaContabileParamDto params, DatiOperazioneDto datiOperazione, boolean soloCount);
	public Long contaCarteContabili(RicercaCartaContabileParamDto params, DatiOperazioneDto datiOperazione);
	public SiacTCartacontFin ricercaCartaContabile(RicercaCartaContabileK pk, Integer idEnte, DatiOperazioneDto datiOperazione);
	public List<Integer> soloSubDocNonCollegatiACarte(List<Integer> subdocIdList);
}
