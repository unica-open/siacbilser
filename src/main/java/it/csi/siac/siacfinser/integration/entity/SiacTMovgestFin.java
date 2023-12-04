/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_movgest database table.
 * 
 */
@Entity
@Table(name="siac_t_movgest")
public class SiacTMovgestFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SIAC_T_MOVGEST_MOVGEST_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_movgest_movgest_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOVGEST_MOVGEST_ID_GENERATOR")
	@Column(name="movgest_id")
	private Integer movgestId;

	@Column(name="movgest_anno")
	// private String movgestAnno;
	private Integer movgestAnno;

	@Column(name="movgest_desc")
	private String movgestDesc;

	@Column(name="movgest_numero")
	private BigDecimal movgestNumero;

	//bi-directional many-to-one association to SiacRMovgestBilElemFin
	@OneToMany(mappedBy="siacTMovgest", fetch=FetchType.LAZY)
	private List<SiacRMovgestBilElemFin> siacRMovgestBilElems;

	//bi-directional many-to-one association to SiacRMovgestOrdinativoFin
	@OneToMany(mappedBy="siacTMovgest", fetch=FetchType.LAZY)
	private List<SiacRMovgestOrdinativoFin> siacRMovgestOrdinativos;

	//bi-directional many-to-one association to SiacDMovgestTipoFin
	@ManyToOne
	@JoinColumn(name="movgest_tipo_id")
	private SiacDMovgestTipoFin siacDMovgestTipo;

	//bi-directional many-to-one association to SiacTBilFin
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBilFin siacTBil;

	//bi-directional many-to-one association to SiacTMovgestT
	@OneToMany(mappedBy="siacTMovgest", fetch=FetchType.LAZY)
	private List<SiacTMovgestTsFin> siacTMovgestTs;
	
	@Column(name = "parere_finanziario")
	private Boolean parereFinanziario;
	
	@Basic
	@Column(name = "parere_finanziario_data_modifica")
	private Date parereFinanziarioDataModifica;
	
	@Basic
	@Column(name = "parere_finanziario_login_operazione")
	private String parereFinanziarioLoginOperazione;
	
	
	public SiacTMovgestFin() {
	}

	public Integer getMovgestId() {
		return this.movgestId;
	}

	public void setMovgestId(Integer movgestId) {
		this.movgestId = movgestId;
	}


	
	public Integer getMovgestAnno() {
		return this.movgestAnno;
	}

	public void setMovgestAnno(Integer movgestAnno) {
		this.movgestAnno = movgestAnno;
	}

	public String getMovgestDesc() {
		return this.movgestDesc;
	}

	public void setMovgestDesc(String movgestDesc) {
		this.movgestDesc = movgestDesc;
	}

	public BigDecimal getMovgestNumero() {
		return this.movgestNumero;
	}

	public void setMovgestNumero(BigDecimal movgestNumero) {
		this.movgestNumero = movgestNumero;
	}

	public List<SiacRMovgestBilElemFin> getSiacRMovgestBilElems() {
		return this.siacRMovgestBilElems;
	}

	public void setSiacRMovgestBilElems(List<SiacRMovgestBilElemFin> siacRMovgestBilElems) {
		this.siacRMovgestBilElems = siacRMovgestBilElems;
	}

	public SiacRMovgestBilElemFin addSiacRMovgestBilElem(SiacRMovgestBilElemFin siacRMovgestBilElem) {
		getSiacRMovgestBilElems().add(siacRMovgestBilElem);
		siacRMovgestBilElem.setSiacTMovgest(this);

		return siacRMovgestBilElem;
	}

	public SiacRMovgestBilElemFin removeSiacRMovgestBilElem(SiacRMovgestBilElemFin siacRMovgestBilElem) {
		getSiacRMovgestBilElems().remove(siacRMovgestBilElem);
		siacRMovgestBilElem.setSiacTMovgest(null);

		return siacRMovgestBilElem;
	}

	public List<SiacRMovgestOrdinativoFin> getSiacRMovgestOrdinativos() {
		return this.siacRMovgestOrdinativos;
	}

	public void setSiacRMovgestOrdinativos(List<SiacRMovgestOrdinativoFin> siacRMovgestOrdinativos) {
		this.siacRMovgestOrdinativos = siacRMovgestOrdinativos;
	}

	public SiacRMovgestOrdinativoFin addSiacRMovgestOrdinativo(SiacRMovgestOrdinativoFin siacRMovgestOrdinativo) {
		getSiacRMovgestOrdinativos().add(siacRMovgestOrdinativo);
		siacRMovgestOrdinativo.setSiacTMovgest(this);

		return siacRMovgestOrdinativo;
	}

	public SiacRMovgestOrdinativoFin removeSiacRMovgestOrdinativo(SiacRMovgestOrdinativoFin siacRMovgestOrdinativo) {
		getSiacRMovgestOrdinativos().remove(siacRMovgestOrdinativo);
		siacRMovgestOrdinativo.setSiacTMovgest(null);

		return siacRMovgestOrdinativo;
	}

	public SiacDMovgestTipoFin getSiacDMovgestTipo() {
		return this.siacDMovgestTipo;
	}

	public void setSiacDMovgestTipo(SiacDMovgestTipoFin siacDMovgestTipo) {
		this.siacDMovgestTipo = siacDMovgestTipo;
	}

	public SiacTBilFin getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBilFin siacTBil) {
		this.siacTBil = siacTBil;
	}

	public List<SiacTMovgestTsFin> getSiacTMovgestTs() {
		return this.siacTMovgestTs;
	}

	public void setSiacTMovgestTs(List<SiacTMovgestTsFin> siacTMovgestTs) {
		this.siacTMovgestTs = siacTMovgestTs;
	}

	public SiacTMovgestTsFin addSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		getSiacTMovgestTs().add(siacTMovgestT);
		siacTMovgestT.setSiacTMovgest(this);

		return siacTMovgestT;
	}

	public SiacTMovgestTsFin removeSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		getSiacTMovgestTs().remove(siacTMovgestT);
		siacTMovgestT.setSiacTMovgest(null);

		return siacTMovgestT;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestId = uid;
	}

	/**
	 * @return the parereFinanziario
	 */
	public Boolean getParereFinanziario() {
		return parereFinanziario;
	}

	/**
	 * @param parereFinanziario the parereFinanziario to set
	 */
	public void setParereFinanziario(Boolean parereFinanziario) {
		this.parereFinanziario = parereFinanziario;
	}

	public Date getParereFinanziarioDataModifica() {
		return parereFinanziarioDataModifica;
	}

	public void setParereFinanziarioDataModifica(Date parereFinanziarioDataModifica) {
		this.parereFinanziarioDataModifica = parereFinanziarioDataModifica;
	}

	public String getParereFinanziarioLoginOperazione() {
		return parereFinanziarioLoginOperazione;
	}

	public void setParereFinanziarioLoginOperazione(
			String parereFinanziarioLoginOperazione) {
		this.parereFinanziarioLoginOperazione = parereFinanziarioLoginOperazione;
	}

}