package servlet;

import model.Car;
import model.Marka;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(IndexServlet.class);

    public IndexServlet() {
        BasicConfigurator.configure();
        log.info("IndexServlet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doPost");
        List<Car> cars = getCarsFilter(req);
        req.setAttribute("cars", cars);
        req.setAttribute("user", req.getSession().getAttribute("user"));
        resp.sendRedirect(req.getContextPath() + "/index.do");
    }

    private List<Car> getCarsFilter(HttpServletRequest req) {
        String filter = req.getParameter("filter");
        List<Car> cars = new ArrayList<>();
        if (filter != null) {
            int markaId = 0;
            if ("lastDay".equalsIgnoreCase(filter.toLowerCase())) {
                cars = (List<Car>) HbmStore.instOf().findCarByDay();
            } else if ("withImage".equalsIgnoreCase(filter.toLowerCase())) {
                cars = (List<Car>) HbmStore.instOf().findCarWithPhoto();
            } else if ("withMarka".equalsIgnoreCase(filter.toLowerCase())) {
                markaId = Integer.parseInt(req.getParameter("markaId"));
                Marka marka = HbmStore.instOf().findById(Marka.class, markaId);
                log.info("Marka " + marka);
                cars = (List<Car>) HbmStore.instOf().findCarByMarka(marka);
            }
            log.info("filter " + filter);
            cars.forEach(car -> log.info(car.toString()));
        } else {
            cars = (List<Car>) HbmStore.instOf().findAllCar();
        }
        log.info("cars found " + cars.size());
        return cars;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doGet");
        List<Car> cars = getCarsFilter(req);
        req.setAttribute("cars", cars);
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}