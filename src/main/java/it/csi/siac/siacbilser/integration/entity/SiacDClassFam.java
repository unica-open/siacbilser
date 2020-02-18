/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_class_fam database table.
 * 
 */
@Entity
@Table(name="siac_d_class_fam")
public class SiacDClassFam extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The classif fam id. */
	@Id
	@SequenceGenerator(name="SIAC_D_CLASS_FAM_CLASSIFFAMID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CLASS_FAM_CLASSIF_FAM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CLASS_FAM_CLASSIFFAMID_GENERATOR")
	@Column(name="classif_fam_id")
	private Integer classifFamId;

	/** The classif fam code. */
	@Column(name="classif_fam_code")
	private String classifFamCode;

	/** The classif fam desc. */
	@Column(name="classif_fam_desc")
	private String classifFamDesc;

	//bi-directional many-to-one association to SiacTClassFamTree
	/** The siac t class fam trees. */
	@OneToMany(mappedBy="siacDClassFam")
	private List<SiacTClassFamTree> siacTClassFamTrees;

	/**
	 * Instantiates a new siac d class fam.
	 */
	public SiacDClassFam() {
	}

	/**
	 * Gets the classif fam id.
	 *
	 * @return the classif fam id
	 */
	public Integer getClassifFamId() {
		return this.classifFamId;
	}

	/**
	 * Sets the classif fam id.
	 *
	 * @param classifFamId the new classif fam id
	 */
	public void setClassifFamId(Integer classifFamId) {
		this.classifFamId = classifFamId;
	}

	/**
	 * Gets the classif fam code.
	 *
	 * @return the classif fam code
	 */
	public String getClassifFamCode() {
		return this.classifFamCode;
	}

	/**
	 * Sets the classif fam code.
	 *
	 * @param classifFamCode the new classif fam code
	 */
	public void setClassifFamCode(String classifFamCode) {
		this.classifFamCode = classifFamCode;
	}

	/**
	 * Gets the classif fam desc.
	 *
	 * @return the classif fam desc
	 */
	public String getClassifFamDesc() {
		return this.classifFamDesc;
	}

	/**
	 * Sets the classif fam desc.
	 *
	 * @param classifFamDesc the new classif fam desc
	 */
	public void setClassifFamDesc(String classifFamDesc) {
		this.classifFamDesc = classifFamDesc;
	}

	/**
	 * Gets the siac t class fam trees.
	 *
	 * @return the siac t class fam trees
	 */
	public List<SiacTClassFamTree> getSiacTClassFamTrees() {
		return this.siacTClassFamTrees;
	}

	/**
	 * Sets the siac t class fam trees.
	 *
	 * @param siacTClassFamTrees the new siac t class fam trees
	 */
	public void setSiacTClassFamTrees(List<SiacTClassFamTree> siacTClassFamTrees) {
		this.siacTClassFamTrees = siacTClassFamTrees;
	}

	/**
	 * Adds the siac t class fam tree.
	 *
	 * @param siacTClassFamTree the siac t class fam tree
	 * @return the siac t class fam tree
	 */
	public SiacTClassFamTree addSiacTClassFamTree(SiacTClassFamTree siacTClassFamTree) {
		getSiacTClassFamTrees().add(siacTClassFamTree);
		siacTClassFamTree.setSiacDClassFam(this);

		return siacTClassFamTree;
	}

	/**
	 * Removes the siac t class fam tree.
	 *
	 * @param siacTClassFamTree the siac t class fam tree
	 * @return the siac t class fam tree
	 */
	public SiacTClassFamTree removeSiacTClassFamTree(SiacTClassFamTree siacTClassFamTree) {
		getSiacTClassFamTrees().remove(siacTClassFamTree);
		siacTClassFamTree.setSiacDClassFam(null);

		return siacTClassFamTree;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return classifFamId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.classifFamId = uid;		
	}

}