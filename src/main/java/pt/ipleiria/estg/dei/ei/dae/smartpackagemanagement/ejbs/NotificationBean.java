package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Notification;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class NotificationBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private QueryBean<Notification> notificationQueryBean;

    public List<Notification> getNotifications(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("timestamp", "desc");
        return notificationQueryBean.getEntities(Notification.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getNotificationsCount(Map<String, String> filterMap) {
        return notificationQueryBean.getEntitiesCount(Notification.class, filterMap);
    }

}
