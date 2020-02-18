/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti.model;

import it.csi.siac.siaccommon.util.Sesso;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;

public abstract class Predocumento
{
	private String tipoCausale;
	private String codiceCausale;
	private String causaleSAC1;
	private String causaleSAC2;
	private String causaleSAC3;
	private String codiceContoEnte;
	private String tipoContoEnte;
	private String descrizioneContoEnte;
	private String numero;
	private String data;
	private String dataCompetenza;
	private String periodo;
	private String descrizione;
	private String importo;
	private String note;
	private String codiceFiscale;
	private String partitaIva;
	private String ragioneSociale;
	private String cognome;
	private String nome;
	private String dataNascita;
	private String idComuneNascita;
	
	private ComuneNascita comuneNascita;
	
	
	private String nazioneNascita;
	private Sesso sesso;
	private String indirizzo;
	private String siglaProvincia;
	private String cap;
	private String comuneIndirizzo;
	private String nazioneIndirizzo;
	private String email;
	private String telefono;
	private String annoCapitolo;
	private String numeroCapitolo;
	private String numeroArticolo;
	private String numeroUeb;
	private String annoMovimentoGestione;
	private String numeroMovimentoGestione;
	private String numeroMovimentoGestioneSub;
	private String annoProvvedimento;
	private String numeroProvvedimento;
	private String strutturaAmministrativoContabileProvvedimento;
	private String tipoProvvedimento;
	private String codiceSoggetto;
	private String dataProvvisorioCassa;
	private String numeroProvvisorioCassa;
	private String codiceNaturaGiuridica;

	private String intestazioneConto;
	private String codiceABI;
	private String codiceCAB;
	private String codiceIBAN;
	private String codiceBIC;
	private String soggettoQuietanzante;
	private String codiceFiscaleQuietanzante;
	private String sedeSoggetto;
	private String tipoSoggetto;
	private String descrizioneSoggetto;

	public String getIntestazioneConto()
	{
		return intestazioneConto;
	}

	public void setIntestazioneConto(String intestazioneConto)
	{
		this.intestazioneConto = intestazioneConto;
	}

	public String getCodiceABI()
	{
		return codiceABI;
	}

	public void setCodiceABI(String codiceABI)
	{
		this.codiceABI = codiceABI;
	}

	public String getCodiceCAB()
	{
		return codiceCAB;
	}

	public void setCodiceCAB(String codiceCAB)
	{
		this.codiceCAB = codiceCAB;
	}

	public String getCodiceIBAN()
	{
		return codiceIBAN;
	}

	public void setCodiceIBAN(String codiceIBAN)
	{
		this.codiceIBAN = codiceIBAN;
	}

	public String getCodiceBIC()
	{
		return codiceBIC;
	}

	public void setCodiceBIC(String codiceBIC)
	{
		this.codiceBIC = codiceBIC;
	}

	public String getSoggettoQuietanzante()
	{
		return soggettoQuietanzante;
	}

	public void setSoggettoQuietanzante(String soggettoQuietanzante)
	{
		this.soggettoQuietanzante = soggettoQuietanzante;
	}

	public String getCodiceFiscaleQuietanzante()
	{
		return codiceFiscaleQuietanzante;
	}

	public void setCodiceFiscaleQuietanzante(String codiceFiscaleQuietanzante)
	{
		this.codiceFiscaleQuietanzante = codiceFiscaleQuietanzante;
	}

	public String getSedeSoggetto()
	{
		return sedeSoggetto;
	}

	public void setSedeSoggetto(String sedeSoggetto)
	{
		this.sedeSoggetto = sedeSoggetto;
	}

	public String getTipoSoggetto()
	{
		return tipoSoggetto;
	}

	public void setTipoSoggetto(String tipoSoggetto)
	{
		this.tipoSoggetto = tipoSoggetto;
	}

	public String getDescrizioneSoggetto()
	{
		return descrizioneSoggetto;
	}

	public void setDescrizioneSoggetto(String descrizioneSoggetto)
	{
		this.descrizioneSoggetto = descrizioneSoggetto;
	}	
	
	public String getTipoCausale()
	{
		return tipoCausale;
	}

