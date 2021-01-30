package servlet;

import model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class IndexServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(IndexServlet.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doPost");
        List<Car> cars = (List<Car>) HbmStore.instOf().findAll(Car.class);
        System.out.println("post " + cars);
        req.setAttribute("cars", cars);
        req.setAttribute("user", req.getSession().getAttribute("user"));
        resp.sendRedirect(req.getContextPath() + "/index.do");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doGet");
        List<Car> cars = (List<Car>) HbmStore.instOf().findAll(Car.class);
        System.out.println("get " + cars);
        req.setAttribute("cars", cars);
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}