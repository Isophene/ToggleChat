/*
 *     Copyright (C) 2021 boomboompower
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package wtf.boomy.togglechat.gui.custom;

import wtf.boomy.togglechat.utils.uis.ModernButton;
import wtf.boomy.togglechat.utils.uis.ModernGui;
import wtf.boomy.togglechat.toggles.custom.TypeCustom;
import wtf.boomy.togglechat.utils.ChatColor;

import java.awt.Color;
import java.io.IOException;

public class ModifyCustomUI extends CustomUI {
    
    private final TypeCustom custom;
    
    private final ModernGui previous;
    
    public ModifyCustomUI(ModernGui previous, TypeCustom customIn) {
        this.previous = previous;
        this.custom = customIn;
    }
    
    @Override
    public void initGui() {
        generateScreen();

        this.buttonList.add(new ModernButton(0, 5, this.height - 25, 75, 20, "Back"));
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        
        drawCenteredString("Modifying " + ChatColor.GOLD + this.custom._getName(), this.width / 2, 13, new Color(255, 255, 255).getRGB());
        
        drawCenteredString("Click on the toggle you would like to modify!", this.width / 2, this.height - 17, new Color(255, 255, 255).getRGB());
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void onGuiClosed() {
        //ToggleChatMod.getInstance().getConfigLoader().saveCustomToggles();
    }
    
    @Override
    public void buttonPressed(ModernButton button) {
        switch (button.getId()) {
            case 0:
                this.mc.displayGuiScreen(this.previous);
                break;
        }
    }
    
    @Override
    public TypeCustom getCustomToggle() {
        return this.custom;
    }

    private void generateScreen() {


        this.custom._getConditions();
    }
}
