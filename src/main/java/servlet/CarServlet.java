package servlet;

import model.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig
public class CarServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(CarServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (!isMultipart) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            LOG.error("SC_BAD_REQUEST");
            return;
        }
        Car car = new Car();
        if (Integer.parseInt(req.getParameter("carId")) > 0) {
            car = HbmStore.instOf().findById(Car.class, Integer.parseInt(req.getParameter("carId")));
        }
        CarHelper.instOf(this.getServletContext()).fillReqInCar(req, car);
        resp.sendRedirect(req.getContextPath() + "/index.do");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cars", HbmStore.instOf().findAll(Car.class));
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("car/edit.jsp").forward(req, resp);

    }

}