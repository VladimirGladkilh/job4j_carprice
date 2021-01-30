package model;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.io.File;


public class FileStoreHelper {
    private static final Logger log = LoggerFactory.getLogger(FileStoreHelper.class);

    private static final class Lazy {
        private static final FileStoreHelper INST = new FileStoreHelper();
    }

    public static FileStoreHelper instOf() {
        return Lazy.INST;
    }

    public void prepareFileStore(ServletContext servletContext) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 1024);
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(1024 * 1024 * 10);
    }

    public File getRootFolder() {
        File folder = new File("images");
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }

    public String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }

    public void clearStorrage(String path) {
        File folder = getRootFolder();
        File file = new File(folder + File.separator + path);
        file.delete();
    }
}
