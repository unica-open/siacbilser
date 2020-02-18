/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.SerializationUtils;

import it.csi.siac.siacbilser.model.BILDataDictionary;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
/**
 * fa riferimento a tracciato stipendi.xls
 * @author Nazha Ahmad
 *
 */
@XmlType(namespace = BILDataDictionary.NAMESPACE)
public class Stipendio extends Entita{

	/**
	 *
	 */
	private static final long serialVersionUID = 1709328436351590970L;

	private Integer annoElaborazione;             //obbligatorio
	private Integer meseElaborazione;             //obbligatorio
	private TipoVoce tipoVoce;
	private String voceContabile;                //obbligatorio           Voce contabile per risalire alla UEB e quindi agli impegni //TODO VoceDiBilancio?
	private BigDecimal importoSpesa;              //obbligatorio
	private BigDecimal importoEntrata;            //obbligatorio
	//private Integer annoRiferimento;              //obbligatorio
	private Bilancio bilancio;                    //obbligatorio bilancio.anno
	//aggiunto in data 09/07/2015 (Lotto L)
 	private Integer annoDiRiferimento;         // obbligatorio
 	private Ente ente;					      //obbligatorio

	//capitolo
	private Capitolo<?,?> capitolo;      // non e' obbligatorio (Verificare che sia un USCITA GESTIONE!!)
	private Capitolo<?,?> capitoloInput;      // non e' obbligatorio (Verificare che sia un USCITA GESTIONE!!)

	//impegno subimpegno ----> spesa
	private Impegno impegno;                         // non e' obbligatorio
	private SubImpegno subImpegno;                   // non e' obbligatorio


	//accertamento subaccertamento ---->entrata
	private Accertamento accertamento;
	private SubAccertamento subAccertamento;

	private StrutturaAmministrativoContabile strutturaAmministrativoContabile; // non e' obbligatorio  Struttura Amministrativo Contabile della UEB del record (per COTO-cdc)
    private Soggetto soggetto;                 // obbligatorio         Chiave soggetto del record elaborato
	private SiopeSpesa siopeSpesa;// non e' obbligatorio  es: codice gestionale per COTO

	private boolean daAssociareAlDocumento = true;

	private boolean accertamentoAutomatico = false;

	private TipoRecord tipoRecord;

	private boolean tipoStipendioLordo;
	private boolean tipoRecuperi;
	private boolean tipoOneri;
	private boolean tipoRitenute;
	private boolean flagTipoImpostati = false;

	private int fileLineNumber;
	private String fileLineContent;

	/**
	 * @return the annoElaborazione
	 */
	public Integer getAnnoElaborazione() {
		return annoElaborazione;
	}

	/**
	 * @param annoElaborazione
	 *            the annoElaborazione to set
	 */
	public void setAnnoElaborazione(Integer annoElaborazione) {
		this.annoElaborazione = annoElaborazione;
	}

	/**
	 * @return the meseElaborazione
	 */
	public Integer getMeseElaborazione() {
		return meseElaborazione;
	}

	/**
	 * @param meseElaborazione
	 *            the meseElaborazione to set
	 */
	public void setMeseElaborazione(Integer meseElaborazione) {
		this.meseElaborazione = meseElaborazione;
	}


	/**
	 * @return the tipoVoce
	 */
	public TipoVoce getTipoVoce() {
		return tipoVoce;
	}

	/**
	 * @param tipoVoce the tipoVoce to set
	 */
	public void setTipoVoce(TipoVoce tipoVoce) {
		this.tipoVoce = tipoVoce;
	}

	/**
	 * @return the voceContabile
	 */
	public String getVoceContabile() {
		return voceContabile;
	}

	/**
	 * @param voceContabile
	 *            the voceContabile to set
	 */
	public void setVoceContabile(String voceContabile) {
		this.voceContabile = voceContabile;
	}

	/**
	 * @return the importoSpesa
	 */
	public BigDecimal getImportoSpesa() {
		return importoSpesa;
	}

	/**
	 * @param importoSpesa
	 *            the importoSpesa to set
	 */
	public void setImportoSpesa(BigDecimal importoSpesa) {
		this.importoSpesa = importoSpesa;
	}

	/**
	 * @return the importoEntrata
	 */
	public BigDecimal getImportoEntrata() {
		return importoEntrata;
	}

	/**
	 * @param importoEntrata
	 *            the importoEntrata to set
	 */
	public void setImportoEntrata(BigDecimal importoEntrata) {
		this.importoEntrata = importoEntrata;
	}

	/**
	 * @return the bilancio
	 */
	public Bilancio getBilancio() {
		return bilancio;
	}

	/**
	 * @param bilancio
	 *            the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	/**
	 * @return the ente
	 */
	public Ente getEnte() {
		return ente;
	}

