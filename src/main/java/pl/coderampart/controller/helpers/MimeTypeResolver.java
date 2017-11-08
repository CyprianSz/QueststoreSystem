package pl.coderampart.controller.helpers;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MimeTypeResolver {
    private File file;

    public MimeTypeResolver(File file) {
        this.file = file;
    }

    public String getMimeType() {
        return pl.coderampart.controller.helpers.MimeTypes.mimeTypeMapping.get(getFileExtension().toLowerCase());
    }

    public String getFileExtension(){
        Pattern pattern = Pattern.compile("\\.(\\w+)$");
        Matcher matcher = pattern.matcher(file.getName());
        matcher.find();
        return matcher.group(1);
    }
}