	public void setTipoCausale(String tipoCausale)
	{
		this.tipoCausale = tipoCausale;
	}

	public String getCodiceCausale()
	{
		return codiceCausale;
	}

	public void setCodiceCausale(String codiceCausale)
	{
		this.codiceCausale = codiceCausale;
	}

	public String getCausaleSAC1()
	{
		return causaleSAC1;
	}

	public void setCausaleSAC1(String causaleSAC1)
	{
		this.causaleSAC1 = causaleSAC1;
	}

	public String getCausaleSAC2()
	{
		return causaleSAC2;
	}

	public void setCausaleSAC2(String causaleSAC2)
	{
		this.causaleSAC2 = causaleSAC2;
	}

	public String getCausaleSAC3()
	{
		return causaleSAC3;
	}

	public void setCausaleSAC3(String causaleSAC3)
	{
		this.causaleSAC3 = causaleSAC3;
	}

	public String getCodiceContoEnte()
	{
		return codiceContoEnte;
	}

	public void setCodiceContoEnte(String codiceContoEnte)
	{
		this.codiceContoEnte = codiceContoEnte;
	}

	public String getTipoContoEnte()
	{
		return tipoContoEnte;
	}

	public void setTipoContoEnte(String tipoContoEnte)
	{
		this.tipoContoEnte = tipoContoEnte;
	}

	public String getDescrizioneContoEnte()
	{
		return descrizioneContoEnte;
	}

	public void setDescrizioneContoEnte(String descrizioneContoEnte)
	{
		this.descrizioneContoEnte = descrizioneContoEnte;
	}

	public String getNumero()
	{
		return numero;
	}

	public void setNumero(String numero)
	{
		this.numero = numero;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getDataCompetenza()
	{
		return dataCompetenza;
	}

	public void setDataCompetenza(String dataCompetenza)
	{
		this.dataCompetenza = dataCompetenza;
	}

	public String getPeriodo()
	{
		return periodo;
	}

	public void setPeriodo(String periodo)
	{
		this.periodo = periodo;
	}

	public String getDescrizione()
	{
		return descrizione;
	}

	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}

	public String getImporto()
	{
		return importo;
	}

