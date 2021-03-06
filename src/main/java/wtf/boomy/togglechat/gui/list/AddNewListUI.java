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

package wtf.boomy.togglechat.gui.list;

import wtf.boomy.togglechat.utils.uis.ModernButton;
import wtf.boomy.togglechat.utils.uis.ModernGui;
import wtf.boomy.togglechat.utils.ChatColor;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.io.IOException;

public class AddNewListUI extends ModernGui {

    private ModernButton next;

    private ModernGui previousScreen;

    private boolean pageInvalid;
    
    private int pageNumber;
    private int pages;

    public AddNewListUI(ModernGui previous, int pageNumber) {
        this.previousScreen = previous;
        this.pageNumber = pageNumber;
        this.pageInvalid = false;
    }

    @Override
    public void initGui() {
        if (this.mod.getConfigLoader().getWhitelist().size() > 0) {
            this.buttonList.add(new ModernButton(0, this.width / 2 - 200, this.height / 2 + 80, 150, 20, "Back"));
            this.buttonList.add(this.next = new ModernButton(1, this.width / 2 + 50, this.height / 2 + 80, 150, 20, "Next"));
        } else {
            this.buttonList.add(new ModernButton(0, this.width / 2 - 75, this.height / 2 + 50, 150, 20, "Back"));
        }
    }

    @Override
    public void drawScreen(int x, int y, float ticks) {
        drawDefaultBackground();

        drawCenteredString(this.fontRendererObj, "Whitelist Entries", this.width / 2, this.height / 2 - 105, Color.WHITE.getRGB());

        super.drawScreen(x, y, ticks);

        if (this.mod.getConfigLoader().getWhitelist().size() > 0 && !this.pageInvalid) {
            drawRect(this.width / 2 - 60, this.height / 2 - 80, this.width / 2 + 60, this.height / 2 + 60, new Color(105, 105, 105, 75).getRGB());

            drawHorizontalLine(this.width / 2 - 60, width / 2 + 60, this.height / 2 - 80, Color.WHITE.getRGB());
            drawHorizontalLine(this.width / 2 - 60, width / 2 + 60, this.height / 2 + 60, Color.WHITE.getRGB());

            drawVerticalLine(this.width / 2 - 60, this.height / 2 - 80, this.height / 2 + 60, Color.WHITE.getRGB());
            drawVerticalLine(this.width / 2 + 60, this.height / 2 - 80, this.height / 2 + 60, Color.WHITE.getRGB());
        }

        if (this.mod.getConfigLoader().getWhitelist().size() > 0) {

            int totalEntries = this.mod.getConfigLoader().getWhitelist().size();
            
            this.pages = (int) Math.ceil((double) this.mod.getConfigLoader().getWhitelist().size() / 10D);

            if (this.pageNumber < 1 || this.pageNumber > pages) {
                writeInformation(this.width / 2, this.height / 2 - 40, 20, String.format(ChatColor.RED + "Invalid page number (%s)", (ChatColor.DARK_RED + String.valueOf(pageNumber) + ChatColor.RED)));
                this.pageInvalid = true;
                return;
            }

            this.pageInvalid = false;
            this.next.setEnabled(this.pageNumber != pages); // Next

            drawCenteredString(String.format("Page %s/%s", (this.pageNumber), pages), this.width / 2, this.height / 2 - 95, Color.WHITE.getRGB());
            drawCenteredString(String.format("There is a total of %s %s on the whitelist!", ChatColor.GOLD + String.valueOf(totalEntries), (totalEntries > 1 ? "entries" : "entry") + ChatColor.RESET), this.width / 2, this.height / 2 + 65, Color.WHITE.getRGB());

            final int[] position = {this.height / 2 - 73};

            this.mod.getConfigLoader().getWhitelist().stream().skip((this.pageNumber - 1) * 10).limit(10).forEach(word -> {
                drawCenteredString(word, this.width / 2, position[0], Color.WHITE.getRGB());
                position[0] += 13;
            });

            return;
        }
        
        this.pageInvalid = true;

        writeInformation(this.width / 2, this.height / 2 - 50, 20, "There are no entries on the whitelist!", "Insert some words then return to this page!");
    }

    @Override
    public void buttonPressed(ModernButton button) {
        switch (button.getId()) {
            case 0:
                if (this.pageNumber > 1) {
                    new AddNewListUI(this, this.pageNumber--);
                } else {
                    this.mc.displayGuiScreen(previousScreen);
                }
                break;
            case 1:
                new AddNewListUI(this, this.pageNumber++);
                break;
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        
        if (this.pageInvalid) {
            return;
        }
        
        int i = Mouse.getEventDWheel();
    
        if (i < 0 && this.pageNumber > 1) {
            new AddNewListUI(this, this.pageNumber--);
        } else if (i > 0 && this.pageNumber != this.pages) {
            new AddNewListUI(this, this.pageNumber++);
        }
    }
}
