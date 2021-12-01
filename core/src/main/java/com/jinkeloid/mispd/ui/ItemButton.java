package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.items.Item;
import com.jinkeloid.mispd.sprites.ItemSprite;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;

public class ItemButton extends Button {

    protected Image buy_tag;
    protected Image sell_tag;
    protected ItemSprite itemSprite;
    public int price;
    public int itemCount;
    public Item item;

    public ItemButton(Image buy_tag, Image sell_tag, ItemSprite itemSprite, int price, int itemCount, Item item) {
        this.buy_tag = buy_tag;
        this.sell_tag = sell_tag;
        this.itemSprite = itemSprite;
        this.price = price;
        this.itemCount = itemCount;
        this.item = item;
    }

    @Override
    protected void layout(){
        
    }
}
