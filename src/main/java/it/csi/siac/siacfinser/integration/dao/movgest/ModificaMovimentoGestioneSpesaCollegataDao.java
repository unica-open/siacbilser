/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;

public interface ModificaMovimentoGestioneSpesaCollegataDao extends Dao<ModificaMovimentoGestioneSpesaCollegata, SiacRMovgestTsDetModFin> {
	
	List<SiacRMovgestTsDetModFin> trovaModificaMovimentoGestioneCollegateAdAccertamento(
			Integer uidAccertamento, Integer uidModifica, boolean escludiModificheEntrataAnnullate);
	
	List<SiacTModificaFin> caricaModificheMovimentoGestionePerDatiDefaultVincoloEsplicito(
			Integer uidAccertamento);
	
	List<SiacTModificaFin> caricaModificheMovimentoGestionePerDatiDefaultVincoloImplicito(
			Integer bilElemId, List<String> macroTipoCodes, Integer idEnte);
	
	/**
	 * [0] => importoResiduoCollegare, [1] => importoMaxCollegabile
	 * @param uidModifica
	 * @param uidAccertamento 
	 * @return List<BigDecimal>
	 */
	List<BigDecimal> caricaImportiModificaSpesaCollegataDefault(int uidModifica, Integer uidAccertamento);

	BigDecimal caricaImportoResiduoCollegare(Integer uidModifica);

	BigDecimal caricaImportoMassimoCollegabileDefault(Integer uidModifica,Integer uidAccertamento);

}
