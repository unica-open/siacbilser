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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_class_fam_tree database table.
 * 
 */
@Entity
@Table(name="siac_t_class_fam_tree")
public class SiacTClassFamTree extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The classif fam tree id. */
	@Id
	@SequenceGenerator(name="SIAC_T_CLASS_FAM_TREE_CLASSIFFAMTREEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CLASS_FAM_TREE_CLASSIF_FAM_TREE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CLASS_FAM_TREE_CLASSIFFAMTREEID_GENERATOR")
	@Column(name="classif_fam_tree_id")
	private Integer classifFamTreeId;

	/** The class fam code. */
	@Column(name="class_fam_code")
	private String classFamCode;

	/** The class fam desc. */
	@Column(name="class_fam_desc")
	private String classFamDesc;

	//bi-directional many-to-one association to SiacRClassFamTree
	/** The siac r class fam trees. */
	@OneToMany(mappedBy="siacTClassFamTree")
	private List<SiacRClassFamTree> siacRClassFamTrees;

	//bi-directional many-to-one association to SiacDClassFam
	/** The siac d class fam. */
	@ManyToOne
	@JoinColumn(name="classif_fam_id")
	private SiacDClassFam siacDClassFam;

	/**
	 * Instantiates a new siac t class fam tree.
	 */
	public SiacTClassFamTree() {
	}

	/**
	 * Gets the classif fam tree id.
	 *
	 * @return the classif fam tree id
	 */
	public Integer getClassifFamTreeId() {
		return this.classifFamTreeId;
	}

	/**
	 * Sets the classif fam tree id.
	 *
	 * @param classifFamTreeId the new classif fam tree id
	 */
	public void setClassifFamTreeId(Integer classifFamTreeId) {
		this.classifFamTreeId = classifFamTreeId;
	}

	/**
	 * Gets the class fam code.
	 *
	 * @return the class fam code
	 */
	public String getClassFamCode() {
		return this.classFamCode;
	}

	/**
	 * Sets the class fam code.
	 *
	 * @param classFamCode the new class fam code
	 */
	public void setClassFamCode(String classFamCode) {
		this.classFamCode = classFamCode;
	}

	/**
	 * Gets the class fam desc.
	 *
	 * @return the class fam desc
	 */
	public String getClassFamDesc() {
		return this.classFamDesc;
	}

	/**
	 * Sets the class fam desc.
	 *
	 * @param classFamDesc the new class fam desc
	 */
	public void setClassFamDesc(String classFamDesc) {
		this.classFamDesc = classFamDesc;
	}

	
	/**
	 * Gets the siac r class fam trees.
	 *
	 * @return the siac r class fam trees
	 */
	public List<SiacRClassFamTree> getSiacRClassFamTrees() {
		return this.siacRClassFamTrees;
	}

	/**
	 * Sets the siac r class fam trees.
	 *
	 * @param siacRClassFamTrees the new siac r class fam trees
	 */
	public void setSiacRClassFamTrees(List<SiacRClassFamTree> siacRClassFamTrees) {
		this.siacRClassFamTrees = siacRClassFamTrees;
	}

	/**
	 * Adds the siac r class fam tree.
	 *
	 * @param siacRClassFamTree the siac r class fam tree
	 * @return the siac r class fam tree
	 */
	public SiacRClassFamTree addSiacRClassFamTree(SiacRClassFamTree siacRClassFamTree) {
		getSiacRClassFamTrees().add(siacRClassFamTree);
		siacRClassFamTree.setSiacTClassFamTree(this);

		return siacRClassFamTree;
	}

	/**
	 * Removes the siac r class fam tree.
	 *
	 * @param siacRClassFamTree the siac r class fam tree
	 * @return the siac r class fam tree
	 */
	public SiacRClassFamTree removeSiacRClassFamTree(SiacRClassFamTree siacRClassFamTree) {
		getSiacRClassFamTrees().remove(siacRClassFamTree);
		siacRClassFamTree.setSiacTClassFamTree(null);

		return siacRClassFamTree;
	}

	/**
	 * Gets the siac d class fam.
	 *
	 * @return the siac d class fam
	 */
	public SiacDClassFam getSiacDClassFam() {
		return this.siacDClassFam;
	}

	/**
	 * Sets the siac d class fam.
	 *
	 * @param siacDClassFam the new siac d class fam
	 */
	public void setSiacDClassFam(SiacDClassFam siacDClassFam) {
		this.siacDClassFam = siacDClassFam;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return classifFamTreeId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.classifFamTreeId = uid;
		
	}


}