/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import it.csi.siac.siacbilser.integration.entity.PagopaDRiconciliazioneErrore;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

public interface PagopaErroreDao  extends Dao<PagopaDRiconciliazioneErrore, Integer>{
	
	List<PagopaDRiconciliazioneErrore> findPagopaErrore(Integer idEnte, Integer idErrore); 
}
