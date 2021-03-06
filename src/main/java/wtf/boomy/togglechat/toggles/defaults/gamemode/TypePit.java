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

package wtf.boomy.togglechat.toggles.defaults.gamemode;

import wtf.boomy.togglechat.toggles.ToggleBase;

import java.util.regex.Pattern;

public class TypePit extends ToggleBase {

    private final Pattern pitPattern = Pattern
            .compile("(?<number>\\[\\d+]) (?<rank>\\[.+?] )?(?<player>\\S{1,16}): (?<message>.*)");

    @Override
    public String getName() {
        return "The_Pit";
    }

    @Override
    public String getDisplayName() {
        return "The Pit: %s";
    }

    @Override
    public boolean shouldToggle(String message) {
        return this.pitPattern.matcher(message).matches();
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Toggles all pit",
                "chat from players, regardless of",
                "their rank. (Supports numbers over 120)",
                "",
                "Message format",
                "&7[1] Player&r: Hi",
                "&7[&5&b90&7] &a[VIP] Player&r: Hi",
                "&7[&b&l120&7] &b[MVP] Player&r: Hi"
        };
    }
}