	/**
	 * @param ente
	 *            the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	/**
	 * @return the capitoloUscitaGestione
	 */
	public Capitolo<?,?> getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitoloUscitaGestione
	 *            the capitoloUscitaGestione to set
	 */
	public void setCapitolo(Capitolo<?,?> capitoloUscitaGestione) {
		this.capitolo = capitoloUscitaGestione;
	}

	/**
	 * @return the subImpegno
	 */
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}

	/**
	 * @param subImpegno
	 *            the subImpegno to set
	 */
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}

	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return impegno;
	}

	/**
	 * @param impegno
	 *            the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}

	/**
	 * @return the accertamento
	 */
	public Accertamento getAccertamento() {
		return accertamento;
	}

	/**
	 * @param accertamento
	 *            the accertamento to set
	 */
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}

	/**
	 * @return the subAccertamento
	 */
	public SubAccertamento getSubAccertamento() {
		return subAccertamento;
	}

	/**
	 * @param subAccertamento
	 *            the subAccertamento to set
	 */
	public void setSubAccertamento(SubAccertamento subAccertamento) {
		this.subAccertamento = subAccertamento;
	}

	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile
	 *            the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto
	 *            the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the siopeSpesa
	 */
	public SiopeSpesa getSiopeSpesa() {
		return siopeSpesa;
	}

	/**
	 * @param siopeSpesa
	 *            the siopeSpesa to set
	 */
	public void setSiopeSpesa(SiopeSpesa siopeSpesa) {
		this.siopeSpesa = siopeSpesa;
	}


	/**
	 * @return the annoDiRiferimento
	 */
	public Integer getAnnoDiRiferimento() {
		return annoDiRiferimento;
	}

	/**
	 * @param annoDiRiferimento the annoDiRiferimento to set
	 */
	public void setAnnoDiRiferimento(Integer annoDiRiferimento) {
		this.annoDiRiferimento = annoDiRiferimento;
	}

	/**
	 * @return the daAssociareAlDocumento
	 */
	public boolean isDaAssociareAlDocumento() {
		return daAssociareAlDocumento;
	}


	/**
	 * @return the accertamentoAutomatico
	 */
	public boolean isAccertamentoAutomatico() {
		return accertamentoAutomatico;
	}

	/**
	 * @param accertamentoAutomatico the accertamentoAutomatico to set
	 */
	public void setAccertamentoAutomatico(boolean accertamentoAutomatico) {
		this.accertamentoAutomatico = accertamentoAutomatico;
	}


	/**
	 * @param daAssociareAlDocumento the daAssociareAlDocumento to set
	 */
	public void setDaAssociareAlDocumento(boolean daAssociareAlDocumento) {
		this.daAssociareAlDocumento = daAssociareAlDocumento;
	}


	/**
	 * @param tipoRecord the tipoRecord to set
	 */
	public void setTipoRecord(TipoRecord tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	/**
	 * @return the tipoRecord
	 */
	public TipoRecord getTipoRecord() {
		return tipoRecord;
	}

	/**
	 * @return the fileLineNumber
	 */
	public int getFileLineNumber() {
		return fileLineNumber;
	}

	/**
	 * @param fileLineNumber the fileLineNumber to set
	 */
	public void setFileLineNumber(int fileLineNumber) {
		this.fileLineNumber = fileLineNumber;
	}

	/**
	 * @return the fileLineContent
	 */
	public String getFileLineContent() {
		return fileLineContent;
	}

	/**
	 * @param fileLineContent the fileLineContent to set
	 */
	public void setFileLineContent(String fileLineContent) {
		this.fileLineContent = fileLineContent;
	}

	public boolean isSpesa(){
		return getImportoSpesa() != null && !BigDecimal.ZERO.equals(getImportoSpesa());
	}

	public boolean isEntrata(){
		return getImportoEntrata() != null && !BigDecimal.ZERO.equals(getImportoEntrata());
	}

	/**
	 * <font color='blue'><b>computeNumeroDocumentoFromStipendio </b></font><br />
	 *Numero documento =<br />
	 * {<br />
	 *  0                   <br />
	 *  mese elaborazione   <br />
	 *  anno di riferimento <br />
	 *  capitolo 		    <br />
	 *  articolo 			<br />
	 *  ueb                 <br />
	 *  }<br />
	 *  presenti nel record se presenti
	 * o relativi al capitolo o alla UEB reperita
	 *
	 * @param stipendio
	 * @return
	 */
	public String computeNumeroDocumento() {
		if(getBilancio() == null || getCapitolo() == null || getStrutturaAmministrativoContabile() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("0");
		sb.append(getMeseElaborazione());
		sb.append(getBilancio().getAnno());
		sb.append(getCapitolo().getNumeroCapitolo());
		sb.append(getCapitolo().getNumeroArticolo());
		if (getCapitolo().getNumeroUEB() != null) {
			sb.append(getCapitolo().getNumeroUEB());
		}
		if (getStrutturaAmministrativoContabile().getCodice() != null) {
			sb.append(getStrutturaAmministrativoContabile().getCodice());
		}
		return sb.toString().trim();

	}

	public boolean isUidImpegnoValorizzato(){

		return impegno != null && impegno.getUid() > 0;
	}
	public boolean isUidSubImpegnoValorizzato(){

		return subImpegno != null && subImpegno.getUid() > 0;
	}
	public boolean isUidAccertamentoValorizzato(){

		return accertamento != null && accertamento.getUid() > 0;
	}
	public boolean isUidSubAccertamentoValorizzato(){

		return subAccertamento != null && subAccertamento.getUid() > 0;
	}


	/**
	 * controlla anno e numero impegno se sono  valorizzati
	 * @return
	 */
	public boolean isAccertamentoValorizzato() {

		return accertamento !=null && accertamento.getNumero()!=null && accertamento.getAnnoMovimento() !=0 && !BigDecimal.ZERO.equals(accertamento.getNumero());
	}

	/**
	 * controlla anno e numero accertamento se sono  valorizzati
	 * @return
	 */
	public boolean isImpegnoValorizzato() {
		return impegno !=null && impegno.getNumero()!=null && impegno.getAnnoMovimento() !=0 && !BigDecimal.ZERO.equals(impegno.getNumero());
	}

	/**
	 * isTipoStipendioLordo
	 * true se TipoVoce =D ,IMP=si,Acc=no,importo= importo di spesa
	 * da non utilizzare sul servizio
	 * @return
	 */
	public boolean isTipoStipendioLordo(){
		return tipoStipendioLordo;
	}

	/**
	 * isTipoRecuperi
	 * true se TipoVoce =D ,IMP=no,Acc=si,importo= importo di entrata
	 * da non utilizzare sul servizio
	 * @return
	 */
	public boolean isTipoRecuperi(){
		return tipoRecuperi;
	}

	/**
	 * isTipoOneri
	 * true se TipoVoce =E ,IMP=si,Acc=no,importo= importo di spesa
	 * da non utilizzare sul servizio
	 * @return
	 */
	public boolean isTipoOneri(){
		return tipoOneri;
	}
	/**
	 * isTipoRitenute
	 * true se TipoVoce =D ,IMP=si,Acc=si,importo= importo di Entrata
	 * da non utilizzare sul servizio
	 * @return
	 */
	public boolean isTipoRitenute(){
		return tipoRitenute;
	}

	/**
	 *
	 * @return TipoRecord.STIPENDIO_LORDO
	 */
	public boolean isTipoRecordStipendioLordo(){
		return TipoRecord.STIPENDIO_LORDO.equals(getTipoRecord());
	}

	/**
	 *
	 * @return TipoRecord.RITENUTE
	 */
	public boolean isTipoRecordRitenute(){
		return TipoRecord.RITENUTE.equals(getTipoRecord());
	}

	/**
	 *
	 * @return TipoRecord.ONERI
	 */
	public boolean isTipoRecordOneri(){
		return TipoRecord.ONERI.equals(getTipoRecord());
	}

	/**
	 *
	 * @return TipoRecord.RECUPERI
	 */
	public boolean isTipoRecordRecuperi(){
		return TipoRecord.RECUPERI.equals(getTipoRecord());
	}

	public void impostaFlagTipo() {
		if(flagTipoImpostati) {
			return;
		}

		flagTipoImpostati = true;
		tipoStipendioLordo = TipoVoce.ENTRATA_SPESE_SINGOLE.equals(getTipoVoce()) && isImpegnoValorizzato() && !isAccertamentoValorizzato() && isSpesa();
		tipoRecuperi = TipoVoce.ENTRATA_SPESE_SINGOLE.equals(getTipoVoce()) && !isImpegnoValorizzato() && isAccertamentoValorizzato() && isEntrata();
		tipoOneri = TipoVoce.RITENUTE_ONERI.equals(getTipoVoce()) && isImpegnoValorizzato() && !isAccertamentoValorizzato() && isSpesa();
		tipoRitenute = TipoVoce.ENTRATA_SPESE_SINGOLE.equals(getTipoVoce()) && isImpegnoValorizzato() && isAccertamentoValorizzato() && isEntrata();
	}

	public void resetCapitoloFromInput() {
		capitolo = SerializationUtils.clone(capitoloInput);
	}
	
	public void initCapitoloInput() {
		capitoloInput = SerializationUtils.clone(capitolo);
	}
}
