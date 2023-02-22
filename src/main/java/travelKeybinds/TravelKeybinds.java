package travelKeybinds;

import necesse.engine.control.Control;
import necesse.engine.localization.Localization;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.network.client.Client;
import necesse.engine.registries.ItemRegistry;
import necesse.inventory.PlayerInventoryManager;
import necesse.inventory.PlayerInventorySlot;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.lwjgl.glfw.GLFW;

@ModEntry
public class TravelKeybinds {
    public static Control recallControl;
    public static Control travelControl;
    private static Set<Integer> recallItemIds;
    private static Set<Integer> travelItemIds;

    public void init() {
        recallItemIds = Set.of(ItemRegistry.getItemID("recallscroll"),
                ItemRegistry.getItemID("recallflask"));
        travelItemIds = Set.of(ItemRegistry.getItemID("travelscroll"));

        recallControl = Control.addModControl(new Control(GLFW.GLFW_KEY_H, "recall"));
        travelControl = Control.addModControl(new Control(-1, "travel"));
    }

    public static void recall(Client client) {
        if (client.getPlayer() == null) {
            return;
        }
        PlayerInventoryManager invManager = client.getPlayer().getInv();
        Optional<PlayerInventorySlot> recallSlot = invManager.streamSlots(true, true, true)
                .filter(new ItemIdSearch(invManager, recallItemIds)).findFirst();
        if (recallSlot.isPresent()) {
            client.getPlayer().tryAttack(recallSlot.get(), 0, 0);
        } else {
            client.chat.addMessage(Localization.translate("chat", "norecallitems"));
        }
    }

    public static void travel(Client client) {
        if (client.getPlayer() == null) {
            return;
        }
        PlayerInventoryManager invManager = client.getPlayer().getInv();
        Optional<PlayerInventorySlot> recallSlot = invManager.streamSlots(true, true, true)
                .filter(new ItemIdSearch(invManager, travelItemIds)).findFirst();
        if (recallSlot.isPresent()) {
            client.getPlayer().tryAttack(recallSlot.get(), 0, 0);
        } else {
            client.chat.addMessage(Localization.translate("chat", "notravelitems"));
        }
    }

    private static class ItemIdSearch implements Predicate<PlayerInventorySlot> {
        private PlayerInventoryManager invManager;
        private Set<Integer> itemIds;

        private ItemIdSearch(PlayerInventoryManager invManager, Set<Integer> itemIds) {
            this.invManager = invManager;
            this.itemIds = itemIds;
        }

        @Override
        public boolean test(PlayerInventorySlot slot) {
            if (slot.isSlotClear(invManager)) {
                return false;
            }
            return itemIds.contains(slot.getItem(invManager).item.getID());
        }
    }
}
