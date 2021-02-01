package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Body;
import model.EnumDataHelper;
import model.Marka;
import model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ModelServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ModelServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("models", HbmStore.instOf().findAll(Model.class));
        req.setAttribute("markas", HbmStore.instOf().findAll(Marka.class));
        req.setAttribute("user", req.getSession().getAttribute("user"));
        String string = "";

        if (req.getParameter("action") == null) {
            req.getRequestDispatcher("car.jsp").forward(req, resp);
        } else {
            if ("marka".equalsIgnoreCase(req.getParameter("action"))) {
                Collection<Marka> list = HbmStore.instOf().findAll(Marka.class);
                list.forEach(l -> l.setModels(null));
                ObjectMapper mapper = new ObjectMapper();
                string = mapper.writeValueAsString(list);
            } else if ("model".equalsIgnoreCase(req.getParameter("action"))) {
                int markaId = Integer.parseInt(req.getParameter("markaId"));
                Marka marka = HbmStore.instOf().findById(Marka.class, markaId);
                Collection<Model> list = HbmStore.instOf().findModelByMarka(marka);
                list.forEach(l -> l.setMarka(null));
                ObjectMapper mapper = new ObjectMapper();
                string = mapper.writeValueAsString(list);
            } else if ("body".equalsIgnoreCase(req.getParameter("action"))) {
                string = EnumDataHelper.instOf().getBodyMap();
            } else if ("engineType".equalsIgnoreCase(req.getParameter("action"))) {
                string = EnumDataHelper.instOf().getEngineTypeMap();
            } else if ("gear".equalsIgnoreCase(req.getParameter("action"))) {
                string = EnumDataHelper.instOf().getGearMap();
            } else if ("privod".equalsIgnoreCase(req.getParameter("action"))) {
                string = EnumDataHelper.instOf().getPrivodMap();
            }
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("json");
            resp.getWriter().write(string);
        }
    }


}
