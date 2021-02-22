package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.HbmStore;
import store.Store;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class CarHelper {
    private static final Logger log = LoggerFactory.getLogger(CarHelper.class);

    private static final FileStoreHelper FILE_STORE_HELPER = FileStoreHelper.instOf();
    private static final Store store = HbmStore.instOf();

    private static final class Lazy {
        private static final CarHelper INST = new CarHelper();
    }

    public static CarHelper instOf(ServletContext servletContext) {
        FILE_STORE_HELPER.prepareFileStore(servletContext);
        return CarHelper.Lazy.INST;
    }

    public Car fillReqInCar(HttpServletRequest req, Car car) throws IOException, ServletException {
        if (req.getPart("marka").getSize() > 0) {
            Marka marka = store.findById(Marka.class, Integer.parseInt(readFromStream(req.getPart("marka"))));
            car.setMarka(marka);
        }
        if (req.getPart("model").getSize() > 0) {
            Model model = store.findById(Model.class, Integer.parseInt(readFromStream(req.getPart("model"))));
            car.setModel(model);
        }

        if (req.getPart("body").getSize() > 0) {
            Body body = Body.valueOf(readFromStream(req.getPart("body")));
            car.setBody(body);
        }
        if (req.getPart("engineType").getSize() > 0) {
            EngineType engineType = EngineType.valueOf(readFromStream(req.getPart("engineType")));
            car.setEngineType(engineType);
        }
        if (req.getPart("gear").getSize() > 0) {
            Gear gear = Gear.valueOf(readFromStream(req.getPart("gear")));
            car.setGear(gear);
        }
        if (req.getPart("privod").getSize() > 0) {
            Privod privod = Privod.valueOf(readFromStream(req.getPart("privod")));
            car.setPrivod(privod);
        }

        int year = Integer.parseInt(readFromStream(req.getPart("year")));
        int probeg = Integer.parseInt(readFromStream(req.getPart("probeg")));
        String description = readFromStream(req.getPart("description"));
        double price = Double.parseDouble(readFromStream(req.getPart("price")));

        car.setYear(year);
        car.setPrice(price);
        car.setProbeg(probeg);
        car.setDescription(description);

        Photo photo = createPhoto(req, car);
        car.addPhotos(photo);

        User user = (User) req.getSession().getAttribute("user");
        car.setUser(user);
        store.save(car);
        return car;
    }

    private String readFromStream(Part part) throws IOException {
        log.info(part.getName());

        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8);
        int charsRead;
        while((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
            out.append(buffer, 0, charsRead);
        }
        log.info(out.toString());
        return out.toString();
    }

    public Photo createPhoto(HttpServletRequest req, Car car) throws IOException, ServletException {
        Photo photo = new Photo(0, "", car);
        File folder = FILE_STORE_HELPER.getRootFolder();
        for (Part part : req.getParts()) {
            String fileName = FILE_STORE_HELPER.extractFileName(part);
            File file = new File(folder + File.separator + fileName);
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(part.getInputStream().readAllBytes());
                photo.setPath(fileName);
                photo = store.save(photo);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return photo;
    }
}
