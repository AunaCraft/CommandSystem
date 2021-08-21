package de.febanhd.fcommand;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FCommandConfig {

    public static final FCommandConfig instance = new FCommandConfig();

    private String noPermissionMessage = "§cYou dont have permissions to do that";

    private FCommandConfig() {

    }
}