	public void setImporto(String importo)
	{
		this.importo = importo;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public String getCodiceFiscale()
	{
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale)
	{
		this.codiceFiscale = codiceFiscale;
	}

	public String getPartitaIva()
	{
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva)
	{
		this.partitaIva = partitaIva;
	}

	public String getRagioneSociale()
	{
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale)
	{
		this.ragioneSociale = ragioneSociale;
	}

	public String getCognome()
	{
		return cognome;
	}

	public void setCognome(String cognome)
	{
		this.cognome = cognome;
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public String getNazioneNascita()
	{
		return nazioneNascita;
	}

	public void setNazioneNascita(String nazioneNascita)
	{
		this.nazioneNascita = nazioneNascita;
	}




	public Sesso getSesso()
	{
		return sesso;
	}

	public void setSesso(Sesso sesso)
	{
		this.sesso = sesso;
	}

	public String getIndirizzo()
	{
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo)
	{
		this.indirizzo = indirizzo;
	}

	public String getComuneIndirizzo()
	{
		return comuneIndirizzo;
	}

	public void setComuneIndirizzo(String comuneIndirizzo)
	{
		this.comuneIndirizzo = comuneIndirizzo;
	}

	public String getNazioneIndirizzo()
	{
		return nazioneIndirizzo;
	}

	public void setNazioneIndirizzo(String nazioneIndirizzo)
	{
		this.nazioneIndirizzo = nazioneIndirizzo;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getTelefono()
	{
		return telefono;
	}

	public void setTelefono(String telefono)
	{
		this.telefono = telefono;
	}

	public String getAnnoCapitolo()
	{
		return annoCapitolo;
	}

	public void setAnnoCapitolo(String annoCapitolo)
	{
		this.annoCapitolo = annoCapitolo;
	}

	public String getNumeroCapitolo()
	{
		return numeroCapitolo;
	}

	public void setNumeroCapitolo(String numeroCapitolo)
	{
		this.numeroCapitolo = numeroCapitolo;
	}

	public String getNumeroArticolo()
	{
		return numeroArticolo;
	}

	public void setNumeroArticolo(String numeroArticolo)
	{
		this.numeroArticolo = numeroArticolo;
	}

	public String getNumeroUeb()
	{
		return numeroUeb;
	}

	public void setNumeroUeb(String numeroUeb)
	{
		this.numeroUeb = numeroUeb;
	}

	public String getAnnoMovimentoGestione()
	{
		return annoMovimentoGestione;
	}

	public void setAnnoMovimentoGestione(String annoMovimentoGestione)
	{
		this.annoMovimentoGestione = annoMovimentoGestione;
	}

	public String getNumeroMovimentoGestione()
	{
		return numeroMovimentoGestione;
	}

	public void setNumeroMovimentoGestione(String numeroMovimentoGestione)
	{
		this.numeroMovimentoGestione = numeroMovimentoGestione;
	}

	public String getNumeroMovimentoGestioneSub()
	{
		return numeroMovimentoGestioneSub;
	}

	public void setNumeroMovimentoGestioneSub(String numeroMovimentoGestioneSub)
	{
		this.numeroMovimentoGestioneSub = numeroMovimentoGestioneSub;
	}

	public String getAnnoProvvedimento()
	{
		return annoProvvedimento;
	}

	public void setAnnoProvvedimento(String annoProvvedimento)
	{
		this.annoProvvedimento = annoProvvedimento;
	}

	public String getNumeroProvvedimento()
	{
		return numeroProvvedimento;
	}

	public void setNumeroProvvedimento(String numeroProvvedimento)
	{
		this.numeroProvvedimento = numeroProvvedimento;
	}

	public String getStrutturaAmministrativoContabileProvvedimento()
	{
		return strutturaAmministrativoContabileProvvedimento;
	}

	public void setStrutturaAmministrativoContabileProvvedimento(
			String strutturaAmministrativoContabileProvvedimento)
	{
		this.strutturaAmministrativoContabileProvvedimento = strutturaAmministrativoContabileProvvedimento;
	}

	public String getTipoProvvedimento()
	{
		return tipoProvvedimento;
	}

	public void setTipoProvvedimento(String tipoProvvedimento)
	{
		this.tipoProvvedimento = tipoProvvedimento;
	}

	public String getCodiceSoggetto()
	{
		return codiceSoggetto;
	}

	public void setCodiceSoggetto(String codiceSoggetto)
	{
		this.codiceSoggetto = codiceSoggetto;
	}

	public String getDataProvvisorioCassa()
	{
		return dataProvvisorioCassa;
	}

	public void setDataProvvisorioCassa(String dataProvvisorioCassa)
	{
		this.dataProvvisorioCassa = dataProvvisorioCassa;
	}

	public String getNumeroProvvisorioCassa()
	{
		return numeroProvvisorioCassa;
	}

	public void setNumeroProvvisorioCassa(String numeroProvvisorioCassa)
	{
		this.numeroProvvisorioCassa = numeroProvvisorioCassa;
	}

	public String getSiglaProvincia()
	{
		return siglaProvincia;
	}

	public void setSiglaProvincia(String siglaProvincia)
	{
		this.siglaProvincia = siglaProvincia;
	}

	public String getCap()
	{
		return cap;
	}

	public void setCap(String cap)
	{
		this.cap = cap;
	}

	public String getDataNascita()
	{
		return dataNascita;
	}

	public void setDataNascita(String dataNascita)
	{
		this.dataNascita = dataNascita;
	}

	public String getCodiceNaturaGiuridica()
	{
		return codiceNaturaGiuridica;
	}

	public void setCodiceNaturaGiuridica(String codiceNaturaGiuridica)
	{
		this.codiceNaturaGiuridica = codiceNaturaGiuridica;
	}

	public String getIdComuneNascita()
	{
		return idComuneNascita;
	}

	public void setIdComuneNascita(String idComuneNascita)
	{
		this.idComuneNascita = idComuneNascita;
	}

	public ComuneNascita getComuneNascita()
	{
		return comuneNascita;
	}

	public void setComuneNascita(ComuneNascita comuneNascita)
	{
		this.comuneNascita = comuneNascita;
	}

}
