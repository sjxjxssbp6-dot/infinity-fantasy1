package cupcake.nikky.imperium.service.config;

import com.github.NeitExx.bukkit.configuration.api.DefaultConfiguration;
import com.github.NeitExx.bukkit.configuration.api.PathToDataFolder;
import com.github.NeitExx.bukkit.configuration.api.annotation.ConfigurationPath;
import lombok.Getter;
import lombok.Setter;

public class DailySettings extends DefaultConfiguration {

    @Getter
    @Setter
    @ConfigurationPath(path = "day")
    private int day;
    @Getter
    @Setter
    @ConfigurationPath(path = "week")
    private int week;
    @Getter
    @Setter
    @ConfigurationPath(path = "month")
    private int month;
    @Getter
    @Setter
    @ConfigurationPath(path = "year")
    private int year;

    @Override
    public void afterInitializing() {

    }

    @Override
    public String getConfigurationName() {
        return "daily-settings.yml";
    }

    @Override
    public PathToDataFolder getDataFolder() {
        return super.getDataFolder();
    }
}
