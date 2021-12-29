package com.shop.common.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 128, nullable = false, unique = true)
	private String name;

	@Column(length = 64, nullable = false, unique = true)
	private String alias;

	@Column(length = 128, nullable = false)
	private String image;

	private boolean enabled;

	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent")
	private Set<Category> children = new HashSet<>();

	public Category() {
	}

	public static Category copyIdAndName(Category category) {
		Category copyCat = new Category();
		copyCat.setId(category.getId());
		copyCat.setName(category.getName());
		return copyCat;
	}

	public static Category copyIdAndName(Integer id, String name) {
		Category copyCat = new Category();
		copyCat.setId(id);
		copyCat.setName(name);
		return copyCat;
	}

	public static Category CopyFull(Category category) {
		Category copyCat = new Category();
		copyCat.setId(category.getId());
		copyCat.setName(category.getName());
		copyCat.setImage(category.getImage());
		copyCat.setAlias(category.getAlias());
		copyCat.setEnabled(category.isEnabled());
		copyCat.setHasChildren(category.getChildren().size() > 0);
		return copyCat;
	}

	public static Category CopyFull(Category category, String name) {
		Category copyCat = CopyFull(category);
		copyCat.setName(name);
		return copyCat;
	}

	public Category(Integer id) {
		this.id = id;
	}

	public Category(String name) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}

	public Category(Integer id, String name, String alias) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
	}

	public Category(String name, Category parent) {
		this(name);
		this.parent = parent;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	@Transient
	public String getImagePath() {
		if (this.id == null)
			return "/images/image-thumbnail.png";
		return "/category-images/" + this.id + "/" + this.image;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@Transient
	private boolean hasChildren;

	@Override
	public String toString() {
			return this.name;
	}
}
