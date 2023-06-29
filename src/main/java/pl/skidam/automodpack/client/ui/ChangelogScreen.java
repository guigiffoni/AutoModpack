/*
 * This file is part of the AutoModpack project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023 Skidam and contributors
 *
 * AutoModpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AutoModpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AutoModpack.  If not, see <https://www.gnu.org/licenses/>.
 */

package pl.skidam.automodpack.client.ui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import pl.skidam.automodpack.client.ModpackUpdater;
import pl.skidam.automodpack.client.audio.AudioManager;
import pl.skidam.automodpack.client.ui.versioned.VersionedText;
import pl.skidam.automodpack.client.ui.versioned.VersionedMatrices;
import pl.skidam.automodpack.client.ui.versioned.VersionedScreen;
import pl.skidam.automodpack.client.ui.widget.ListEntryWidget;
import pl.skidam.automodpack.config.ConfigTools;
import pl.skidam.automodpack.config.Jsons;
import pl.skidam.automodpack.utils.ModpackContentTools;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChangelogScreen extends VersionedScreen {
    private static List<String> changelogs;
    private final Screen parent;
    private final Path modpackDir;
    private ListEntryWidget listEntryWidget;
    private TextFieldWidget searchField;
    private ButtonWidget backButton;

    public ChangelogScreen(Screen parent, Path modpackDir) {
        super(VersionedText.common.literal("ChangelogScreen"));
        this.parent = parent;
        this.modpackDir = modpackDir;

        if (AudioManager.isMusicPlaying()) {
            AudioManager.stopMusic();
        }
    }

    @Override
    protected void init() {
        super.init();

        changelogs = getChangelogs();

        initWidgets();

        this.addDrawableChild(this.listEntryWidget);
        this.addDrawableChild(this.searchField);
        this.addDrawableChild(this.backButton);
        this.setInitialFocus(this.searchField);
    }

    private void initWidgets() {
        this.listEntryWidget = new ListEntryWidget(changelogs, this.client, this.width, this.height, 48, this.height - 50, 20);

        this.searchField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 20, 200, 20,
                VersionedText.common.literal("")
        );
        this.searchField.setChangedListener((textField) -> updateChangelogs()); // Update the changelogs display based on the search query

        this.backButton = VersionedText.buttonWidget(10, this.height - 30, 72, 20,
                VersionedText.common.translatable("automodpack.back"),
                button -> this.client.setScreen(this.parent)
        );
    }

    @Override
    public void versionedRender(VersionedMatrices matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        this.listEntryWidget.render(matrices, mouseX, mouseY, delta);

        // Draw summary of added/removed mods
        drawSummaryOfChanges(matrices);
    }

    private void drawSummaryOfChanges(VersionedMatrices matrices) {

        Path modpackContentFile = ModpackContentTools.getModpackContentFile(modpackDir);

        if (modpackContentFile == null) return;

        Jsons.ModpackContentFields modpackContent = ConfigTools.loadModpackContent(modpackContentFile);

        int modsAdded = 0;
        int modsRemoved = 0;
        if (modpackContent == null) return;
        for (Map.Entry<String, Boolean> changelog : ModpackUpdater.changelogList.entrySet()) {
            String fileType = ModpackContentTools.getFileType(changelog.getKey(), modpackContent);
            if (fileType.equals("mod")) {
                if (changelog.getValue()) {
                    modsAdded++;
                } else {
                    modsRemoved++;
                }
            }
        }

        String summary = "Mods + " + modsAdded + " | - " + modsRemoved;

        VersionedText.drawCenteredTextWithShadow(matrices, textRenderer, VersionedText.common.literal(summary), this.width / 2, 5, 16777215);
    }

    private void updateChangelogs() {
        // If the search field is empty, reset the changelogs to the original list
        if (this.searchField.getText().isEmpty()) {
            changelogs = getChangelogs();
        } else {
            // Filter the changelogs based on the search query using a case-insensitive search
            List<String> filteredChangelogs = new ArrayList<>();
            for (String changelog : getChangelogs()) {
                if (changelog.toLowerCase().contains(this.searchField.getText().toLowerCase())) {
                    filteredChangelogs.add(changelog);
                }
            }
            changelogs = filteredChangelogs;
        }

        // remove method is only available in 1.17+
//#if MC >= 11700
        this.remove(this.listEntryWidget);
        this.remove(this.backButton);
//#endif

        this.listEntryWidget = new ListEntryWidget(changelogs, this.client, this.width, this.height, 48, this.height - 50, 20);

        this.addDrawableChild(this.listEntryWidget);
        this.addDrawableChild(this.searchField);
        this.addDrawableChild(this.backButton);
    }

    private List<String> getChangelogs() {
        List<String> changelogs = new ArrayList<>();

        for (Map.Entry<String, Boolean> changelog : ModpackUpdater.changelogList.entrySet()) {
            if (changelog.getValue()) {
                changelogs.add("+ " + changelog.getKey());
            } else {
                changelogs.add("- " + changelog.getKey());
            }
        }

        return changelogs;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        assert this.client != null;
        this.client.setScreen(this.parent);
        return false;
    }
}


