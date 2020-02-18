/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import it.csi.siac.siacbilser.integration.dao.SiacTModpagRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDRelazTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRModpagStato;
import it.csi.siac.siacbilser.integration.entity.SiacRSoggettoRelaz;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacfinser.integration.helper.ModalitaPagamentoSoggettoHelper;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.DescrizioneInfoModPagSog;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;

/**
 * Converter di base per la modalita di pagamento
 */
public abstract class ModalitaPagamentoSoggettoBaseConverter<A, B> extends ExtendedDozerConverter<A, B> {
	
	/** Pattern per il SimpleDateFormat: rappresenta le date come giorno / mese / anno */
	private static final String SIMPLE_DATE_FORMAT_PATTERN = "dd/MM/yyyy";

	@Autowired
	private SiacTModpagRepository siacTModpagRepository;
	@Autowired
	private ApplicationContext applicationContext;
	
	private ModalitaPagamentoSoggettoHelper mpsHelper;
	
	public ModalitaPagamentoSoggettoBaseConverter(Class<A> prototypeA, Class<B> prototypeB) {
		super(prototypeA, prototypeB);
	}
	
	protected ModalitaPagamentoSoggetto createModalitaPagamentoSoggettoFromSiacTModpagAndSoggetto(SiacTModpag stm, SiacTSoggetto siacTSoggetto) {
		final String methodName = "createModalitaPagamentoSoggettoFromSiacTModpagAndSoggetto";
		// Caso base
		if(stm == null) {
			return null;
		}
		SiacTModpag siacTModpag = siacTModpagRepository.findOne(stm.getModpagId());
		SiacTModpag siacTModpagCessione = null;
		SiacDRelazTipo siacDRelazTipo = null;
		
		if(siacTModpag.getSiacTSoggetto() != null && siacTSoggetto != null && siacTSoggetto.getSoggettoCode() != null
				&& !siacTSoggetto.getSoggettoCode().equals(siacTModpag.getSiacTSoggetto().getSoggettoCode())) {
			// Il soggetto della modalita di pagamento non e' quello del documento: siamo in cessione, e devo recuperare i dati originali
			siacTModpagCessione = siacTModpag;
			
			siacTModpag = new SiacTModpag();
			// Prendo solo l'uid
			SiacRSoggettoRelaz siacRSoggettoRelaz = recuperaSiacRSoggettoRelaz(siacTModpagCessione, siacTSoggetto);
			if(siacRSoggettoRelaz != null) {
				siacDRelazTipo = siacRSoggettoRelaz.getSiacDRelazTipo();
				siacTModpag.setUid(siacRSoggettoRelaz.getUid());
			}
		}
		
		if(siacTModpag.getUid() == null || siacTModpag.getUid().intValue() == 0) {
			return null;
		}
		
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = mapModalitaPagamentoSoggetto(siacTModpag);
		
		if(siacTModpagCessione != null) {
			ModalitaPagamentoSoggetto modalitaPagamentoSoggettoCessione2 = mapModalitaPagamentoSoggetto(siacTModpagCessione);
			modalitaPagamentoSoggetto.setModalitaPagamentoSoggettoCessione2(modalitaPagamentoSoggettoCessione2);
			
			modalitaPagamentoSoggetto.setCessioneCodModPag(siacTModpagCessione.getUid().toString());
			modalitaPagamentoSoggetto.setCessioneCodSoggetto(siacTModpagCessione.getSiacTSoggetto().getSoggettoCode());
			
			// SIAC-5156: mancano i dati del soggetto cessione
			if(siacTModpagCessione.getSiacTSoggetto() != null) {
				Soggetto soggettoCessione = new Soggetto();
				soggettoCessione.setCodiceSoggetto(siacTModpagCessione.getSiacTSoggetto().getSoggettoCode());
				soggettoCessione.setDenominazione(siacTModpagCessione.getSiacTSoggetto().getSoggettoDesc());
				//SIAC-6261
				soggettoCessione.setDataFineValiditaDurc(siacTModpagCessione.getSiacTSoggetto().getDataFineValiditaDurc());
				modalitaPagamentoSoggetto.setSoggettoCessione(soggettoCessione);
			}
			
			// Modalita accredito: via siacDRelazTipo
			if(siacDRelazTipo != null) {
				ModalitaAccreditoSoggetto modalitaAccreditoSoggetto = new ModalitaAccreditoSoggetto();
				modalitaAccreditoSoggetto.setCodice(siacDRelazTipo.getRelazTipoCode());
				modalitaAccreditoSoggetto.setDescrizione(siacDRelazTipo.getRelazTipoDesc());
				modalitaPagamentoSoggetto.setModalitaAccreditoSoggetto(modalitaAccreditoSoggetto);
				
				try {
					TipoAccredito tipoAccredito = TipoAccredito.valueOf(siacDRelazTipo.getRelazTipoCode());
					modalitaPagamentoSoggetto.setTipoAccredito(tipoAccredito);
				} catch(IllegalArgumentException iae) {
					// Nessun valore dell'enum corrispondente al tipo accredito
					log.info(methodName, "Nessun tipo accredito corrispondente al codice " + siacTModpag.getSiacDAccreditoTipo().getAccreditoTipoCode());
				}
			}
		}
		
		// SIAC-5156: completo i dati della MPS
		// Inizializzazione lazy: se non ho i dati della MPS non inizializzo alcunche'; riutilizzo inoltre la stessa istanza
		if(mpsHelper == null) {
			mpsHelper = new ModalitaPagamentoSoggettoHelper(applicationContext);
			mpsHelper.init();
		}
		
		DescrizioneInfoModPagSog descrizioneInfoModPagSog = mpsHelper.componiDescrizioneArricchitaModalitaPagamento(modalitaPagamentoSoggetto, null, stm.getSiacTEnteProprietario().getEnteProprietarioId());
		modalitaPagamentoSoggetto.setDescrizioneInfo(descrizioneInfoModPagSog);
		
		return modalitaPagamentoSoggetto;
	}
	
