package travelKeybinds.patch;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.inventory.PlayerInventoryManager;
import necesse.inventory.item.Item;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;

public class PlayerInventoryManagerPatch {
    @ModMethodPatch(target = PlayerInventoryManager.class, name = "removeItems", arguments = {
            Item.class, int.class, boolean.class, boolean.class, boolean.class, boolean.class, String.class})
    public static class RemoveItemsPatch {
        @Advice.OnMethodEnter
        public static void onEnter(@Advice.AllArguments(readOnly = false, typing = Typing.DYNAMIC) Object[] arguments) {
            if (!"travelscroll".equals(arguments[6])) {
                return;
            }

            // Do some juggling so bytebuddy knows to update inputs
            Object[] _args = arguments;
            _args[3] = true;
            _args[4] = true;
            _args[5] = true;
            arguments = _args;
        }
    }
}
