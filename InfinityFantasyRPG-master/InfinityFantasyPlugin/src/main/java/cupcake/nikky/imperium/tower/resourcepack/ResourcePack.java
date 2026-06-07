package cupcake.nikky.imperium.tower.resourcepack;

import lombok.*;
import org.bukkit.Bukkit;
import ru.cristalix.wada.util.WadaUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;

@AllArgsConstructor
@RequiredArgsConstructor
public enum ResourcePack {

    DEFAULT("https://storage.c7x.dev/neitexx/INF-Resourcepack-00017.zip");

    @Getter private final String url;
    @Setter private String sha1;

    public String findSha1() {
        if (sha1 != null && !sha1.trim().equals("")) return sha1;
        return this.loadSha1();
    }

    @SneakyThrows
    public String loadSha1() {
        this.sha1 = this.getFileHashFromUrl(getUrl());
        return sha1;
    }

    private String getFileHashFromUrl(String link) throws IOException {
        var url = URI.create(link).toURL();
        var connection = (HttpURLConnection) url.openConnection();
        try {
            connection.setRequestMethod("HEAD");
            connection.setUseCaches(false);
            if (connection.getResponseCode() / 100 == 2) {
                var eTag = connection.getHeaderField("ETag");
                return eTag.replace("\"", "");
            } else {
                Bukkit.getLogger().warning("Can't get ETag from url: " + link + " Response code: " + connection.getResponseCode());
                return WadaUtil.getHashFromString(link);
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("Can't get ETag from url: " + link + " Exception: " + e.getClass().getName() + ": " + e.getMessage());
            return WadaUtil.getHashFromString(link);
        } finally {
            connection.disconnect();
        }
    }

}