	private SiacRSoggettoRelaz recuperaSiacRSoggettoRelaz(SiacTModpag siacTModpagCessione, SiacTSoggetto siacTSoggetto) {
		final String methodName = "recuperaSiacRSoggettoRelaz";

//		SIAC-6281: per il revert, tornare ad usare findSiacRSoggettoRelazByModpagIdAndSoggettoIdDa comn gli stessi pareametrui di input
		List<SiacRSoggettoRelaz> siacRSoggettoRelazs = siacTModpagRepository.findSiacRSoggettoRelazByModpagIdAndSoggettoIdDaSedeSecondaria(siacTModpagCessione.getUid(), siacTSoggetto.getUid());
		if(siacRSoggettoRelazs.isEmpty()) {
			log.warn(methodName, "Nessuna relazione presente su base dati tra modalita' di cessione [" + siacTModpagCessione.getUid() + "] e soggetto [" + siacTSoggetto.getUid() + "]");
			return null;
		}
		SiacRSoggettoRelaz siacRSoggettoRelaz = siacRSoggettoRelazs.get(0);
		if(siacRSoggettoRelazs.size() > 1) {
			log.warn(methodName, "Piu' di una relazione presente su base dati tra modalita' di cessione [" + siacTModpagCessione.getUid() + "] e soggetto [" + siacTSoggetto.getUid()
				+ "]: verra' selezionata la prima [" + siacRSoggettoRelaz.getUid() + "]");
		}
		
		return siacRSoggettoRelaz;
	}

	private ModalitaPagamentoSoggetto mapModalitaPagamentoSoggetto(SiacTModpag siacTModpag) {
		final String methodName = "createModalitaPagamentoSoggetto";
		
		ModalitaPagamentoSoggetto mps = new ModalitaPagamentoSoggetto();
		mps.setUid(siacTModpag.getModpagId());
		
		// Dati necessarii per la descrizione:
		// (codice - descrizione) iban: <...> - bic <...> - conto <...> - intestato a <...> - quietanzante: <...> - CF: <...> - nato il <...> a <comune e nazione>
		mps.setIban(siacTModpag.getIban());
		mps.setBic(siacTModpag.getBic());
		mps.setContoCorrente(siacTModpag.getContocorrente());
		mps.setIntestazioneConto(siacTModpag.getContocorrenteIntestazione());
		mps.setSoggettoQuietanzante(siacTModpag.getQuietanziante());
		mps.setCodiceFiscaleQuietanzante(siacTModpag.getQuietanzianteCodiceFiscale());
		mps.setLuogoNascitaQuietanzante(siacTModpag.getQuietanzianteNascitaLuogo());
		mps.setStatoNascitaQuietanzante(siacTModpag.getQuietanzianteNascitaStato());
		
		// Imposto la data di nascita del quietanzante se valorizzata
		if(siacTModpag.getQuietanzanteNascitaData() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATE_FORMAT_PATTERN, Locale.ITALY);
			mps.setDataNascitaQuietanzante(sdf.format(siacTModpag.getQuietanzanteNascitaData()));
		}
		
		String descrizioneStatoModalitaPagamento = "";
		String codiceStatoModalitaPagamento = "";
		if(siacTModpag.getSiacRModpagStatos() != null) {
			for(SiacRModpagStato siacRModpagStato: siacTModpag.getSiacRModpagStatos()){
				if(siacRModpagStato.getDataCancellazione() == null){
					descrizioneStatoModalitaPagamento = siacRModpagStato.getSiacDModpagStato().getModpagStatoDesc();
					codiceStatoModalitaPagamento = siacRModpagStato.getSiacDModpagStato().getModpagStatoCode();
					break;
				}
			}
		}
		mps.setDescrizioneStatoModalitaPagamento(descrizioneStatoModalitaPagamento);
		mps.setCodiceStatoModalitaPagamento(codiceStatoModalitaPagamento);
		
		if(siacTModpag.getSiacDAccreditoTipo()!=null){
			ModalitaAccreditoSoggetto modalitaAccreditoSoggetto = new ModalitaAccreditoSoggetto();
			modalitaAccreditoSoggetto.setUid(siacTModpag.getSiacDAccreditoTipo().getUid());
			modalitaAccreditoSoggetto.setCodice(siacTModpag.getSiacDAccreditoTipo().getAccreditoTipoCode());
			modalitaAccreditoSoggetto.setDescrizione(siacTModpag.getSiacDAccreditoTipo().getAccreditoTipoDesc());
			mps.setModalitaAccreditoSoggetto(modalitaAccreditoSoggetto);
			
			// Tipo accredito
			try {
				TipoAccredito tipoAccredito = TipoAccredito.valueOf(siacTModpag.getSiacDAccreditoTipo().getAccreditoTipoCode());
				mps.setTipoAccredito(tipoAccredito);
			} catch(IllegalArgumentException iae) {
				// Nessun valore dell'enum corrispondente al tipo accredito
				log.info(methodName, "Nessun tipo accredito corrispondente al codice " + siacTModpag.getSiacDAccreditoTipo().getAccreditoTipoCode());
			}
		}
		
		return mps;
	}
	
}
