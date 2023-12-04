/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.movimentogestione;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelSheet;
import it.csi.siac.siacbilser.model.mutuo.MovimentoGestioneAssociatoMutuo;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siacfinser.model.MovimentoGestione;


public abstract class MovimentiGestioneAssociatiMutuoExcelSheet
	<MGAMER extends MovimentiGestioneAssociatiMutuoExcelRow, MG extends MovimentoGestione> extends ExcelSheet<MGAMER> {

	protected void map(MovimentoGestioneAssociatoMutuo<MG> impegnoAssociatoMutuo, MGAMER riga) {
		riga.setAnno(impegnoAssociatoMutuo.getMovimentoGestione().getAnno());
		riga.setNumero(impegnoAssociatoMutuo.getMovimentoGestione().getNumero());
		riga.setStatoOperativo(impegnoAssociatoMutuo.getMovimentoGestione().getStatoOperativo().getDescrizione());
		riga.setCapitolo(impegnoAssociatoMutuo.getMovimentoGestione().getCapitolo() == null ? 
			null : impegnoAssociatoMutuo.getMovimentoGestione().getCapitolo().getAnnoCapitoloArticolo());
		riga.setDescTipoFinanziamento(impegnoAssociatoMutuo.getMovimentoGestione().getCapitolo() == null ? 
			null: impegnoAssociatoMutuo.getMovimentoGestione().getCapitolo().getTipoFinanziamento() == null ? 
				null : impegnoAssociatoMutuo.getMovimentoGestione().getCapitolo().getTipoFinanziamento().getDescrizione());
		riga.setAttoAmministrativo(impegnoAssociatoMutuo.getMovimentoGestione().getAttoAmministrativo() == null ?
			null : impegnoAssociatoMutuo.getMovimentoGestione().getAttoAmministrativo().getDescrizioneCompleta());
		riga.setSoggetto(impegnoAssociatoMutuo.getMovimentoGestione().getSoggetto() == null ? 
			null : impegnoAssociatoMutuo.getMovimentoGestione().getSoggetto().getCodiceDenominazione());
		riga.setAltriMutui(CoreUtil.getOption(impegnoAssociatoMutuo.getMovimentoGestione().getTotaleMutuiAssociati() > 1, "Sì", "No"));
		riga.setImportoAttuale(impegnoAssociatoMutuo.getMovimentoGestione().getImportoAttuale());
	}
}
