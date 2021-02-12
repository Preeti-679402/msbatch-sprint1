package com.superleague.inventory.demo.config;
 
import org.springframework.batch.item.ItemProcessor;

import com.superleague.inventory.demo.model.Inventory;


 
public class DBLogProcessor implements ItemProcessor<Inventory, Inventory>
{
    public Inventory process(Inventory Inventory) throws Exception
    {
        System.out.println("Inserting Inventory : " + Inventory);
        return Inventory;
    }
}