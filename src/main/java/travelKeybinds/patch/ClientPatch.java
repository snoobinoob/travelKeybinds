package travelKeybinds.patch;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.state.MainGame;
import necesse.engine.tickManager.TickManager;
import net.bytebuddy.asm.Advice;
import travelKeybinds.TravelKeybinds;

public class ClientPatch {
    @ModMethodPatch(target = MainGame.class, name = "frameTick", arguments = {TickManager.class})
    public static class FrameTickPatch {
        @Advice.OnMethodExit
        public static void update(@Advice.This MainGame mainGame) {
            if (TravelKeybinds.recallControl.isPressed()) {
                TravelKeybinds.recall(mainGame.getClient());
            }
            if (TravelKeybinds.travelControl.isPressed()) {
                TravelKeybinds.travel(mainGame.getClient());
            }
        }
    }
}
