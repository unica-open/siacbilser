/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa.factory;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiCassaPrevidenzialeType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.RitenutaType;
import it.csi.siac.sirfelser.model.CassaPrevidenzialeFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.NaturaFEL;
import it.csi.siac.sirfelser.model.TipoCassaFEL;

public final class InvioFatturaPACassaPrevidenzialeFELFactory {
	
	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private InvioFatturaPACassaPrevidenzialeFELFactory() {
		// Previene l'instanziazione
	}

	public static CassaPrevidenzialeFEL init(FatturaFEL fatturaFEL, Integer progressivo, DatiCassaPrevidenzialeType datiCassaPrevidenzialeType, Ente ente) {
		CassaPrevidenzialeFEL cassaPrevidenzialeFEL = new CassaPrevidenzialeFEL();
		cassaPrevidenzialeFEL.setProgressivo(progressivo);
		cassaPrevidenzialeFEL.setEnte(ente);
		cassaPrevidenzialeFEL.setFattura(fatturaFEL);
		
		if(datiCassaPrevidenzialeType == null) {
			return cassaPrevidenzialeFEL;
		}
		
		cassaPrevidenzialeFEL.setAliquotaCassa(datiCassaPrevidenzialeType.getAlCassa());
		cassaPrevidenzialeFEL.setImportoContributoCassa(datiCassaPrevidenzialeType.getImportoContributoCassa());
		cassaPrevidenzialeFEL.setImponibileCassa(datiCassaPrevidenzialeType.getImponibileCassa());
		cassaPrevidenzialeFEL.setAliquotaIva(datiCassaPrevidenzialeType.getAliquotaIVA());
		cassaPrevidenzialeFEL.setRiferimentoAmministrazione(datiCassaPrevidenzialeType.getRiferimentoAmministrazione());
		
		setTipoCassaFEL(cassaPrevidenzialeFEL, datiCassaPrevidenzialeType);
		setRitenuta(cassaPrevidenzialeFEL, datiCassaPrevidenzialeType);
		setNaturaFEL(cassaPrevidenzialeFEL, datiCassaPrevidenzialeType);
		
		return cassaPrevidenzialeFEL;
	}

	private static void setTipoCassaFEL(CassaPrevidenzialeFEL cassaPrevidenzialeFEL, DatiCassaPrevidenzialeType datiCassaPrevidenzialeType) {
		if(datiCassaPrevidenzialeType.getTipoCassa() != null) {
			cassaPrevidenzialeFEL.setTipoCassaFEL(TipoCassaFEL.byCodice(datiCassaPrevidenzialeType.getTipoCassa().value()));
		}
	}
	
	private static void setRitenuta(CassaPrevidenzialeFEL cassaPrevidenzialeFEL, DatiCassaPrevidenzialeType datiCassaPrevidenzialeType) {
		if(datiCassaPrevidenzialeType.getRitenuta() != null) {
			cassaPrevidenzialeFEL.setRitenuta(RitenutaType.SI.equals(datiCassaPrevidenzialeType.getRitenuta()));
		}
	}
	
	private static void setNaturaFEL(CassaPrevidenzialeFEL cassaPrevidenzialeFEL, DatiCassaPrevidenzialeType datiCassaPrevidenzialeType) {
		if(datiCassaPrevidenzialeType.getNatura() != null) {
			cassaPrevidenzialeFEL.setNaturaFEL(NaturaFEL.byCodice(datiCassaPrevidenzialeType.getNatura().value()));
		}
	}

}
