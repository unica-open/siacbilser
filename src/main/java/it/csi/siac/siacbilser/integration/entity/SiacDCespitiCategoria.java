/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_cespiti_categoria database table.
 * 
 */
@Entity
@Table(name="siac_d_cespiti_categoria")
@NamedQuery(name="SiacDCespitiCategoria.findAll", query="SELECT s FROM SiacDCespitiCategoria s")
public class SiacDCespitiCategoria extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	
	@Id
	@SequenceGenerator(name="SIAC_D_CESPITI_CATEGORIAID_GENERATOR", allocationSize=1, sequenceName="siac_d_cespiti_categoria_cescat_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CESPITI_CATEGORIAID_GENERATOR")
	@Column(name="cescat_id")
	private Integer cescatId;

	@Column(name="cescat_code")
	private String cescatCode;

	@Column(name="cescat_desc")
	private String cescatDesc;

	//bi-directional many-to-one association to SiacTPdceConto
	@OneToMany(mappedBy="siacDCespitiCategoria")
	private List<SiacTPdceConto> siacTPdceContos;
	
	// bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;

//	@Column(name="aliquota_annua")
//	private BigDecimal aliquotaAnnua;
//	
//	@ManyToOne
//	@JoinColumn(name="cescat_calcolo_tipo_id")
//	private SiacDCespitiCategoriaCalcoloTipo siacDCespitiCategoriaCalcoloTipo;
	
	//bi-directional many-to-one association to SiacRVariazioneAttr
	/** The siac r variazione attrs. */
	@OneToMany(mappedBy="siacDCespitiCategoria", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCespitiCategoriaAliquotaCalcoloTipo> siacRCespitiCategoriaAliquotaCalcoloTipos;

	
	public SiacDCespitiCategoria() {
	}

	public Integer getCescatId() {
		return this.cescatId;
	}

	public void setCescatId(Integer cescatId) {
		this.cescatId = cescatId;
	}

	public String getCescatCode() {
		return this.cescatCode;
	}

	public void setCescatCode(String cescatCode) {
		this.cescatCode = cescatCode;
	}

	public String getCescatDesc() {
		return this.cescatDesc;
	}

	public void setCescatDesc(String cescatDesc) {
		this.cescatDesc = cescatDesc;
	}

	public List<SiacTPdceConto> getSiacTPdceContos() {
		return this.siacTPdceContos;
	}

	public void setSiacTPdceContos(List<SiacTPdceConto> siacTPdceContos) {
		this.siacTPdceContos = siacTPdceContos;
	}

	public SiacTPdceConto addSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		getSiacTPdceContos().add(siacTPdceConto);
		siacTPdceConto.setSiacDCespitiCategoria(this);

		return siacTPdceConto;
	}

	public SiacTPdceConto removeSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		getSiacTPdceContos().remove(siacTPdceConto);
		siacTPdceConto.setSiacDCespitiCategoria(null);

		return siacTPdceConto;
	}
	
	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}

	@Override
	public Integer getUid() {
		return this.cescatId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cescatId = uid;
	}

//	/**
//	 * @return the aliquotaAnnua
//	 */
//	public BigDecimal getAliquotaAnnua() {
//		return aliquotaAnnua;
//	}
//
//	/**
//	 * @param aliquotaAnnua the aliquotaAnnua to set
//	 */
//	public void setAliquotaAnnua(BigDecimal aliquotaAnnua) {
//		this.aliquotaAnnua = aliquotaAnnua;
//	}
//
//	/**
//	 * @return the SiacDCespitiCategoriaCalcoloTipo
//	 */
//	public SiacDCespitiCategoriaCalcoloTipo getSiacDCespitiCategoriaCalcoloTipo() {
//		return siacDCespitiCategoriaCalcoloTipo;
//	}
//
//	/**
//	 * @param SiacDCespitiCategoriaCalcoloTipo the SiacDCespitiCategoriaCalcoloTipo to set
//	 */
//	public void setSiacDCespitiCategoriaCalcoloTipo(SiacDCespitiCategoriaCalcoloTipo siacDCespitiCategoriaCalcoloTipo) {
//		this.siacDCespitiCategoriaCalcoloTipo = siacDCespitiCategoriaCalcoloTipo;
//	}

	/**
	 * @return the siacRCategoriaCespitiAliquotaCalcoloTipo
	 */
	public List<SiacRCespitiCategoriaAliquotaCalcoloTipo> getSiacRCategoriaCespitiAliquotaCalcoloTipos() {
		return siacRCespitiCategoriaAliquotaCalcoloTipos;
	}

	/**
	 * @param siacRCespitiCategoriaAliquotaCalcoloTipo the siacRCategoriaCespitiAliquotaCalcoloTipo to set
	 */
	public void setSiacRCespitiCategoriaAliquotaCalcoloTipos(List<SiacRCespitiCategoriaAliquotaCalcoloTipo> siacRCespitiCategoriaAliquotaCalcoloTipo) {
		this.siacRCespitiCategoriaAliquotaCalcoloTipos = siacRCespitiCategoriaAliquotaCalcoloTipo;
	}
	
	
	
	/**
	 * Adds the siac r variazione attr.
	 *
	 * @param siacRCategoriaCespitiAliquotaCalcoloTipo the siac r variazione attr
	 * @return the siac r variazione attr
	 */
	public SiacRCespitiCategoriaAliquotaCalcoloTipo addSiacRCategoriaCespitiAliquotaCalcoloTipo(SiacRCespitiCategoriaAliquotaCalcoloTipo siacRCategoriaCespitiAliquotaCalcoloTipo) {
		getSiacRCategoriaCespitiAliquotaCalcoloTipos().add(siacRCategoriaCespitiAliquotaCalcoloTipo);
		siacRCategoriaCespitiAliquotaCalcoloTipo.setSiacDCespitiCategoria(this);
		return siacRCategoriaCespitiAliquotaCalcoloTipo;
	}

	/**
	 * Removes the SiacRCategoriaCespitiAliquotaCalcoloTipo
	 *
	 * @param siacRCategoriaCespitiAliquotaCalcoloTipo the siac r variazione attr
	 * @return the siac r variazione attr
	 */
	public SiacRCespitiCategoriaAliquotaCalcoloTipo removeSiacRCategoriaCespitiAliquotaCalcoloTipo(SiacRCespitiCategoriaAliquotaCalcoloTipo siacRCategoriaCespitiAliquotaCalcoloTipo) {
		getSiacRCategoriaCespitiAliquotaCalcoloTipos().remove(siacRCategoriaCespitiAliquotaCalcoloTipo);
		siacRCategoriaCespitiAliquotaCalcoloTipo.setSiacDCespitiCategoria(null);
		return siacRCategoriaCespitiAliquotaCalcoloTipo;
	}

	
	
}