package com.superleague.inventory.demo.model;


import java.sql.Date;

public class Inventory {
	
	private Long id;
	private String name;
	private Integer quantity;
	private Integer price;
	private String inDate;

	public Inventory()
	{
		
	}

	public Inventory(Long id, String name, Integer quantity, Integer price, String inDate) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.inDate = inDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	@Override
	public String toString() {
		return "Hotels [id=" + id + ", name=" + name + ", quantity=" + quantity + ", price=" + price + ", inDate="
				+ inDate + "]";
	}
	
	

}
