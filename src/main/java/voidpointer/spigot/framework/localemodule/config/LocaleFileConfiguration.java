/*
 *            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *
 * Copyright (C) 2020 Vasiliy Petukhov <void.pointer@ya.ru>
 *
 * Everyone is permitted to copy and distribute verbatim or modified
 * copies of this license document, and changing it is allowed as long
 * as the name is changed.
 *
 *            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *  0. You just DO WHAT THE FUCK YOU WANT TO.
 */

package voidpointer.spigot.framework.localemodule.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

@Getter(AccessLevel.PACKAGE)
public class LocaleFileConfiguration extends AbstractLocaleConfigurationSection {
    public static final String LOCALE_FILENAME = "locale.yml";
    public static final String MESSAGES_PATH = "messages";
    private File messagesFile;
    private FileConfiguration fileConfiguration;

    public LocaleFileConfiguration(@NonNull final Plugin plugin) {
        load(plugin);
    }

    public final void reload() {
        loadFileConfiguration();
    }

    public final void save() {
        try {
            fileConfiguration.save(messagesFile);
        } catch (IOException ioException) {
            super.getPlugin().getLogger().warning(String.format(
                    "Unable to save %s file: %s",
                    messagesFile.getAbsolutePath(),
                    ioException.getMessage()
            ));
        }
    }

    private void load(final Plugin plugin) {
        super.setPlugin(plugin);
        messagesFile = new File(plugin.getDataFolder(), LOCALE_FILENAME);
        saveDefaultMessagesFileIfNotExists();
        loadFileConfiguration();
    }

    private void saveDefaultMessagesFileIfNotExists() {
        if (messagesFile.exists())
            return;
        if (!messagesFile.getParentFile().exists())
            messagesFile.getParentFile().mkdirs();
        saveDefaultMessagesFile();
    }

    private void saveDefaultMessagesFile() {
        try {
            super.getPlugin().saveResource(messagesFile.getName(), false);
        } catch (IllegalArgumentException illegalArgumentException) {
            super.getPlugin().getLogger().warning(String.format(
                    "Unable to save default %s file: %s",
                    messagesFile.getName(),
                    illegalArgumentException.getMessage()
            ));
        }
    }

    private void loadFileConfiguration() {
        fileConfiguration = YamlConfiguration.loadConfiguration(messagesFile);
        fileConfiguration.options().copyDefaults(true).copyHeader(true);
        ConfigurationSection config = fileConfiguration.getConfigurationSection(MESSAGES_PATH);
        if (config != null) {
            super.setConfig(fileConfiguration.getConfigurationSection(MESSAGES_PATH));
        } else {
            super.setConfig(fileConfiguration.createSection(MESSAGES_PATH));
        }
    }
}
