package sereneseasons.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

public class MaskItem extends ArmorItem {

    public MaskItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Item.Properties properties) {
        super(material, type, properties);  // 최신 생성자에 맞게 수정
    }
}
