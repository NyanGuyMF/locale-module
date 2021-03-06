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

import lombok.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import voidpointer.spigot.framework.localemodule.Locale;
import voidpointer.spigot.framework.localemodule.LocalizedMessage;
import voidpointer.spigot.framework.localemodule.Message;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
abstract class AbstractLocaleConfigurationSection implements Locale {
    private Plugin plugin;
    private ConfigurationSection config;

    @Override public void addDefaults(final Message[] messages) {
        for (Message message : messages) {
            config.addDefault(message.getPath(), message.getDefaultMessage());
        }
    }

    @Override public LocalizedMessage localize(final Message message) {
        return localize(message.getPath(), message.getDefaultMessage());
    }

    @Override public LocalizedMessage localize(final String path, final String defaultMessage) {
        if (!config.isSet(path))
            plugin.getLogger().warning(String.format(Locale.MISSING_LOCALIZATION, path));
        return LocalizedMessageBuilder.builder()
                .rawMessage(config.getString(path, defaultMessage))
                .build();
    }

    @Override public LocalizedMessage localizeColorized(final Message message) {
        return localizeColorized(message.getPath(), message.getDefaultMessage());
    }

    @Override public LocalizedMessage localizeColorized(final String path, final String defaultMessage) {
        return localize(path, defaultMessage).colorize();
    }
}
