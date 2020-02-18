/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.backoffice;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ModificaCigBackoffice;
import it.csi.siac.siacbilser.frontend.webservice.msg.ModificaCigBackofficeResponse;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

@Service
public class ModificaCigBackofficeService
		extends ExtendedBaseService<ModificaCigBackoffice, ModificaCigBackofficeResponse> {

	@Autowired
	private ImpegnoBilDad impegnoBilDad;

	@Autowired
	private CodificaDad codificaDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();

		checkEntita(req.getImpegno(), "Impegno");
		checkEntita(req.getImpegno().getSiopeTipoDebito(), "SiopeTipoDebito");
		checkNotNull(req.getTipoModifica(), "TipoModifica");

	}

	@Override
	@Transactional
	public ModificaCigBackofficeResponse executeService(ModificaCigBackoffice serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {

		SiopeTipoDebito tipoDebito = codificaDad.ricercaCodifica(SiopeTipoDebito.class,
				req.getImpegno().getSiopeTipoDebito().getUid());
		
		boolean checkSiopeAssenzaMotivazione = req.getImpegno().getSiopeAssenzaMotivazione() != null 
				&& req.getImpegno().getSiopeAssenzaMotivazione().getUid() != 0;
		
		boolean checkCig = StringUtils.isNotBlank(req.getImpegno().getCig());
		
		if (Constanti.SIOPE_CODE_COMMERCIALE.equals(tipoDebito.getCodice())) {
			// Debito commerciale
			checkBusinessCondition(checkSiopeAssenzaMotivazione || checkCig,
					ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("inserire CIG o Motivazione assenza del CIG"));

			checkBusinessCondition(!checkCig || !checkSiopeAssenzaMotivazione, ErroreCore.INCONGRUENZA_NEI_PARAMETRI
					.getErrore("Inserire solo uno dei due campi CIG e Motivazione assenza del CIG"));
		} else {
			// Non commerciale
			checkBusinessCondition(!checkSiopeAssenzaMotivazione,
					ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("per il tipo debito NON COMMERCIALE non e' possibile impostare la Motivazione assenza del CIG"));
		}

		// TODO Completare implementazione: 1)ottenre l'uid corretto 2) richiamare il
		// DAO

		Impegno impegno = req.getImpegno();
		int uid;
		// Evolutive BackofficeModificaCigRemedy
		String numeroRemedy = req.getNumeroRemedy();

		if (impegno instanceof SubImpegno) {
			uid = impegno.getUid();

		} else {
			Integer uidTestata = impegnoBilDad.findUidTestataByUidMovimento(impegno.getUid());
			checkBusinessCondition(uidTestata != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("impegno", "uid " + impegno.getUid()));
			uid = uidTestata.intValue();
		}

		// invocare DAD con i singoli parametri (uid,uid tipo debito, cig, uid
		// motivazione assenza); selezionare il metodo in base a tipoModifica
		
		switch (req.getTipoModifica()) {
		case MOVIMENTO:
			impegnoBilDad.modificaCigDocumentiQuote(
					uid, 
					tipoDebito.getUid(), 
					impegno.getCig(), 
					impegno.getSiopeAssenzaMotivazione()==null ? null : impegno.getSiopeAssenzaMotivazione().getUid(),
					numeroRemedy);
			break;
		case QUOTE_E_LIQUIDAZIONI_SENZA_ORDINATIVI_COLLEGATI:
			impegnoBilDad.modificaCigLiquidazioniSenzaOrdinativiCollegati(uid, 
					tipoDebito.getUid(), 
					impegno.getCig(), 
					impegno.getSiopeAssenzaMotivazione()==null ? null : impegno.getSiopeAssenzaMotivazione().getUid(),
					numeroRemedy);
			break;
		default:
			throw new IllegalStateException("Tipo Modifica non implementato");
		}


//		checkBusinessCondition(false, ErroreCore.ERRORE_DI_SISTEMA.getErrore("Test"));
		//TODO gestire i valori restituiti e la response
	}

}
