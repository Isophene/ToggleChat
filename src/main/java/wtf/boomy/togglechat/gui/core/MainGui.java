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

package wtf.boomy.togglechat.gui.core;

import wtf.boomy.togglechat.gui.custom.MainCustomUI;
import wtf.boomy.togglechat.utils.uis.ModernButton;
import wtf.boomy.togglechat.utils.uis.ModernGui;
import wtf.boomy.togglechat.utils.uis.gui.ModernConfigGui;
import wtf.boomy.togglechat.gui.list.ViewListUI;
import wtf.boomy.togglechat.toggles.ToggleBase;
import wtf.boomy.togglechat.toggles.custom.ICustomToggle;
import wtf.boomy.togglechat.toggles.dummy.ToggleDummyMessage;
import wtf.boomy.togglechat.utils.ChatColor;

import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

public class MainGui extends ModernGui {

    //        - 99
    //        - 75
    //        - 51
    //        - 27
    //        - 3
    //        + 21
    //        + 45
    //        + 69

    private boolean favouritesChanged = false;
    private boolean nothingFound = false;
    private boolean changed = false;

    private int pages;
    private int pageNumber;
    private int pagesTotal;

    public MainGui(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public void initGui() {
        Map<String, ToggleBase> toggles = this.mod.getToggleHandler().getToggles();
        
        this.pagesTotal = (int) Math.ceil((double) toggles.size() / 7D);
        
        if (toggles.values().size() > 0) {
            this.nothingFound = false;

            this.pages = (int) Math.ceil((double) toggles.size() / 7D);

            if (this.pageNumber < 1 || this.pageNumber > pages) {
                this.pageNumber = 1;
            }

            final int[] position = {this.height / 2 - 75};

            Comparator<ToggleBase> sorter = this.mod.getConfigLoader().getSortType().getSorter();
    
            toggles.values().stream().sorted(sorter).skip((this.pageNumber - 1) * 7).limit(7)
                    .forEach(baseType -> {
                        ModernButton button = new ModernButton(0, baseType.getIdString(),
                                this.width / 2 - 75, position[0], 150, 20,
                                String.format(baseType.getDisplayName(), getStatus(baseType.isEnabled())))
                                .setButtonData(baseType);
                        if (baseType instanceof ICustomToggle) {
                            button = button.setEnabledColor(new Color(100, 88, 192, 75)).setDisabledColor(new Color(67, 67, 133, 75));
                        }

                        button.setFavourite(baseType.isFavourite());

                        this.buttonList.add(button);
                        position[0] += 24;
                    });

            this.buttonList.add(new ModernButton(1, "inbuilt_whitelist", 5, this.height - 49, 90, 20, "Whitelist"));
            this.buttonList.add(new ModernButton(2, "inbuilt_back", this.width - 114, this.height - 25, 50, 20, "\u21E6").setEnabled(this.pageNumber > 1));
            this.buttonList.add(new ModernButton(3, "inbuilt_next", this.width - 60, this.height - 25, 50, 20, "\u21E8").setEnabled(this.pageNumber != pages));
            this.buttonList.add(new ModernButton(4, "inbuilt_theme", 5, 5, 20, 20, "\u2699")
                    .setButtonData(
                            // Let them know what this button does
                            new ToggleDummyMessage(
                                    "Opens the",
                                    "&bTheme Modifier&r,",
                                    "containing options which",
                                    "customization the",
                                    "look of the mod"
                            )
                    ));

            String sort_string = "Sort: " + ChatColor.AQUA + this.mod.getConfigLoader().getSortType().getDisplayName();
            ToggleDummyMessage dummyMessage = new ToggleDummyMessage();

            addSortMessageData(dummyMessage);

            this.buttonList.add(new ModernButton(5, "inbuilt_sort", 5, this.height - 25, 90, 20, sort_string).setButtonData(dummyMessage));

            this.buttonList.add(new ModernButton(6, this.width - 114, this.height - 49, 104, 20,
                    "Custom Toggles").setEnabledColor(new Color(100, 88, 192, 75))
                    .setDisabledColor(new Color(67, 67, 133, 75)).setButtonData(
                            new ToggleDummyMessage(
                                    "Allows you to add",
                                    "your own custom",
                                    "toggles to the mod",
                                    "",
                                    "This feature is still",
                                    "in &cbeta&r and may be",
                                    "changed at any time",
                                    "",
                                    "Brought to you by",
                                    "&6OrangeMarshall"
                            )
                    ));
            return;
        }
        this.nothingFound = true;
    }

    @Override
    public void drawScreen(int x, int y, float ticks) {
        drawDefaultBackground();

        if (this.nothingFound) {
            drawCenteredString(this.fontRendererObj, "An issue occurred whilst loading ToggleChat!", this.width / 2, this.height / 2 - 50, Color.WHITE.getRGB());
            drawCenteredString(this.fontRendererObj, "Buttons have not loaded correctly", this.width / 2, this.height / 2 - 30, Color.WHITE.getRGB());
            drawCenteredString(this.fontRendererObj, "Please reinstall the mod!", this.width / 2, this.height / 2, Color.WHITE.getRGB());
            return;
        } else {
            drawCenteredString(this.fontRendererObj, "Page " + this.pageNumber + "/" + this.pagesTotal, this.width / 2, this.height / 2 - 94, Color.WHITE.getRGB());
        }

        super.drawScreen(x, y, ticks);

        checkHover(this.height / 2 - 75);
    }

    @Override
    public void buttonPressed(ModernButton button) {
        switch (button.getId()) {
            case 1:
                new ViewListUI().display();
                return;
            case 2:
                this.mc.displayGuiScreen(new MainGui(this.pageNumber - 1));
                return;
            case 3:
                this.mc.displayGuiScreen(new MainGui(this.pageNumber + 1));
                return;
            case 4:
                this.mc.displayGuiScreen(new ModernConfigGui(this));
                return;
            case 5:
                this.mod.getConfigLoader().setSortType(this.mod.getConfigLoader().getSortType().getNextSortType());

                ToggleBase inbuiltData = button.getButtonData();

                if (inbuiltData instanceof ToggleDummyMessage) {
                    ToggleDummyMessage dummyMessage = (ToggleDummyMessage) inbuiltData;

                    dummyMessage.clearLines();

                    addSortMessageData(dummyMessage);
                }
                
                this.mod.getConfigLoader().saveModernUtils();

                this.mc.displayGuiScreen(new MainGui(this.pageNumber));
                return;
            case 6:
                this.mc.displayGuiScreen(new MainCustomUI());
                return;
        }

        // Make sure the id is 0 to prevent other buttons being pressed
        if (button.getId() == 0) {
            if (button.hasButtonData()) {
                ToggleBase base = button.getButtonData();

                base.setEnabled(!base.isEnabled());
                button.setText(String.format(base.getDisplayName(), getStatus(base.isEnabled())));

                this.changed = true;
            }
        }
    }

    @Override
    public void rightClicked(ModernButton button) {
        if (!button.hasButtonData()) {
            return;
        }

        ToggleBase base = button.getButtonData();
        
        // Patch for non-toggle buttons.
        if (base instanceof ToggleDummyMessage) {
            return;
        }

        base.setFavourite(!base.isFavourite());
        button.setFavourite(base.isFavourite());

        this.favouritesChanged = true;

//            if (base instanceof TypeCustom) {
//                TypeCustom custom = (TypeCustom) base;
//                if (custom.getIdString().equals(button.getButtonId())) {
//                    this.mc.displayGuiScreen(new CustomToggleModify(this, custom));
//                }
//            }
    }

    @Override
    public void onGuiClosed() {
        if (this.changed) {
            this.mod.getConfigLoader().saveToggles();
        }

        if (this.favouritesChanged) {
    
            this.mod.getConfigLoader().getFavourites().clear();
    
            for (ToggleBase t : this.mod.getToggleHandler().getToggles().values()) {
                if (t.isFavourite()) {
                    this.mod.getConfigLoader().getFavourites().add(t.getIdString());
                }
            }

            this.mod.getConfigLoader().saveModernUtils();
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        if (this.nothingFound) {
            return;
        }

        int i = Mouse.getEventDWheel();

        if (i < 0 && this.pageNumber > 1) {
            this.mc.displayGuiScreen(new MainGui(this.pageNumber - 1));
        } else if (i > 0 && this.pageNumber != this.pages) {
            this.mc.displayGuiScreen(new MainGui(this.pageNumber + 1));
        }
    }

    private void addSortMessageData(ToggleDummyMessage dummyMessage) {
        dummyMessage.appendLine("Changes the sorting");
        dummyMessage.appendLine("of the toggles so some");
        dummyMessage.appendLine("are easier to find");
        dummyMessage.appendLine(" ");
        
        String desc = this.mod.getConfigLoader().getSortType().getDescription();

        if (desc != null) {
            if (desc.contains("\n")) {
                for (String line : desc.split("\n")) {
                    dummyMessage.appendLine(line);
                }
            } else {
                dummyMessage.appendLine(desc);
            }
        }
    }
}
