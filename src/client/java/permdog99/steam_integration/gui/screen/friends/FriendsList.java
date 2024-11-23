package permdog99.steam_integration.gui.screen.friends;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

public class FriendsList extends ObjectSelectionList<FriendsList.Entry> {


    public FriendsList(Minecraft minecraft, int i, int j, int k, int l, int m) {
        super(minecraft, i, j, k, l, m);
    }

    public abstract static class Entry extends ObjectSelectionList.Entry<Entry> implements AutoCloseable {
        public Entry() {
        }

        public void close() {
        }
    }

    public class JoinableFriendsEntry extends Entry {

        @Override
        public Component getNarration() {
            return null;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {

        }
    }
}
