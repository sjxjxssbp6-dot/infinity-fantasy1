### Example of configuration class

```java
@ConfigurationKey(key = "MyId")
public class YourConfiguration extends DefaultConfiguration {
    
    @ConfigurationPath(path = "some.path.to.your.data")
    private boolean var1;
    
    @Override
    public String getConfigurationName() {
        return "my-configuration.yml";
    }

    @Override
    public void afterInitializing() {
        // TODO stuff
    }

    @Override
    public String getDirectory() {
        return super.getDirectory();
    }

    @Override
    public PathToDataFolder getDataFolder() {
        return super.getDataFolder();
    }
    
}
```

>Annotation [@ConfigurationKey](https://github.com/NeitExx/ConfigurationNikkyAPI/blob/559a470089151636c557128bb68a2b14a312d78c/src/main/java/me/neitexx/configuration/api/annotation/ConfigurationKey.java) sets the key you will use to find your configuration in the ConfigurationService. By default, this is the name of the configuration class.

>Annotation [@ConfigurationPath](https://github.com/NeitExx/ConfigurationNikkyAPI/blob/559a470089151636c557128bb68a2b14a312d78c/src/main/java/me/neitexx/configuration/api/annotation/ConfigurationPath.java) sets the path to your value in the config file.

>Default data folder is JavaPlugin#getDataFolder(); If you want to change default path you must use PathToDirectory.of(Function<JavaPlugin, File> accepter).

If you want to use your own FieldHandler and/or ReflectHandler implementation you can set it before initialization:
```java
@Override
public DefaultConfiguration initialize(@NotNull final File dataFolder) {
    this.setFieldHandler(FieldHandler);
    this.setReflectHandler(ReflectHandler);
    return super.initialize(dataFolder);
}
```

```java
// Your service initializing
ConfigurationService service = new DefaultConfigurationService().initialize(JavaPlugin);
// Or with your own ConfigurationRepository implementation
ConfigurationService service = new DefaultConfigurationService().initialize(JavaPlugin, ConfigurationRepository);

// Your configuration registration
service.register(YourConfiguration.class);

// If your configuration class annotated with @ConfigurationKey you must use the key to find your instance
service.<YourConfiguration>findById("MyId");
// Another way if you avoid using the @ConfigurationKey annotation
service.findByClass(YourConfiguration.class);
// Both of these methods will return Optional with your configuration instance or null
```
