/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.jinkeloid.mispd.items.bags;

import com.jinkeloid.mispd.Badges;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.Char;
import com.jinkeloid.mispd.actors.hero.Hero;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.items.Item;
import com.jinkeloid.mispd.scenes.GameScene;
import com.jinkeloid.mispd.windows.WndBag;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Iterator;

public class Bag extends Item implements Iterable<Item> {

	public int emptySlot = 0;

	public static final String AC_OPEN	= "OPEN";
	
	{
		image = 11;
		
		defaultAction = AC_OPEN;

		unique = true;
	}
	
	public Char owner;
	
	public ArrayList<Item> items = new ArrayList<>();

	public int capacity(){
		Perk.onInventoryTrigger();
		return (Dungeon.hero.hasPerk(Perk.ORGANIZED) ? 25 :
				Dungeon.hero.hasPerk(Perk.DISORGANIZED) ? 15 : 20) - emptySlot;

	}

	//because backpack hide other types of bags, we have to count how many bags are hiding there to enable dynamic inventory
	public int bagCount(){
		int count = 0;
		for (Item item : items){
			if (item instanceof Bag){
				count++;
			}
		}
		return count;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_OPEN )) {
			
			GameScene.show( new WndBag( this, null, WndBag.Mode.ALL, null ) );
			
		}
	}
	
	@Override
	public boolean collect( Bag container, boolean isLoaded ) {

		grabItems(container);

		if (super.collect( container, isLoaded)) {
			
			owner = container.owner;
			
			Badges.validateAllBagsBought( this );
			
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDetach( ) {
		this.owner = null;
		for (Item item : items)
			Dungeon.quickslot.clearItem(item);
		updateQuickslot();
	}

	public void grabItems(){
		if (owner != null && owner instanceof Hero && this != ((Hero) owner).belongings.backpack) {
			grabItems(((Hero) owner).belongings.backpack);
		}
	}

	public void grabItems( Bag container ){
		for (Item item : container.items.toArray( new Item[0] )) {
			if (canHold( item )) {
				int slot = Dungeon.quickslot.getSlot(item);
				item.detachAll(container);
				if (!item.collect(this, false)) {
					item.collect(container, false);
				}
				if (slot != -1) {
					Dungeon.quickslot.setSlot(slot, item);
				}
			}
		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	public void clear() {
		items.clear();
	}
	
	public void resurrect() {
		for (Item item : items.toArray(new Item[0])){
			if (!item.unique) items.remove(item);
		}
	}
	
	private static final String ITEMS	= "inventory";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ITEMS, items );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		for (Bundlable item : bundle.getCollection( ITEMS )) {
			if (item != null) ((Item)item).collect( this, true );
		}
	}
	
	public boolean contains( Item item ) {
		for (Item i : items) {
			if (i == item) {
				return true;
			} else if (i instanceof Bag && ((Bag)i).contains( item )) {
				return true;
			}
		}
		return false;
	}

	public boolean canHold( Item item ){
		if (items.contains(item) || item instanceof Bag || items.size() < capacity()){
			return true;
		} else if (item.stackable) {
			for (Item i : items) {
				if (item.isSimilar( i )) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		private Iterator<Item> nested = null;
		
		@Override
		public boolean hasNext() {
			if (nested != null) {
				return nested.hasNext() || index < items.size();
			} else {
				return index < items.size();
			}
		}

		@Override
		public Item next() {
			if (nested != null && nested.hasNext()) {
				
				return nested.next();
				
			} else {
				
				nested = null;
				
				Item item = items.get( index++ );
				if (item instanceof Bag) {
					nested = ((Bag)item).iterator();
				}
				
				return item;
			}
		}

		@Override
		public void remove() {
			if (nested != null) {
				nested.remove();
			} else {
				items.remove( index );
			}
		}
	}
}
