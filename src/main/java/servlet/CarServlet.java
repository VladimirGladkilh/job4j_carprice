package servlet;

import model.Car;
import model.FileStoreHelper;
import model.Photo;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@MultipartConfig
public class CarServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(CarServlet.class);
    private static final FileStoreHelper FILE_STORE_HELPER = FileStoreHelper.instOf();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (!isMultipart) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            LOG.error("SC_BAD_REQUEST");
            return;
        }
        FILE_STORE_HELPER.prepareFileStore(this.getServletContext());

        if (req.getParameter("deleteid") != null ){
            Car delCar = HbmStore.instOf().findById(Car.class, Integer.parseInt(req.getParameter("deleteid")));
            delCar.getPhotos().forEach(photo -> FILE_STORE_HELPER.clearStorrage(photo.getPath()));
            HbmStore.instOf().delete(delCar);
        } else {
            Car car = HbmStore.instOf().findById(Car.class, Integer.parseInt(req.getParameter("id")));
            if (car != null) {
                //todo update car info
            } else {
                createCar(req, car);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }

    private void createCar(HttpServletRequest req, Car car) throws IOException, ServletException {
        Photo photo = createPhoto(req, car);
        car.setMainPhoto(photo);
        HbmStore.instOf().save(car);
    }

    private Photo createPhoto(HttpServletRequest req, Car car) throws IOException, ServletException {
        Photo photo = new Photo(0, "");
        if (car != null && car.getMainPhoto() != null) {
            photo = car.getMainPhoto();
        }
        File folder = FILE_STORE_HELPER.getRootFolder();
        for (Part part : req.getParts()) {
            String fileName = FILE_STORE_HELPER.extractFileName(part);
            File file = new File(folder + File.separator + fileName);
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(part.getInputStream().readAllBytes());
                photo = HbmStore.instOf().save(new Photo(photo.getId(), fileName));
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return photo;
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cars", HbmStore.instOf().findAll(Car.class));
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("car.jsp").forward(req, resp);

    }

}