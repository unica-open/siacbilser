/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggettoResponse;
import it.csi.siac.siacfinser.integration.dad.ProgressivoType;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.entity.SiacDAmbitoFin;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceSoggettoService extends AbstractBaseService<InserisceSoggetto, InserisceSoggettoResponse> {

	
	
	@Autowired
	SoggettoFinDad soggettoDad;
	
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	
	@Override
	@Transactional
	public InserisceSoggettoResponse executeService(
			InserisceSoggetto serviceRequest) {
		
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	public void execute() {
		final String methodName ="InserisceSoggettoService - execute";
		//1.  Leggo i dati in input al servizio:
		Soggetto soggettoInput = req.getSoggetto();
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		DatiOperazioneDto datiOperazioneDto = commonDad.inizializzaDatiOperazione(ente, richiedente, Operazione.INSERIMENTO, req.getCodificaAmbito(), null);
		
		//SIAC-5275 stacchiamo il codice fuori della transazione:
		Number idEnte = ente.getUid();
		SiacDAmbitoFin siacDAmbitoPerCode = datiOperazioneDto.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();
		long nuovoCode = soggettoDad.getMaxCode(ProgressivoType.SOGGETTO, idEnte , idAmbito, loginOperazione);
		
		//2. Richiamo il metodo che si occupa di inserire un nuovo soggetto:
		Soggetto soggetto =  soggettoDad.inserisciSoggetto(soggettoInput, richiedente, ente, false, datiOperazioneDto,nuovoCode);
		//3. Costruisco la response da restituire in output al servizio:
		res.setSoggetto(soggetto);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		Soggetto soggettoInput = req.getSoggetto();		

		checkNotNull(soggettoInput, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto"));

		checkNotNull(soggettoInput.getTipoSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Tipo Natura giuridica"));

		if(!(soggettoInput.getTipoSoggetto().getCodice().equalsIgnoreCase("PGI")|| soggettoInput.getTipoSoggetto().getCodice().equalsIgnoreCase("PG"))){	
		soggettoInput.setCognome(soggettoInput.getCognome().toUpperCase());
		soggettoInput.setNome(soggettoInput.getNome().toUpperCase());
		}
		//2.5.10	Controlli per inserimento/aggiornamento Soggetto
		
//		b.	Se l'attributo Soggetto.residenteEstero = FALSE verificare che sia stato valorizzato Soggetto.codiceFiscale 
//		e in caso Soggetto.residenteEstero = FALSE verificare che sia valorizzato il CF estero.
		if(StringUtilsFin.isEmpty(soggettoInput.getCodiceFiscale()) && StringUtilsFin.isEmpty(soggettoInput.getCodiceFiscaleEstero())){
			checkNotNull(null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Codice Fiscale"));
		}

		String confronto = soggettoInput.getTipoSoggetto().getCodice().trim().toUpperCase();
		if(CostantiFin.PERSONA_FISICA.equals(confronto) || CostantiFin.PERSONA_FISICA_I.equals(confronto)){
//			c.	Se Soggetto.TipoNaturaGiuridica = 'Persona Fisica' o 'Persona Fisica con PIVA' verificare che siano stati
//			valorizzati i campi  Soggetto.nome, Soggetto.cognome, Soggetto.sesso, Soggetto.dataNascita, Soggetto.Comune di tipo TipoComune = 'Nascita'.
			checkNotNull(soggettoInput.getCognome(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Cognome"));
			checkNotNull(soggettoInput.getNome(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Nome"));
			checkNotNull(soggettoInput.getComuneNascita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Comune Nascita"));
			checkNotNull(soggettoInput.getSesso(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Sesso"));
			checkNotNull(soggettoInput.getDataNascita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Data Nascita"));
		} else if(CostantiFin.PERSONA_GIURIDICA.equals(confronto) || CostantiFin.PERSONA_GIURIDICA_I.equals(confronto)){
//			d.	Se Soggetto.TipoNaturaGiuridica = 'Persona Giuridica o 'Persona Giuridica senza PIVA' verificare che siano stati valorizzati
//			i campi  Soggetto.ragioneSociale e Soggetto.NaturaGiuridicaSoggetto; 
			checkNotNull(soggettoInput.getDenominazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ragione Sociale"));
			checkNotNull(soggettoInput.getNaturaGiuridicaSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Natura giuridica"));
		}
		
		if(CostantiFin.PERSONA_FISICA_I.equals(confronto) || CostantiFin.PERSONA_GIURIDICA.equals(confronto)){
//			e.	Se Soggetto.TipoNaturaGiuridica = 'Persona Fisica con PIVA'  o 'Persona Giuridica' verificare che sia stato valorizzato Soggetto.partitaIVA
			checkNotNull(soggettoInput.getPartitaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Partita iva"));
		}
		
//		f.	Deve essere valorizzato un Indirizzo del Soggetto che ha l'attributo Indirizzo.principale = TRUE
		if(soggettoInput.getIndirizzi()==null || soggettoInput.getIndirizzi().size()==0){
			checkNotNull(null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Indirizzi"));
		} else {
			boolean trovatoPrincipale = false;
			for(IndirizzoSoggetto iterato :soggettoInput.getIndirizzi()){
				if(iterato!=null && Boolean.valueOf(iterato.getPrincipale())){
					trovatoPrincipale = true;
				}
			}
			if(!trovatoPrincipale){
				checkNotNull(null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Indirizzi (inserire almeno un indirizzo principale)"));
			}
		}
	}
}
